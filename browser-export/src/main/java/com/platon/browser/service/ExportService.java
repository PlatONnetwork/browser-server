package com.platon.browser.service;

import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
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
    private static volatile boolean txHashExportDone =false;
    @Getter
    @Setter
    private static volatile boolean addressExportDone =false;
    @Getter
    @Setter
    private static volatile boolean rpplanExportDone =false;
    @Getter
    @Setter
    private static volatile boolean nodeExportDone =false;
    @Getter
    @Setter
    private static volatile boolean delegationExportDone =false;
    @Getter
    @Setter
    private static volatile boolean proposalExportDone =false;
    @Getter
    @Setter
    private static volatile boolean voteExportDone =false;

    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private RpPlanMapper rpPlanMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private VoteMapper voteMapper;


    @Value("${paging.pageSize}")
    private int transactionPageSize;
    @Value("${paging.maxCount}")
    private int maxCount;

    /**
     * 导出交易表交易hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportTxHash(){
        String fileName = "txhash.csv";
        List<Object[]> csvRows = new ArrayList<>();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<Transaction> esResult=null;
        for (int pageNo = 0; pageNo*transactionPageSize <= maxCount; pageNo++) {
            try {
                esResult = transactionESRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")) {
                    break;
                }else {
                    log.error("【syncBlock()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0||esResult.getRsData().size()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Transaction> txList = esResult.getRsData();
            try{
                txList.forEach(tx->csvRows.add(new Object[]{tx.getHash()}));
                log.info("【exportTxHash()】第{}页,{}条记录",pageNo,txList.size());
            }catch (Exception e){
                log.error("【exportTxHash()】导出出错:",e);
                throw e;
            }
        }
        buildFile(fileName,csvRows,null);
        txHashExportDone=true;
    }

    /**
     * 导出地址表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportAddress(){
    	List<Object[]> rows = new ArrayList<>();
    	for(int pageNo=1;pageNo*transactionPageSize < maxCount;pageNo++) {
    		PageHelper.startPage(pageNo, transactionPageSize);
        	List<Address> addresses = addressMapper.selectByExample(null);
        	if(addresses.size() == 0) {
        		break;
        	}
        	for(Address d:addresses) {
        		Object[] row = new Object[1];
        		row[0] = d.getAddress();
        		rows.add(row);
        	}
        	log.info("【exportAddress()】第{}页,{}条记录",pageNo,rows.size());
    	}
    	log.info("address 导出成功。总共行数：{}", rows.size());
    	this.buildFile("address.csv", rows, null);
    	addressExportDone = true;
    }

    /**
     * 导出rpplan表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportRpPlanAddress(){
    	List<Object[]> rows = new ArrayList<>();
		for(int pageNo=1;pageNo<20;pageNo++) {
			PageHelper.startPage(pageNo, 1000);
			List<RpPlan> rpPlans = rpPlanMapper.selectByExample(null);
			if(rpPlans.size() == 0) {
				break;
			}
	    	for(RpPlan d:rpPlans) {
	    		Object[] row = new Object[1];
	    		row[1] = d.getAddress();
	    		rows.add(row);
	    	}
	    	log.info("【exportRpPlanAddress()】第{}页,{}条记录",pageNo,rows.size());
		}
		log.info("rpplan 导出成功。总共行数：{}", rows.size());
    	this.buildFile("rpplan.csv", rows, null);
    	rpplanExportDone = true;
    }
    
    /**
     * 导出提案hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportProposal(){
    	List<Object[]> rows = new ArrayList<>();
		for(int pageNo=1;pageNo<200;pageNo++) {
			PageHelper.startPage(pageNo, 1000);
			List<Proposal> proposals = proposalMapper.selectByExample(null);
			if(proposals.size() == 0) {
				break;
			}
	    	for(Proposal d:proposals) {
	    		Object[] row = new Object[1];
	    		row[1] = d.getHash();
	    		rows.add(row);
	    	}
	    	log.info("【exportProposal()】第{}页,{}条记录",pageNo,rows.size());
		}
		log.info("proposals 导出成功。总共行数：{}", rows.size());
    	this.buildFile("proposals.csv", rows, null);
    	proposalExportDone = true;
    }
    
    /**
     * 导出vote hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportVote(){
    	List<Object[]> rows = new ArrayList<>();
		for(int pageNo=1;pageNo<200;pageNo++) {
			PageHelper.startPage(pageNo, 1000);
			List<Vote> votes = voteMapper.selectByExample(null);
			if(votes.size() == 0) {
				break;
			}
	    	for(Vote d:votes) {
	    		Object[] row = new Object[1];
	    		row[1] = d.getHash();
	    		rows.add(row);
	    	}
	    	log.info("【exportVote()】第{}页,{}条记录",pageNo,rows.size());
		}
		log.info("vote 导出成功。总共行数：{}", rows.size());
    	this.buildFile("votes.csv", rows, null);
    	voteExportDone = true;
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
    		row[0] = d.getNodeId();
    		rows.add(row);
    	}
    	log.info("node 导出成功。总共行数：{}", rows.size());
    	this.buildFile("node.csv", rows, null);
    	nodeExportDone = true;
    }

    /**
     * 导出委托表地址和节点id
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportDelegationInfo(){
    	List<Object[]> rows = new ArrayList<>();
    	try {
    		for(int pageNo=1;pageNo*transactionPageSize < maxCount;pageNo++) {
        		PageHelper.startPage(pageNo, transactionPageSize);
            	DelegationExample delegationExample = new DelegationExample();
            	delegationExample.setOrderByClause(" sequence desc");
            	List<Delegation> delegations = delegationMapper.selectByExample(delegationExample);
            	if(delegations.size() == 0) {
            		break;
            	}
            	for(Delegation d:delegations) {
            		Object[] row = new Object[2];
            		row[0] = d.getDelegateAddr();
            		row[1] = d.getNodeId();
            		rows.add(row);
            	}
            	log.info("【exportDelegationInfo()】第{}页,{}条记录",pageNo,rows.size());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	log.info("degation 导出成功。总共行数：{}", rows.size());
    	this.buildFile("delegation.csv", rows, null);
    	delegationExportDone = true;
    }


    @Value("${fileUrl}")
    private String fileUrl;
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
