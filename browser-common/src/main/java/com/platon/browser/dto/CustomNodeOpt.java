package com.platon.browser.dto;

import com.platon.browser.dao.entity.NodeOpt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 19:36
 * @Description:
 */
public class CustomNodeOpt extends NodeOpt {

    public CustomNodeOpt(){
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
    }

    public CustomNodeOpt(String nodeId, DescEnum desc) {
        super();
        this.setNodeId(nodeId);
        this.setDesc(desc.code);
    }

    public void updateWithCustomTransaction(CustomTransaction tx) {
        this.setTxHash(tx.getHash());
        this.setBlockNumber(tx.getBlockNumber());
        this.setTimestamp(tx.getTimestamp());
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
    public enum DescEnum{
        CREATE("1", "创建"),
        MODIFY("2", "修改"),
        QUIT("3", "退出"),
        PROPOSALS("4", "提案"),
        VOTE("5", "投票"),
        MULTI_SIGN("6", "双签"),
        LOW_BLOCK_RATE("8", "出块率低")
        ;
        public String code;
        public String desc;
        DescEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<String,DescEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(DescEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static DescEnum getEnum(String code){
            return ENUMS.get(code);
        }
        public static boolean contains(String code){return ENUMS.containsKey(code);}
        public static boolean contains(DescEnum en){return ENUMS.containsValue(en);}
    }

}
