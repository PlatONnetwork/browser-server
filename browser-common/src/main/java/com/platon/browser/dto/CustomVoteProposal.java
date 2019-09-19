package com.platon.browser.dto;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.param.VotingProposalParam;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:36
 * @Description: 投票提案实体扩展类
 */
@Data
public class CustomVoteProposal extends Vote {
	private String pproposalHash;
    private String type;
    private String proposalVerifier;
    private String proposalVerifierName;
    private String url;
    private String newVersion;
    private String endVotingBlock;
    private String activeBlock;
    private Date proposalTimestamp;
    private Long yeas;
    private Long nays;
    private Long abstentions;
    private Long accuVerifiers;
    private Integer status;
    private Date proposalUpdateTime;
    private Date proposalCreateTime;
    private String pipNum;
    private Integer pipId;
    private String topic;
    private String description;
    private String canceledPipId;
    private String canceledTopic;

    public CustomVoteProposal(){
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }

    public void updateWithVote( CustomTransaction tx, VotingProposalParam param ){
        this.setHash(tx.getHash());
        this.setProposalHash(param.getProposalId());
        this.setOption(param.getOption());
        this.setTimestamp(tx.getTimestamp());
        this.setVerifier(param.getVerifier());
    }

    public OptionEnum getOptionEnum(){
        return OptionEnum.getEnum(this.getOption());
    }
    /**
     * 投票类型枚举类：
     *  1.支持
     *  2.反对
     *  3.弃权
     */
    public enum OptionEnum {
        SUPPORT("1", "支持"),
        OPPOSITION("2", "反对"),
        ABSTENTION("3", "弃权");
        private String code;
        private String desc;
        OptionEnum ( String code, String desc ) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode () {
            return code;
        }
        public String getDesc () {
            return desc;
        }
        private static Map<String, OptionEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(OptionEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }
        public static OptionEnum getEnum ( String code ) {
            return ENUMS.get(code);
        }
        public static boolean contains ( String code ) {
            return ENUMS.containsKey(code);
        }
        public static boolean contains ( OptionEnum en ) {
            return ENUMS.containsValue(en);
        }
    }
}
