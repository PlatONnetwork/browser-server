package com.platon.browser.dto;

import com.platon.browser.dao.entity.Proposal;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:34
 * @Description:
 */
@Data
public class CustomProposal extends Proposal {
    //赞成票
    private List <CustomVote> yesList = new ArrayList <>();
    //否决票
    private List <CustomVote> noList = new ArrayList <>();
    //弃权票
    private List <CustomVote> abstentionList = new ArrayList <>();

    public static final String queryFlag = "inquiry";

    public void updateWithCustomTransaction(CustomTransaction tx,Long accuVerSum) {
        this.setHash(tx.getHash());
        this.setYeas(0L);
        this.setNays(0L);
        this.setAbstentions(0L);
        this.setAccuVerifiers(accuVerSum);
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setTimestamp(tx.getTimestamp());
        this.setStatus(StatusEnum.VOTING.code);
        //设置成查询中，以便任务过滤并查询分析主题&描述
        this.setTopic(queryFlag);
        this.setDescription(queryFlag);
    }

    public void updateWithProposalMarkDown(ProposalMarkDownDto proposalMarkDownDto) {
       this.setTopic(proposalMarkDownDto.getTopic());
       this.setDescription(proposalMarkDownDto.getDescription());
    }

    public enum TypeEnum {
        TEXT("1", "文本提案"),
        UPGRADE("2", "升级提案"),
        CANCEL("4", "取消提案");
        public String code;
        public String desc;
        TypeEnum ( String code, String desc ) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode () {
            return code;
        }
        public String getDesc () {
            return desc;
        }
        private static Map <String, TypeEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }
        public static TypeEnum getEnum ( String code ) {
            return ENUMS.get(code);
        }
        public static boolean contains ( String code ) {
            return ENUMS.containsKey(code);
        }
        public static boolean contains ( TypeEnum en ) {
            return ENUMS.containsValue(en);
        }
    }

    public enum StatusEnum {
        VOTING(1, "投票中"),
        PASS(2, "通过"),
        FAIL(3, "失败"),
        PRE_UPGRADE(4, "预升级"),
        FINISH(5, "生效"),
        CANCEL(6, "被取消");
        public int code;
        public String desc;
        StatusEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode () {
            return code;
        }
        public String getDesc () {
            return desc;
        }
        private static Map <Integer, StatusEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }
        public static StatusEnum getEnum ( Integer code ) {
            return ENUMS.get(code);
        }
        public static boolean contains ( int code ) {
            return ENUMS.containsKey(code);
        }
        @SuppressWarnings("unlikely-arg-type")
		public static boolean contains ( TypeEnum en ) {
            return ENUMS.containsValue(en);
        }
    }
}
