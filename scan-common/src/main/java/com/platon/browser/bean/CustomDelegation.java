package com.platon.browser.bean;

import com.platon.browser.dao.entity.Delegation;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description: 委托实体扩展类
 */
@Data
public class CustomDelegation extends Delegation {

    public CustomDelegation() {
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }

    /**
     * 委托是否历史类型枚举类：
     *  1.是
     *  2.否
     */
    public enum YesNoEnum{
        YES(1, "是"),
        NO(2, "否")
        ;
        private int code;
        private String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map <Integer, YesNoEnum> ENUMS = new HashMap <>();
        static {Arrays.asList(YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static YesNoEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( YesNoEnum en){return ENUMS.containsValue(en);}
    }
}
