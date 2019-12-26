package com.platon.browser.service;

import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExportService {
    @Getter
    @Setter
    private static volatile boolean blockSyncDone =false;
    @Getter
    @Setter
    private static volatile boolean transactionSyncDone =false;

    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private NodeOptESRepository nodeOptESRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private RpPlanMapper rpPlanMapper;
    @Autowired
    private DelegationMapper delegationMapper;


    @Value("${paging.transaction.page-size}")
    private int transactionPageSize;
    @Value("${paging.transaction.page-count}")
    private int transactionPageCount;

    /**
     * 导出交易表交易hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportTxHash(){
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setDesc("num");
        // 分页查询区块数据
        ESResult<Block> esResult=null;
        /*for (int pageNo = 0; pageNo <= blockPageCount; pageNo++) {
            try {
                esResult = blockESRepository.search(blockConstructor, Block.class, pageNo, blockPageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")) {
                    break;
                }else {
                    log.error("【syncBlock()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Block> blocks = esResult.getRsData();
            try{
                redisBlockService.save(new HashSet<>(blocks),false);
                log.info("【syncBlock()】第{}页,{}条记录",pageNo,blocks.size());
            }catch (Exception e){
                log.error("【syncBlock()】同步区块到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(blocks.size()<blockPageSize) break;
        }*/
        //txSyncDone=true;
    }

    /**
     * 导出地址表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportAddress(){
    	List<Object[]> rows = new ArrayList<>();
    	for(int i=0;i*transactionPageSize < transactionPageCount;i++) {
    		PageHelper.startPage(i, transactionPageSize);
        	List<Address> addresses = addressMapper.selectByExample(null);
        	for(Address d:addresses) {
        		Object[] row = new Object[1];
        		row[0] = d.getAddress();
        		rows.add(row);
        	}
    	}
    	log.info("address 导出成功。总共行数：{}", rows.size());
    	this.buildFile("address.csv", rows, null);
    }

    /**
     * 导出rpplan表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportRpPlanAddress(){
    	List<Object[]> rows = new ArrayList<>();
		for(int i=0;i<20;i++) {
			PageHelper.startPage(i, 1000);
			List<RpPlan> rpPlans = rpPlanMapper.selectByExample(null);
			if(rpPlans.size() == 0) {
				break;
			}
	    	for(RpPlan d:rpPlans) {
	    		Object[] row = new Object[1];
	    		row[1] = d.getAddress();
	    		rows.add(row);
	    	}
		}
		log.info("rpplan 导出成功。总共行数：{}", rows.size());
    	this.buildFile("rpplan.csv", rows, null);
    }

    /**
     * 导出节点表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportNodeId(){
    	List<Object[]> rows = new ArrayList<>();
		PageHelper.startPage(1, 1500);
    	List<Node> nodes = nodeMapper.selectByExample(null);
    	for(Node d:nodes) {
    		Object[] row = new Object[1];
    		row[1] = d.getNodeId();
    		rows.add(row);
    	}
    	log.info("node 导出成功。总共行数：{}", rows.size());
    	this.buildFile("node.csv", rows, null);
    }

    /**
     * 导出委托表地址和节点id
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportDelegationInfo(){
    	List<Object[]> rows = new ArrayList<>();
    	for(int i=0;i*transactionPageSize < transactionPageCount;i++) {
    		PageHelper.startPage(i, transactionPageSize);
        	DelegationExample delegationExample = new DelegationExample();
        	delegationExample.setOrderByClause(" sequence desc");
        	List<Delegation> delegations = delegationMapper.selectByExample(delegationExample);
        	for(Delegation d:delegations) {
        		Object[] row = new Object[2];
        		row[0] = d.getDelegateAddr();
        		row[1] = d.getNodeId();
        		rows.add(row);
        	}
    	}
    	log.info("degation 导出成功。总共行数：{}", rows.size());
    	this.buildFile("delegation.csv", rows, null);
    }


    @Value("${fileUrl}")
    private int fileUrl;
    public void buildFile(String fileName, List<Object[]> rows, String[] headers) {
        try {
            /** 初始化输出流对象 */
            FileOutputStream fis=new FileOutputStream(fileUrl + fileName);
            /** 设置返回的头，防止csv乱码 */
            fis.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
            OutputStreamWriter outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8);
            /** 厨师书writer对象 */
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            if(headers!=null) {
            	writer.writeHeaders(headers);
            }
            writer.writeRowsAndClose(rows);
        } catch (IOException e) {
            log.error("数据输出错误:", e);
            return;
        }
        log.info("导出报表成功，路径：{}", fileUrl + fileName);
    }
}
