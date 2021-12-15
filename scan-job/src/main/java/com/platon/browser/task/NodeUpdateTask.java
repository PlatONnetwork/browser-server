package com.platon.browser.task;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.bean.NodeVersion;
import com.platon.browser.bean.keybase.KeyBaseUserInfo;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.HttpUtil;
import com.platon.browser.utils.KeyBaseAnalysis;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 节点表补充
 *
 * @date: 2021/11/30
 */
@Component
@Slf4j
public class NodeUpdateTask {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private StakeBusinessMapper stakeBusinessMapper;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private SpecialApi specialApi;

    /**
     * 节点表补充
     * 每5秒执行一次
     *
     * @param :
     * @return: void
     * @date: 2021/12/7
     */
    @XxlJob("nodeUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void nodeUpdate() throws Exception {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) return;
        try {
            //查询待补充的质押信息
            List<Node> nodeList = nodeMapper.selectByExample(null);
            Map<String, Optional<KeyBaseUserInfo>> cache = new HashMap<>();
            List<Node> updateNodeList = new ArrayList<>();
            // 查询节点版本号列表
            List<NodeVersion> versionList = specialApi.getNodeVersionList(platOnClient.getWeb3jWrapper().getWeb3j());
            Map<String, NodeVersion> versionMap = new HashMap<>();
            versionList.forEach(v -> versionMap.put(v.getNodeId(), v));
            // 请求URL前缀
            String prefix = chainConfig.getKeyBase().concat(chainConfig.getKeyBaseApi());
            nodeList.forEach(node -> {
                // 更新keybase相关信息
                if (node.getExternalId().trim().length() == 16) {
                    Optional<KeyBaseUserInfo> optional = cache.computeIfAbsent(node.getExternalId(), key -> {
                        String url = prefix.concat(key);
                        try {
                            KeyBaseUserInfo keyBaseUser = HttpUtil.get(url, KeyBaseUserInfo.class);
                            return Optional.of(keyBaseUser);
                        } catch (HttpRequestException e) {
                            log.error("get keybase error:key={}", key, e);
                            return Optional.ofNullable(null);
                        }
                    });
                    optional.ifPresent(keyBaseUser -> {
                        boolean hasChange = false;
                        try {
                            String userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
                            String icon = KeyBaseAnalysis.getKeyBaseIcon(keyBaseUser);

                            if (StringUtils.isNotBlank(icon) && !icon.equals(node.getNodeIcon())) {
                                node.setNodeIcon(icon);
                                hasChange = true;
                            }
                            if (StringUtils.isNotBlank(userName) && !userName.equals(node.getExternalName())) {
                                node.setExternalName(userName);
                                hasChange = true;
                            }
                        } catch (Exception e) {
                            log.error("get keybase error:keyBaseUser={}", JSONObject.toJSONString(keyBaseUser), e);
                        }
                        if (hasChange) {
                            updateNodeList.add(node);
                        }
                    });
                }
                // 更新节点版本号相关信息
                NodeVersion version = versionMap.get(node.getNodeId());
                if (version != null && (!version.getBigVersion().equals(node.getBigVersion()) || !version.getProgramVersion().equals(node.getProgramVersion()))) {
                    node.setBigVersion(version.getBigVersion());
                    node.setProgramVersion(version.getProgramVersion());
                    updateNodeList.add(node);
                }
            });
            if (!updateNodeList.isEmpty()) {
                stakeBusinessMapper.updateNodeForTask(updateNodeList);
            }
            XxlJobHelper.handleSuccess("节点表补充成功");
        } catch (Exception e) {
            log.error("节点表补充异常", e);
            throw e;
        }
    }

}
