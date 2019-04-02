package com.platon.browser.util;

import com.platon.browser.dto.AnalysisResult;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
public class TransactionAnalysis {

    public static AnalysisResult analysis (String input , boolean onlyType) {
        AnalysisResult analysisResult = new AnalysisResult();
        if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
            RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
            List <RlpType> rlpTypes = rlpList.getValues();
            RlpList rlpList1 = (RlpList) rlpTypes.get(0);
            if(onlyType){
                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                String typecode = Hex.toHexString(rlpString.getBytes());
                byte[] hexByte = Numeric.hexStringToByteArray(typecode);
                Type transactionType = PlatOnTypeDecoder.decode(hexByte, Uint64.class);
                analysisResult.setType(transactionType.toString());
                return analysisResult;
            }
            Map <Integer, ClassName> paramTypeMap = new HashMap <>();
            for (int i = 0; i < rlpList1.getValues().size(); i++) {
                RlpString rlpString = (RlpString) rlpList1.getValues().get(i);
                String typecode = Hex.toHexString(rlpString.getBytes());
                byte[] hexByte = Numeric.hexStringToByteArray(typecode);
                switch (i) {
                    case 0:
                        Type transactionType = PlatOnTypeDecoder.decode(hexByte, Uint64.class);
                        analysisResult.setType(transactionType.getValue().toString());
                        break;
                    case 1:
                        Type functionName = PlatOnTypeDecoder.decode(hexByte, Utf8String.class);
                        analysisResult.setFunctionName(functionName.getValue().toString());
                        paramTypeMap = TransactionType.functionNameToParamTypeMap.get(functionName.getValue().toString());
                        //paramTypeMap =  TransactionType.functionNameToParamTypeMap.get(functionName.getValue().toString());
                    default:
                        if (paramTypeMap != null && paramTypeMap.size() > 0 && i > 1) {
                            ClassName className = paramTypeMap.get(i);
                            if (!org.springframework.util.StringUtils.isEmpty(className)) {
                                Type par = PlatOnTypeDecoder.decode(hexByte, paramTypeMap.get(i).getClazz());
                                analysisResult.getParameters().put(paramTypeMap.get(i).getName(), par.getValue().toString());
                                break;
                            } else {
                                throw new NullPointerException();
                            }

                        }
                }
            }
        }
        return analysisResult;
    }


    public static String getTypeName(String type){
        String typeName = null;
        if(type != null){
            switch (type) {
                case "0":
                    //主币交易转账
                    typeName = "transfer";
                    break;
                case "1":
                    //合约发布
                    typeName = "contractCreate";
                    break;
                case "2":
                    //合约调用
                    typeName = "transactionExecute";
                    break;
                case "1000":
                    //投票
                    typeName = "voteTicket";
                    break;
                case "4":
                    //权限
                    typeName = "authorization";
                    break;
                case "5":
                    //MPC交易
                    typeName = "MPCtransaction";
                    break;
                case "1001":
                    //竞选质押
                    typeName = "candidateDeposit";
                    break;
                case "1002":
                    //减持质押
                    typeName = "candidateApplyWithdraw";
                    break;
                case "1003":
                    //提取质押
                    typeName = "candidateWithdraw";
                    break;
                default:
                    typeName = "unknown";
            }
        }
        return typeName;
    }



}
