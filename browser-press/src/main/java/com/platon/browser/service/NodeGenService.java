package com.platon.browser.service;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Data
@Service
public class NodeGenService {

    public List<Node> buildNodeInfo( List <Transaction> transactions, Long nowQty){
        List<Node> nodes = new ArrayList <>();
        for(Transaction t :transactions){
            if(t.getTypeEnum().equals(Transaction.TypeEnum.STAKE_CREATE)){
                Node node = new Node();
                node.setNodeId(DigestUtils.sha512Hex(nowQty.toString()));
                node.setStatSlashMultiQty(0);
                node.setStatSlashLowQty(0);
                node.setStatBlockQty(0L);
                node.setStatExpectBlockQty(0L);
                node.setStatValidAddrs(1);
                node.setIsRecommend(1);
                node.setTotalValue(new BigDecimal("2000000"));
                node.setStakingBlockNum(t.getNum());
                node.setStakingTxIndex(t.getIndex());
                node.setStakingHes(new BigDecimal("2000000"));
                node.setStakingLocked(BigDecimal.ZERO);
                node.setStakingReduction(BigDecimal.ZERO);
                node.setStakingReductionEpoch(1);
                node.setNodeName("Node-"+nowQty);
                node.setNodeIcon("nope");
                node.setExternalId("Ga");
                node.setStakingAddr(t.getFrom());
                node.setBenefitAddr(t.getFrom());
                node.setAnnualizedRate(0d);
                node.setAnnualizedRateInfo("{}");
                node.setProgramVersion(100);
                node.setBigVersion(99);
                node.setWebSite("http://www.baidu.com");
                node.setJoinTime(t.getTime());
                node.setStatus(1);
                if(nowQty <= 25){
                    node.setIsConsensus(1);
                }
                node.setIsConsensus(2);
                node.setIsSettle(1);
                if(nowQty <= 10){
                    node.setIsInit(1);
                }
                node.setIsInit(2);
                node.setStatDelegateValue(BigDecimal.ZERO);
                node.setStatDelegateReleased(BigDecimal.ZERO);
                node.setStatValidAddrs(4);
                node.setStatInvalidAddrs(0);
                node.setStatBlockRewardValue(new BigDecimal("1000"));
                node.setStatStakingRewardValue(new BigDecimal("1000"));
                node.setStatFeeRewardValue(new BigDecimal("1000"));
                node.setPredictStakingReward(new BigDecimal("1000"));
                node.setCreateTime(new Date());
                node.setUpdateTime(new Date());
                node.setStatVerifierTime(2);
                node.setDetails("balabala");
                nodes.add(node);
            }
        }
        return nodes;
    }




}