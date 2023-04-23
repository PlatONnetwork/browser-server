package com.platon.browser.decoder;

import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 普通合约（EVM||WASM）交易解析工具：
 * 根据tx input解得合约类型及参数
 * EVM合约没有MAGIC_NUM
 * WASM合约有MAGIC_NUM
 */
@Slf4j
public class TxInputDecodeUtil {
    private TxInputDecodeUtil(){}
    public static TxInputDecodeResult decode(String txInput) {
        TxInputDecodeResult result = new TxInputDecodeResult();
        result.setTypeEnum(Transaction.TypeEnum.EVM_CONTRACT_CREATE);
        try {
            if (StringUtils.isNotEmpty(txInput) && !txInput.equals("0x")) {
            	if(txInput.length() > 9) {
            		String prefix = txInput.substring(0, 10);
            		/**
            		 * 前八位相等就认为是wasm合约
            		 */
            		if("0x0061736d".equals(prefix)) {
            			result.setTypeEnum(Transaction.TypeEnum.WASM_CONTRACT_CREATE);
            		}
            	}

//                // 非WASM合约的创建交易的txInput解码时可能会出异常，现阶段暂时把解txInput出异常的合约创建交易认为是EVM合约创建交易
//                RlpList rlpList = RlpDecoder.decode(Hex.decode(txInput.replace("0x", "")));
//                List <RlpType> rlpTypes = rlpList.getValues();
//                if(rlpTypes.size() >= 4) {
//                    // WASM合约创建交易的input数据前四个字节是MAGIC_NUM
//                    RlpString mn0 = (RlpString)rlpTypes.get(0);
//                    RlpString mn1 = (RlpString)rlpTypes.get(1);
//                    RlpString mn2 = (RlpString)rlpTypes.get(2);
//                    RlpString mn3 = (RlpString)rlpTypes.get(3);
//                    if(
//                        mn0.asPositiveBigInteger().intValue()==0
//                        &&mn1.asPositiveBigInteger().intValue()==97
//                        &&mn2.asPositiveBigInteger().intValue()==115
//                        &&mn3.asPositiveBigInteger().intValue()==109
//                    ){
//                        result.setTypeEnum(Transaction.TypeEnum.WASM_CONTRACT_CREATE);
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("解析普通合约交易输入出错,系统将默认把当前交易识别为EVM合约创建:",e);
        }
        return result;
    }
    public static boolean isWASM(String txInput) {
        return StringUtils.startsWith(txInput, "0x0061736d");
    }


}
