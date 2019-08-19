package com.platon.browser.res.proposal;

import lombok.Data;

@Data
public class ProposalDetailsResp {
	private String proposalHash;        //提案内部标识
	private String url;                 //github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md PIP编号   eip-100
	private String type;                //提案类型   1：文本提案； 2：升级提案；  3参数提案。
	private Integer status;              //状态  1：投票中  2：通过  3：失败   4：预升级  5：升级完成    已通过=2 或4 或 5
	private String curBlock;       //当前块高
	private String endVotingBlock;      //投票结算的快高
	private Long timestamp;    //提案时间
	private String activeBlock;         //（如果投票通过）生效块高
	private String newVersion;          //升级提案升级的版本
	private String paramName;           //参数名
    private String currentValue;        //参数当前值
    private String newValue;            //参数的新值
    private String nodeName;            //发起提案的节点名称
    private String nodeId;              //发起提案的节点id
    private Integer yeas;                //同意的人
    private Integer nays;              //反对的人
    private Integer abstentions;         //弃权的人
    private String accuVerifiers;        //总人数
    private String activeBlockTime;      //生效块高预计时间  (activeBlock-curBlock)*period
    private String endVotingBlockTime;    //投票块高的时间   (endVotingBlock-curBlock)*period
    private String supportRateThreshold;   //通过率
}
