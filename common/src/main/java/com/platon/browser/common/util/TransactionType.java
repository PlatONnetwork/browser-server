package com.platon.browser.common.util;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Int64;
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
                Type result = PlatOnTypeDecoder.decode(hexByte, Int64.class);
                //todo:置换web3j jar包platon版本
                switch (result.getValue().toString()) {
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
                    case "1000":
                        //投票
                        type = "voteTicket";
                        break;
                    case "4":
                        //权限
                        type = "authorization";
                        break;
                    case "5":
                        //MPC交易
                        type = "MPCtransaction";
                        break;
                    case "1001":
                        //竞选质押
                        type = "candidateDeposit";
                    case "1002":
                        //减持质押
                        type = "candidateApplyWithdraw";
                    case "1003":
                        //提取质押
                        type = "candidateWithdraw";
                }
                return type;
            }
            return type = "transfer";
        } catch (Exception e) {
            return type = "transfer";
        }

    }
    public static void main(String args[]){
        String input = "0x";
        RlpList rlpList = RlpDecoder.decode(Hex.decode("f8c28800000000000000028a566f74655469636b657488000000000000000aa00000000000000000000000000000000000000000000000000000000000000001b88230783166336138363732333438666636623738396534313637363261643533653639303633313338623865623464383738303130313635386632346232333639663161386530393439393232366234363764386263306334653033653164633930336466383537656562336336373733336432316236616165653238343065343239"));
        List <RlpType> rlpTypes = rlpList.getValues();
        RlpList rlpList1 = (RlpList) rlpTypes.get(0);
        RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
        String typecode = Hex.toHexString(rlpString.getBytes());
        byte[] hexByte = Numeric.hexStringToByteArray(typecode);
        Type result = PlatOnTypeDecoder.decode(hexByte, Int64.class);
        System.out.println(result.getValue());
    }

}