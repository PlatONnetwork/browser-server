package com.platon.browser.utils;

import com.platon.bech32.Bech32;
import com.platon.crypto.ECDSASignature;
import com.platon.crypto.Hash;
import com.platon.crypto.Sign;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.rlp.solidity.RlpEncoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 10:52
 */
public class NodeUtil {
    private NodeUtil(){}

    /**
     * 通过区块计算节点公钥
     *
     * @param block
     * @return
     * @throws Exception
     */
    public static String getPublicKey(PlatonBlock.Block block){
        String publicKey = testBlock(block).toString(16);
        // 不足128前面补0
        int lack = 128 - publicKey.length();
        if(lack<=0) return publicKey;
        StringBuilder prefix = new StringBuilder();
        for (int i=0;i<lack;i++) prefix.append("0");
        prefix.append(publicKey);
        return prefix.toString();
    }

    public static BigInteger testBlock(PlatonBlock.Block block){
        String extraData = block.getExtraData();
        String signature = extraData.substring(66, extraData.length());
        byte[] msgHash = getMsgHash(block);
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
        return Sign.recoverFromSignature( v, new ECDSASignature(new BigInteger(1, r), new BigInteger(1, s)), msgHash);
    }

    private static byte[] getMsgHash(PlatonBlock.Block block) {
        byte[] signData = encode(block);
        return Hash.sha3(signData);
    }

    public static byte[] encode(PlatonBlock.Block block) {
        List<RlpType> values = asRlpValues(block);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(PlatonBlock.Block block) {
        List<RlpType> result = new ArrayList<>();
        //ParentHash  common.Hash    `json:"parentHash"       gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getParentHash())));
        //Coinbase    common.Address `json:"miner"            gencodec:"required"`
        result.add(RlpString.create(decodeAddress(block.getMiner())));
        //Root        common.Hash    `json:"stateRoot"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getStateRoot())));
        //TxHash      common.Hash    `json:"transactionsRoot" gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getTransactionsRoot())));
        //ReceiptHash common.Hash    `json:"receiptsRoot"     gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getReceiptsRoot())));
        //Bloom       Bloom          `json:"logsBloom"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getLogsBloom())));
        //Number      *big.Int       `json:"number"           gencodec:"required"`
        result.add(RlpString.create(block.getNumber()));
        //GasLimit    uint64         `json:"gasLimit"         gencodec:"required"`
        result.add(RlpString.create(block.getGasLimit()));
        //GasUsed     uint64         `json:"gasUsed"          gencodec:"required"`
        result.add(RlpString.create(block.getGasUsed()));
        //Time        *big.Int       `json:"timestamp"        gencodec:"required"`
        result.add(RlpString.create(block.getTimestamp()));
        //Extra       []byte         `json:"extraData"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getExtraData().substring(0, 66))));
        //Nonce       BlockNonce     `json:"nonce"`
        result.add(RlpString.create(decodeHash(block.getNonceRaw())));

        //底层1.5.0
        if (StringUtils.isNotBlank(block.getBaseFeePerGasRaw())){
            result.add(RlpString.create(block.getBaseFeePerGas()));
        }

        return result;
    }

    static byte[] decodeHash(String hex) {
        return Hex.decode(Numeric.cleanHexPrefix(hex));
    }

    static byte[] decodeAddress(String address) {
        return Bech32.addressDecode(address);
    }

}
