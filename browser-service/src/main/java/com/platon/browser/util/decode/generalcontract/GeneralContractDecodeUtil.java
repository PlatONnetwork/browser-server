package com.platon.browser.util.decode.generalcontract;

import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.OthersTxParam;
import com.platon.browser.util.decode.innercontract.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;

import java.math.BigInteger;
import java.util.List;

/**
 * 普通合约（EVM||WASM）交易解析工具：
 * 根据tx input解得合约类型及参数
 * EVM合约没有MAGIC_NUM
 * WASM合约有MAGIC_NUM
 */
@Slf4j
public class GeneralContractDecodeUtil {
    private GeneralContractDecodeUtil(){}
    public static GeneralContractDecodedResult decode(String txInput) {
        GeneralContractDecodedResult result = new GeneralContractDecodedResult();
        try {
            if (StringUtils.isNotEmpty(txInput) && !txInput.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(txInput.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();
                if(rlpTypes.size() >= 4) {
                    // WASM合约创建交易的input数据前四个字节是MAGIC_NUM
                    RlpString mn0 = (RlpString)rlpTypes.get(0);
                    RlpString mn1 = (RlpString)rlpTypes.get(1);
                    RlpString mn2 = (RlpString)rlpTypes.get(2);
                    RlpString mn3 = (RlpString)rlpTypes.get(3);
                    if(
                        mn0.asPositiveBigInteger().intValue()==0
                        &&mn1.asPositiveBigInteger().intValue()==97
                        &&mn2.asPositiveBigInteger().intValue()==115
                        &&mn3.asPositiveBigInteger().intValue()==109
                    ){
                        result.setTypeEnum(Transaction.TypeEnum.WASM_CONTRACT_CREATE);
                    }
                }
            }else{
                result.setTypeEnum(Transaction.TypeEnum.EVM_CONTRACT_CREATE);
            }
        } catch (Exception e) {
            log.error("解析普通合约交易输入出错:",e);
        }
        return result;
    }
}
