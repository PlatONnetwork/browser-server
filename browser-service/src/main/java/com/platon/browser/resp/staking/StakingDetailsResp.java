package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class StakingDetailsResp {
	private String nodeName;            //验证人名称
    private String stakingIcon;         //验证人图标
    private Integer status;              //状态   1:候选中  2:活跃中  3:出块中
    private String totalValue;          //质押总数=有效的质押+委托
    private String delegateValue;       //委托总数
    private String stakingValue;        //质押总数
    private Integer delegateQty;         //委托人数
    private Integer slashLowQty;         //低出块率举报次数
    private Integer slashMultiQty;       //多签举报次数
    private Long blockQty;           //产生的区块数
    private Long expectBlockQty;      //预计的出块数
    private String expectedIncome;      //预计年收化率（从验证人加入时刻开始计算）
    private String joinTime;            //加入时间
    private Integer verifierTime;        //进入共识验证轮次数
    private String rewardValue;         //累计的收益 
    private String nodeId;              //节点id
    private String stakingAddr;         //发起质押的账户地址
    private String denefitAddr;         //收益地址
    private String website;             //节点的第三方主页
    private String details;             //节点的描述
    private String externalId;          //身份证id
    private Long stakingBlockNum;     //最新的质押交易块高
    private String statDelegateReduction;//待提取的委托
    private Long leaveTime;  //退出时间
    private Boolean isInit;          //是否为初始节点 
}
