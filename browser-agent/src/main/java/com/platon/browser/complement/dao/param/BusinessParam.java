package com.platon.browser.complement.dao.param;

import com.platon.browser.common.enums.BusinessType;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 持久化参数基类
 */
@Data
public abstract class BusinessParam {
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
        private static final Map<Integer, YesNoEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static YesNoEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(YesNoEnum en){return ENUMS.containsValue(en);}
    }
    public abstract BusinessType getBusinessType();
}
