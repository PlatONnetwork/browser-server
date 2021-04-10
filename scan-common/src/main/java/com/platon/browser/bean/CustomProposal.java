package com.platon.browser.bean;

import com.platon.browser.dao.entity.Proposal;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:34
 * @Description: 提案实体扩展类
 */
@Data
public class CustomProposal extends Proposal {
    //赞成票
    private List <CustomVote> yesList = new ArrayList <>();
    //否决票
    private List <CustomVote> noList = new ArrayList <>();
    //弃权票
    private List <CustomVote> abstentionList = new ArrayList <>();

    public static final String QUERY_FLAG = "inquiry";

    public CustomProposal(){
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }

    /**
     * 提案类型枚举类：
     *  1.文本提案
     *  2.升级提案
     *  3.参数提案
     *  4.取消提案
     */
    public enum TypeEnum {
        TEXT(1, "文本提案"),
        UPGRADE(2, "升级提案"),
        PARAMETER(3, "参数提案"),
        CANCEL(4, "取消提案");
        private int code;
        private String desc;
        TypeEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode () {
            return code;
        }
        public String getDesc () {
            return desc;
        }
        private static final Map <Integer, TypeEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }
        public static TypeEnum getEnum ( int code ) {
            return ENUMS.get(code);
        }
        public static boolean contains ( int code ) {
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
        private int code;
        private String desc;
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
        private static final Map <Integer, StatusEnum> ENUMS = new HashMap <>();
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
    /**
     * 提案信息同步类型枚举类：
     *  1.已完成
     *  2.未完成
     */
    public enum FlagEnum {
        COMPLETE(1, "已完成"),
        INCOMPLETE(2, "未完成");
        private int code;
        private String desc;
        FlagEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode () {
            return code;
        }
        public String getDesc () {
            return desc;
        }
        private static final Map <Integer, FlagEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(FlagEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }
        public static FlagEnum getEnum ( int code ) {
            return ENUMS.get(code);
        }
        public static boolean contains ( int code ) {
            return ENUMS.containsKey(code);
        }
        public static boolean contains ( FlagEnum en ) {
            return ENUMS.containsValue(en);
        }
    }
}
