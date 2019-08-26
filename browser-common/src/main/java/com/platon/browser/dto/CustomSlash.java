package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.param.EvidencesParam;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:37
 * @Description:
 */
@Data
public class CustomSlash extends Slash {

    public void updateWithSlash( CustomTransaction tx , EvidencesParam param){
        this.setNodeId(param.getVerify());
        String date = JSON.toJSONString(param);
        this.setData(date);
        this.setDenefitAddr(tx.getFrom());
        this.setHash(tx.getHash());
        this.setUpdateTime(new Date());
        this.setCreateTime(new Date());
        this.setStatus(StatusEnum.SUCCESS.code);
        this.setIsQuit(YesNoEnum.YES.code);
    }

    public enum StatusEnum{
        FAILURE(1, "失败"),
        SUCCESS(2, "成功")
        ;
        public int code;
        public String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map <Integer, StatusEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }


    public enum YesNoEnum{
        YES(1, "是"),
        NO(2, "否")
        ;
        public int code;
        public String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<Integer, CustomSlash.YesNoEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(CustomSlash.YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static CustomSlash.YesNoEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( CustomSlash.YesNoEnum en){return ENUMS.containsValue(en);}
    }
}
