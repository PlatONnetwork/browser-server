package com.platon.browser.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.platon.browser.dao.entity.Erc20Token;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomErc20Token extends Erc20Token {

    /**
     * 地址类型 :E，evm的代币合约，W，wasm的代币合约
     */
    public enum TypeEnum {
        EVM("E", "EVM合约"), WASM("W", "WASM合约");

        private String code;
        private String desc;

        TypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        private static final Map<String, CustomErc20Token.TypeEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(CustomErc20Token.TypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static CustomErc20Token.TypeEnum getEnum(String code) {
            return ENUMS.get(code);
        }

        public static boolean contains(String code) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains(CustomStaking.StatusEnum en) {
            return ENUMS.containsValue(en);
        }
    }

    /**
     * 合约状态 1 可见，0 隐藏
     */
    public enum StatusEnum {
        HIDE(0, "隐藏"), VISIBLE(1, "可见"),;

        private int code;
        private String desc;

        StatusEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        private static final Map<Integer, CustomErc20Token.StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(CustomErc20Token.StatusEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static CustomErc20Token.StatusEnum getEnum(Integer code) {
            return ENUMS.get(code);
        }

        public static boolean contains(Integer code) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains(CustomStaking.StatusEnum en) {
            return ENUMS.containsValue(en);
        }
    }
}