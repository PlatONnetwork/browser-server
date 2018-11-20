package com.platon.browser.common.util;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.List;

/**
 * User: dongqile
 * Date: 2018/11/12
 * Time: 16:10
 */
public class TransactionType {
    public static  final String geTransactionTyep ( String input ) {
        String type = null;
        try {
            if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(input));
                List <RlpType> rlpTypes = rlpList.getValues();
                RlpList rlpList1 = (RlpList) rlpTypes.get(0);
                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                String typecode = Hex.toHexString(rlpString.getBytes());
                byte[] hexByte = Numeric.hexStringToByteArray(typecode);
                //todo:置换web3j jar包platon版本
                switch (type) {
                    case "0":
                        //主币交易转账
                        type = "transfer";
                        break;
                    case "1":
                        //合约发布
                        type = "contractCreate";
                        break;
                    case "2":
                        //合约调用
                        type = "transactionExecute";
                        break;
                    case "3":
                        //投票
                        type = "vote";
                        break;
                    case "4":
                        //权限
                        type = "authorization";
                        break;
                    case "5":
                        //MPC交易
                        type = "MPCtransaction";
                        break;
                }
                return type;
            }
            return type = "transfer";
        } catch (Exception e) {
            return type = "transfer";
        }

    }

}