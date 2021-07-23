package com.platon.browser.v0160.bean;

import com.alibaba.fastjson.JSON;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FixIssue1583 {
    private String nodeId;
    private Long stakingBlockNumber;
    private Integer RecoveredEpoch;
    private BigInteger totalDelegationAmount;
    private BigInteger totalDelegationRewardAmount;
    private List<RecoveredDelegation> recoveredDelegationList;

    public void addRecoveredDelegation(RecoveredDelegation recoveredDelegation) {
        if (recoveredDelegationList == null) {
            recoveredDelegationList = new ArrayList<>();
        }
        recoveredDelegationList.add(recoveredDelegation);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Long getStakingBlockNumber() {
        return stakingBlockNumber;
    }

    public void setStakingBlockNumber(Long stakingBlockNumber) {
        this.stakingBlockNumber = stakingBlockNumber;
    }

    public Integer getRecoveredEpoch() {
        return RecoveredEpoch;
    }

    public void setRecoveredEpoch(Integer recoveredEpoch) {
        RecoveredEpoch = recoveredEpoch;
    }

    public BigInteger getTotalDelegationAmount() {
        return totalDelegationAmount;
    }

    public void setTotalDelegationAmount(BigInteger totalDelegationAmount) {
        this.totalDelegationAmount = totalDelegationAmount;
    }

    public BigInteger getTotalDelegationRewardAmount() {
        return totalDelegationRewardAmount;
    }

    public void setTotalDelegationRewardAmount(BigInteger totalDelegationRewardAmount) {
        this.totalDelegationRewardAmount = totalDelegationRewardAmount;
    }

    public List<RecoveredDelegation> getRecoveredDelegationList() {
        return recoveredDelegationList;
    }

    public void setRecoveredDelegationList(List<RecoveredDelegation> recoveredDelegationList) {
        this.recoveredDelegationList = recoveredDelegationList;
    }
/**
 * 获取
 *
 * @return:
 * @date: 2021/6/25
 */
    public static List<FixIssue1583> getRecoveredDelegationInfo() {
        List<FixIssue1583> list = new ArrayList<>();

        FixIssue1583 issue1583 = new FixIssue1583();
        issue1583.setNodeId("0xf2ec2830850a4e9dd48b358f908e1f22448cf5b0314750363acfd6a531edd2056237d39d182b92891a58e7d9862a43ee143a049167b7700914c41c726fad1399");
        issue1583.setStakingBlockNumber(502569L);
        issue1583.setRecoveredEpoch(216);
        issue1583.setTotalDelegationAmount(new BigInteger("3185100027705555555556"));
        issue1583.setTotalDelegationRewardAmount(new BigInteger("8599922061705855512"));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp12trrqnpqkj2kn03cwz5ae4v7lfshvqetqrkeez", new BigInteger("1000000000000000000")));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp143ml5dd3qz3wykmg4p3vnp9eqlugd9sxmgpsux", new BigInteger("2000000000000000000")));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp1687slxxcghuhxgv3uy6v2epftn8nhn9jss2sd6", new BigInteger("9361750080000000000")));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp1dtmhmexryrg7h8tzsufrg4d9y48sne7rr5ezsj", new BigInteger("1150000000000000000")));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp1y4arxmjpy5grkp9attefax07z56wcuq2n5937u", new BigInteger("2806452320000000000")));
        issue1583.addRecoveredDelegation(new RecoveredDelegation("atp1z3w63q0q6rnrqw55s4hhu7ewfep0vcqaxt508d", new BigInteger("1612074080000000000")));
        list.add(issue1583);

        FixIssue1583 issue15832 = new FixIssue1583();
        issue15832.setNodeId("0xfff1010bbf1762d13bf13828142c612a7d287f0f1367f8104a78f001145fd788fb44b87e9eac404bc2e880602450405850ff286658781dce130aee981394551d");
        issue15832.setStakingBlockNumber(902037L);
        issue15832.setRecoveredEpoch(475);
        issue15832.setTotalDelegationAmount(new BigInteger("10986475785670000000000"));
        issue15832.setTotalDelegationRewardAmount(new BigInteger("8790854215894931178"));
        issue15832.addRecoveredDelegation(new RecoveredDelegation("atp1rek8y8nz07tdp4v469xmuyymar8qe0pnejknyw", new BigInteger("18729736290000000000")));
        issue15832.addRecoveredDelegation(new RecoveredDelegation("atp1szy8d7094kl0q82la9l2pfz3hjy99zh730flw4", new BigInteger("89940584120000000000")));
        issue15832.addRecoveredDelegation(new RecoveredDelegation("atp15wekgs8q07rs24dmnwmdgd0qhqwvqlus6dh5g5", new BigInteger("62385695880000000000")));
        issue15832.addRecoveredDelegation(new RecoveredDelegation("atp1cmfc2nea3am2znutaunaze2q8t6ttyrcu7mjqh", new BigInteger("16252405540000000000")));
        list.add(issue15832);

        return list;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(FixIssue1583.getRecoveredDelegationInfo()));
    }
}
