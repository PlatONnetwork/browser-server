package com.platon.browser.bean;

import com.platon.browser.dao.entity.Slash;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:37
 * @Description: 惩罚实体扩张类
 */
@Data
public class CustomSlash extends Slash {

    public CustomSlash(){
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }

    /**
     * 举报成败类型枚举类：
     *  1.成功
     *  2.失败
     */
    public enum StatusEnum{
        FAILURE(1, "失败"),
        SUCCESS(2, "成功")
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map <Integer, StatusEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }

    /**
     * 是否退出类型枚举类：
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
        private static final Map<Integer, CustomSlash.YesNoEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(CustomSlash.YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static CustomSlash.YesNoEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( CustomSlash.YesNoEnum en){return ENUMS.containsValue(en);}
    }
}
