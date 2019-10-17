package com.platon.browser.dto;

import com.platon.browser.dao.entity.NodeOpt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 19:36
 * @Description: 操作实体扩展类
 */
public class CustomNodeOpt extends NodeOpt {

    public CustomNodeOpt(){
        super();
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
    }

    public CustomNodeOpt(String nodeId, TypeEnum typeEnum) {
        this();
        this.setNodeId(nodeId);
        this.setType(typeEnum.code);
    }

    public void updateWithCustomTransaction(CustomTransaction tx) {
        this.setTxHash(tx.getHash());
        this.setBlockNumber(tx.getBlockNumber());
        this.setTimestamp(tx.getTimestamp());
    }

    public void updateWithCustomBlock(CustomBlock block) {
        this.setTxHash("BlockHash:"+block.getHash());
        this.setBlockNumber(block.getNumber());
        this.setTimestamp(block.getTimestamp());
    }

    /**
     * 1 create 创建
       2 modify 修改
       3 quit 退出
       4 proposals 提案
       5 vote 投票
       6 multisign 双签
       7 lowBlockRate 出块率低
     */
    public enum TypeEnum{
        CREATE("1", "创建",""),
        MODIFY("2", "修改",""),
        QUIT("3", "退出",""),
        PROPOSALS("4", "提案","ID|TITLE|TYPE|VERSION"),
        VOTE("5", "投票","ID|TITLE|OPTION|TYPE|VERSION"),
        MULTI_SIGN("6", "双签","PERCENT|AMOUNT"),
        LOW_BLOCK_RATE("7", "出块率低","BLOCK_COUNT|SLASH_BLOCK_COUNT|AMOUNT|KICK_OUT"),
        VERSION("8", "版本声明","NODE_NAME|ACTIVE_NODE|VERSION"),
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
