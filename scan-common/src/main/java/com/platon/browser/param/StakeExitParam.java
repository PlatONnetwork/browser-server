package com.platon.browser.param;

import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:56
 * tyType=1003撤销质押(退出验证人)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class StakeExitParam extends TxParam{
    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId= HexUtil.prefix(nodeId);
    }

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private BigInteger stakingBlockNum;

    /**
     *  撤销金额
     */
    private BigDecimal amount;
    /**
     * 节点实际的退出区块号（质押金退还的到账区块号）
     */
    private BigInteger withdrawBlockNum;
}
