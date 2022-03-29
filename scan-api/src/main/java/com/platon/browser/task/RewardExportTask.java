package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomBlockNodeMapper;
import com.platon.browser.dao.entity.BlockNode;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.service.elasticsearch.EsNodeOptRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.AppStatusUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: browser-server
 * @description: 计算奖励的定时器
 * @author: Rongjin Zhang
 * @create: 2020-09-23 14:30
 */
@Slf4j
@Component
public class RewardExportTask {

    @Resource
    private EsNodeOptRepository ESNodeOptRepository;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private CustomBlockNodeMapper customBlockNodeMapper;

    /**
     * 需要统计的共识轮数
     */
    @Value("${limit.num}")
    private int limitNum;

    @Value("${output.file.url}")
    private String fileUrl;

    private final static String REWARD_KEY = "REWARD_KEY";

    private final static String ROUND_KEY = "ROUND_KEY";

    @Scheduled(cron = "0/5 * * * * ?")
    public void exportRewardNode() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            Boolean v = redisTemplate.opsForValue().setIfAbsent(REWARD_KEY, "30000");
            if (v == null || !v) {
                return;
            }
            String consensus = redisTemplate.opsForValue().get(ROUND_KEY);
            int conL = 0;
            if (StringUtils.isNotBlank(consensus)) {
                conL = Integer.parseInt(consensus);
            }
            /**
             * 计算查询开始的区块和结束的区块
             */
            long startNum = this.chainConfig.getConsensusPeriodBlockCount().longValue() * conL;
            long endNum = this.chainConfig.getConsensusPeriodBlockCount().longValue() * (conL + this.limitNum);
            BigInteger blockNumber = this.platOnClient.getLatestBlockNumber();
            if (blockNumber.longValue() > endNum) {
                ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
                constructor.must(new ESQueryBuilders().range("bNum", startNum, endNum));
                List<Object> types = new ArrayList<>();
                types.add(NodeOpt.TypeEnum.QUIT.getCode());
                types.add(NodeOpt.TypeEnum.MULTI_SIGN.getCode());
                types.add(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode());
                constructor.must(new ESQueryBuilders().terms("type", types));
                ESResult<NodeOpt> nodeOpts = this.ESNodeOptRepository.search(constructor, NodeOpt.class, 1, 30000);
                List<BlockNode> blockNodes = this.customBlockNodeMapper.selectNodeByDis(conL, conL + this.limitNum);
                List<Object[]> rs = new ArrayList<>();
                /**
                 * 对于被惩罚的节点需要进行排除
                 */
                blockNodes.forEach(blockNode -> {
                    boolean flag = false;
                    for (NodeOpt nodeOpt : nodeOpts.getRsData()) {
                        if (nodeOpt.getNodeId().equals(blockNode.getNodeId())) {
                            nodeOpts.getRsData().remove(nodeOpt);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Object[] row = new Object[2];
                        row[0] = blockNode.getNodeId();
                        row[1] = blockNode.getNodeName();
                        rs.add(row);
                    }
                });
                String[] headers = new String[]{"nodeId", "nodeName"};
                this.buildFile(startNum + "_" + endNum + "_reward.csv", rs, headers);
                redisTemplate.opsForValue().set(ROUND_KEY, String.valueOf(conL + this.limitNum));
            }
        } catch (Exception e) {
            log.error("exportRewardNode fail", e);
        } finally {
            redisTemplate.delete(REWARD_KEY);
        }

    }

    private void buildFile(String fileName, List<Object[]> rows, String[] headers) {
        File file = new File(this.fileUrl);
        if (!file.exists()) file.mkdir();
        try (FileOutputStream fis = new FileOutputStream(this.fileUrl + fileName)) {
            /* 设置返回的头，防止csv乱码 */
            fis.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            try (OutputStreamWriter outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8)) {
                CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
                if (headers != null) writer.writeHeaders(headers);
                writer.writeRowsAndClose(rows);
                log.info("导出报表成功，路径：{}", this.fileUrl + fileName);
            } catch (IOException e) {
                log.error("导出报表失败:", e);
            }
        } catch (IOException e) {
            log.error("数据输出错误:", e);
        }
    }

}
