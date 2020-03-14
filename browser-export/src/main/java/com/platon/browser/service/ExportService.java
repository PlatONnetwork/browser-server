package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.NodeVersion;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
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
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.utils.HexTool;
import com.platon.sdk.contracts.ppos.dto.resp.Reward;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Getter
    @Setter
    private static volatile boolean delegationRewardExportDone =false;
    @Getter
    @Setter
    private static volatile boolean txInfoExportDone =false;
    @Autowired
    private PlatOnClient platonClient;

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
    @Autowired
	private SpecialApi specialApi;


    @Value("${paging.pageSize}")
    private int transactionPageSize;
    @Value("${paging.maxCount}")
    private int maxCount;

    @PostConstruct
    private void init(){
        File destDir =new File(fileUrl);
        if(destDir.exists()) destDir.delete();
        if (!destDir.exists()) destDir.mkdirs();
    }

    /**
     * 导出交易表交易hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportTxHash(){
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
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0||esResult.getRsData().isEmpty()){
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
        buildFile("txhash.csv",csvRows,null);
        log.info("交易HASH导出成功,总行数：{}", csvRows.size());
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
        	List<Address> data = addressMapper.selectByExample(null);
        	if(data.isEmpty()) break;
            data.forEach(e -> rows.add(new Object[]{e.getAddress()}));
        	log.info("【exportAddress()】第{}页,{}条记录",pageNo,rows.size());
    	}
    	this.buildFile("address.csv", rows, null);
        log.info("地址表导出成功,总行数：{}", rows.size());
    	addressExportDone = true;
    }

    /**
     * 导出rpplan表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportRpPlanAddress(){
    	List<Object[]> rows = new ArrayList<>();
    	try {
    		for(int pageNo=1;pageNo<20;pageNo++) {
    			PageHelper.startPage(pageNo, 1000);
    			List<RpPlan> rpPlans = rpPlanMapper.selectByExample(null);
    			if(rpPlans == null ||rpPlans.size() == 0) {
    				break;
    			}
    	    	for(RpPlan d:rpPlans) {
    	    		Object[] row = new Object[1];
    	    		row[0] = d.getAddress();
    	    		rows.add(row);
    	    	}
    	    	log.info("【exportRpPlanAddress()】第{}页,{}条记录",pageNo,rows.size());
    		}
		} catch (Exception e) {
			log.error("导出rpplan失败", e);
		}
		
		log.info("rpplan 导出成功。总共行数：{}", rows.size());
    	this.buildFile("rpplan.csv", rows, null);
        log.info("rpplan导出成功,总行数：{}", rows.size());
    	rpplanExportDone = true;
    }
    
    /**
     * 导出提案hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportProposal(){
    	List<Object[]> rows = new ArrayList<>();
    	try {
    		for(int pageNo=1;pageNo<200;pageNo++) {
    			PageHelper.startPage(pageNo, 1000);
    			List<Proposal> proposals = proposalMapper.selectByExample(null);
    			if(proposals == null || proposals.size() == 0) {
    				break;
    			}
    	    	for(Proposal d:proposals) {
    	    		Object[] row = new Object[1];
    	    		row[0] = d.getHash();
    	    		rows.add(row);
    	    	}
    	    	log.info("【exportProposal()】第{}页,{}条记录",pageNo,rows.size());
    		}
		} catch (Exception e) {
			log.error("导出proposals失败", e);
		}
		
		log.info("proposals 导出成功。总共行数：{}", rows.size());
    	this.buildFile("proposals.csv", rows, null);
        log.info("proposals导出成功,总行数：{}", rows.size());
    	proposalExportDone = true;
    }
    
    /**
     * 导出vote hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportVote(){
    	List<Object[]> rows = new ArrayList<>();
    	try {
    		for(int pageNo=1;pageNo<200;pageNo++) {
    			PageHelper.startPage(pageNo, 1000);
    			List<Vote> votes = voteMapper.selectByExample(null);
    			if(votes == null || votes.size() == 0) {
    				break;
    			}
    	    	for(Vote d:votes) {
    	    		Object[] row = new Object[1];
    	    		row[0] = d.getHash();
    	    		rows.add(row);
    	    	}
    	    	log.info("【exportVote()】第{}页,{}条记录",pageNo,rows.size());
    		}
		} catch (Exception e) {
			log.error("导出vote失败", e);
		}
    	this.buildFile("votes.csv", rows, null);
        log.info("votes导出成功,总行数：{}", rows.size());
    	voteExportDone = true;
    }

    /**
     * 导出节点表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportNodeId(){
    	List<Object[]> rows = new ArrayList<>();
		PageHelper.startPage(1, 1500);
    	List<Node> data = nodeMapper.selectByExample(null);
        data.forEach(e -> rows.add(new Object[]{e.getNodeId()}));
    	this.buildFile("node.csv", rows, null);
        log.info("nodes导出成功,总行数：{}", rows.size());
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
            	if(delegations ==  null|delegations.size() == 0) {
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
			log.error("导出委托失败", e);
		}
    	this.buildFile("delegation.csv", rows, null);
        log.info("delegations导出成功,总行数：{}", rows.size());
    	delegationExportDone = true;
    }


    @Value("${fileUrl}")
    private String fileUrl;
    public void buildFile(String fileName, List<Object[]> rows, String[] headers) {
        try {
        	File file = new File(fileUrl);
        	if(!file.exists()) {
        		file.mkdir();
        	}
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
    
    /**
     * 导出委托表奖励
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportDelegationReward(){
    	List<Object[]> rows = new ArrayList<>();
    	Object[] rowHead = new Object[2];
    	rowHead[0] = "委托地址";
    	rowHead[1] = "待领取奖励";
    	rows.add(rowHead);
    	List<String> address = new ArrayList<>();
    	try {
    		for(int pageNo=1;pageNo < Integer.MAX_VALUE;pageNo++) {
        		PageHelper.startPage(pageNo, transactionPageSize);
            	DelegationExample delegationExample = new DelegationExample();
            	delegationExample.setOrderByClause(" sequence desc");
            	List<Delegation> delegations = delegationMapper.selectByExample(delegationExample);
            	if(delegations ==  null|delegations.size() == 0) {
            		break;
            	}
            	for(Delegation d:delegations) {
            		if(address.contains(d.getDelegateAddr())) {
            			continue;
            		}
            		address.add(d.getDelegateAddr());
            		Object[] row = new Object[2];
            		row[0] = d.getDelegateAddr();
            		List<String> nodes = new ArrayList<>();
            		List<Reward> rewards = platonClient.getRewardContract().getDelegateReward(d.getDelegateAddr(),nodes).send().getData();
            		/**
            		 * 当奖励为空时直接return
            		 */
            		BigDecimal allRewards = BigDecimal.ZERO;
            		if (rewards != null) {
            			for(Reward reward : rewards) {
                			allRewards = allRewards.add(new BigDecimal(reward.getReward()));
                		}
            		}
            		row[1] = HexTool.append(EnergonUtil.format(Convert.fromVon(allRewards, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18));
            		rows.add(row);
            	}
            	log.info("【exportDelegationReward()】第{}页,{}条记录",pageNo,rows.size());
        	}
		} catch (Exception e) {
			log.error("导出委托奖励失败", e);
		}
    	this.buildFile("delegationReward.csv", rows, null);
        log.info("delegations导出成功,总行数：{}", rows.size());
        delegationRewardExportDone = true;
    }
    
    /**
     * 导出交易表交易hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportAllTx(){
    	
    	// 查询节点版本号列表
    	try {
			List<NodeVersion> versionList = specialApi.getNodeVersionList(platonClient.getWeb3jWrapper().getWeb3j());
			for (int i = 0; i < versionList.size(); i++) {
				System.out.println(versionList.get(i).getBigVersion());
			}
			System.out.println(versionList.size());
    	} catch (ContractInvokeException e) {
			e.printStackTrace();
		} catch (BlankResponseException e) {
			e.printStackTrace();
		}
    	
//        List<Object[]> csvRows = new ArrayList<>();
//        Object[] rowHead = new Object[11];
//    	rowHead[0] = "交易hash";
//    	rowHead[1] = "交易区块";
//    	rowHead[2] = "交易时间";
//    	rowHead[3] = "交易类型";
//    	rowHead[4] = "from";
//    	rowHead[5] = "to";
//    	rowHead[6] = "value";
//    	rowHead[7] = "tx fee cost";
//    	rowHead[8] = "tx amount";
//    	rowHead[9] = "tx reward";
//    	rowHead[10] = "tx info";
//    	csvRows.add(rowHead);
//        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
//        constructor.setDesc("seq");
//        // 分页查询区块数据
//        ESResult<Transaction> esResult=null;
//        for (int pageNo = 0; pageNo <= Integer.MAX_VALUE; pageNo++) {
//            try {
//                esResult = transactionESRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
//            } catch (Exception e) {
//                if(e.getMessage().contains("all shards failed")) {
//                    break;
//                }else {
//                    log.error("【syncBlock()】查询ES出错:",e);
//                }
//            }
//            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0||esResult.getRsData().isEmpty()){
//                // 如果查询结果为空则结束
//                break;
//            }
//            List<Transaction> txList = esResult.getRsData();
//            try{
//                txList.forEach(tx->{
//                	BigDecimal txAmount = BigDecimal.ZERO;
//                	BigDecimal reward = BigDecimal.ZERO;
//                	switch (Transaction.TypeEnum.getEnum(tx.getType())) {
//			    		/** 创建验证人 */
//						case STAKE_CREATE:
//							StakeCreateParam createValidatorParam = JSON.parseObject(tx.getInfo(), StakeCreateParam.class);
//							txAmount  = createValidatorParam.getAmount();
//							break;
//							/**
//							 * 增加质押
//							 */
//						case STAKE_INCREASE:
//							StakeIncreaseParam increaseStakingParam = JSON.parseObject(tx.getInfo(), StakeIncreaseParam.class);
//							txAmount  = increaseStakingParam.getAmount();
//							break;
//						/**
//						 * 退出验证人
//						 */
//						case STAKE_EXIT:
//							// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
//							StakeExitParam exitValidatorParam = JSON.parseObject(tx.getInfo(), StakeExitParam.class);
//							txAmount  = exitValidatorParam.getAmount();
//							break;
//							/**
//							 * 委托
//							 */
//						case DELEGATE_CREATE:
//							DelegateCreateParam delegateParam = JSON.parseObject(tx.getInfo(), DelegateCreateParam.class);
//							txAmount  = delegateParam.getAmount();
//							break;
//						/**
//						 * 委托赎回
//						 */
//						case DELEGATE_EXIT:
//							// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
//							// 通过txHash关联un_delegation表
//							DelegateExitParam unDelegateParam = JSON.parseObject(tx.getInfo(), DelegateExitParam.class);
//							txAmount = unDelegateParam.getAmount();
//							reward = unDelegateParam.getReward();
//							break;
//							/**
//							 * 领取奖励
//							 */
//						case CLAIM_REWARDS:
//							DelegateRewardClaimParam delegateRewardClaimParam  = JSON.parseObject(tx.getInfo(), DelegateRewardClaimParam.class);
//							for(com.platon.browser.param.claim.Reward rewardTemp:delegateRewardClaimParam.getRewardList()) {
//								reward = reward.add(rewardTemp.getReward());
//							}
//							break;
//						default:
//							break;
//                	}
//                	Object[] row = {
//                            tx.getHash(),
//                            tx.getNum(),
//                            DateUtil.timeZoneTransfer(tx.getTime(), "0", "+8"),
//                            Transaction.TypeEnum.getEnum(tx.getType()).getDesc(),
//                            tx.getFrom(),
//                            tx.getTo(),
//                            /** 数值von转换成lat，并保留十八位精确度 */
//                            HexTool.append(EnergonUtil.format(Convert.fromVon(tx.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
//                            HexTool.append(EnergonUtil.format(Convert.fromVon(tx.getCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
//                            HexTool.append(EnergonUtil.format(Convert.fromVon(txAmount, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
//                            HexTool.append(EnergonUtil.format(Convert.fromVon(reward, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
//                            tx.getInfo(),
//                    };
//                	csvRows.add(row);
//                });
//                log.info("【exportTxh()】第{}页,{}条记录",pageNo,txList.size());
//            }catch (Exception e){
//                log.error("【exportTxh()】导出出错:",e);
//                throw e;
//            }
//        }
//        buildFile("txhash.csv",csvRows,null);
//        log.info("交易数据导出成功,总行数：{}", csvRows.size());
        txInfoExportDone=true;
    }
}
