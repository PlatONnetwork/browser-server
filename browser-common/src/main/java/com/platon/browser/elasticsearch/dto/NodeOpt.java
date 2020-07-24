package com.platon.browser.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class NodeOpt {
    private Long id;
    private String nodeId;
    private Integer type;
    private String txHash;
    private Long bNum;
    private Date time;
    private String desc;
    private Date creTime;
    private Date updTime;

    /**
     * 1 create 创建
     2 modify 修改
     3 quit 退出
     4 proposals 提案
     5 vote 投票
     6 multisign 双签
     7 lowBlockRate 出块率低
     11 解除锁定
     */
    public enum TypeEnum{
        CREATE("1", "创建",""),
        MODIFY("2", "修改","BEFORERATE|AFTERRATE"),
        QUIT("3", "退出",""),
        PROPOSALS("4", "提案","ID|TITLE|TYPE|VERSION"),
        PARAMETER("9", "参数提案","ID|TITLE|TYPE|MODULE|NAME|VALUE"),
        VOTE("5", "投票","ID|TITLE|OPTION|TYPE|VERSION"),
        MULTI_SIGN("6", "双签","PERCENT|AMOUNT"),
        LOW_BLOCK_RATE("7", "出块率低","BLOCK_COUNT|SLASH_BLOCK_COUNT|AMOUNT|KICK_OUT"),
        VERSION("8", "版本声明","NODE_NAME|ACTIVE_NODE|VERSION"),
        INCREASE("10", "增持质押",""),
        UNLOCKED("11", "解除锁定","LOCKED_EPOCH|UNLOCKED_EPOCH|FREEZE_DURATION"),
        ;
        private String code;
        private String desc;
        private String tpl;
        TypeEnum(String code, String desc,String tpl) {
            this.code = code;
            this.desc = desc;
            this.tpl = tpl;
        }
        public String getCode(){return code;}
        public String getDesc(){return desc;}
        public String getTpl(){return tpl;}
        private static final Map<String,TypeEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static TypeEnum getEnum(String code){
            return ENUMS.get(code);
        }
        public static boolean contains(String code){return ENUMS.containsKey(code);}
        public static boolean contains(TypeEnum en){return ENUMS.containsValue(en);}
    }
}