package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ContractTypeEnum {
    INNER(0, "INNER"),
    EVM(1, "EVM"),
    WASM(2, "WASM"),
    //UNKNOWN(3, "UNKNOWN"),
    ERC20_EVM(4, "ERC20_EVM"),
    ERC721_EVM(5, "ERC721_EVM"),
    ERC1155_EVM(6, "ERC1155_EVM");

    private int code;
    private String desc;

    ContractTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ContractTypeEnum> ENUMS = new HashMap<>();
    static {
        Arrays.asList(ContractTypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
    }

    public static ContractTypeEnum getEnum(Integer code) {
        return ENUMS.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public ErcTypeEnum convertToErcType(){
        switch (this){
            case ERC20_EVM:
            return ErcTypeEnum.ERC20;
            case ERC721_EVM:
                return ErcTypeEnum.ERC721;
            case ERC1155_EVM:
                return ErcTypeEnum.ERC1155;
            default:
                return null;
        }
    }

    public AddressTypeEnum convertToAddressType(){
        switch (this){
            case INNER:
                return AddressTypeEnum.INNER_CONTRACT;
            case EVM:
                return AddressTypeEnum.EVM_CONTRACT;
            case WASM:
                return AddressTypeEnum.WASM_CONTRACT;
            //case UNKNOWN:
                //return AddressTypeEnum.ACCOUNT;
            case ERC20_EVM:
                return AddressTypeEnum.ERC20_EVM_CONTRACT;
            case ERC721_EVM:
                return AddressTypeEnum.ERC721_EVM_CONTRACT;
            case ERC1155_EVM:
                return AddressTypeEnum.ERC1155_EVM_CONTRACT;
            default:
                return AddressTypeEnum.ACCOUNT;
        }
    }
}
