package com.platon.browser.service.impl;

import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CalculateMapper;
import com.platon.browser.dto.SearchParam;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.query.Query;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxDetail;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PendingTxService pendingTxService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CalculateMapper calculateMapper;

    @Override
    public Query findInfoByParam ( SearchParam param ) {
        //以太坊内部和外部账户都是20个字节，0x开头，string长度40,加上0x
        //以太坊区块hash和交易hash都是0x打头长度33
        //1.判断是否是块高
        //2.判断是否是地址
        //3.不是以上两种情况，就为交易hash或者区块hash，需要都查询
        Query query = new Query();
        boolean isAccountOrContract = false;
        boolean isHash = false;
        String par = param.getParameter();
        boolean result=param.getParameter().matches("[0-9]+");
        if (!result) {
            //为false则可能为区块交易hash或者为账户
            if(par.length()<=2){
                throw new BusinessException("请输入长度大于2的查询关键字!");
            }
            if(par.substring(0, 2).equals("0x") && par.length() == 42){
                isAccountOrContract = true;
            }else
                isHash = true;

        }

        if (isAccountOrContract) {
            //内部账户account
            long accountSum = calculateMapper.countTransactionOrContract("account",param.getCid(),param.getParameter(),param.getParameter());
            if(accountSum > 0){
                AccountDetailReq adr = new AccountDetailReq();
                adr.setCid(param.getCid());
                adr.setAddress(param.getParameter());
                AccountDetail detail = accountService.getAccountDetail(adr);
                query.setStruct(detail);
                query.setType("account");
            }else {
                //外部账户contract
                long addressSum = calculateMapper.countTransactionOrContract("contract",param.getCid(),param.getParameter(),param.getParameter());
                if(addressSum > 0){
                    ContractDetailReq contractDetailReq = new ContractDetailReq();
                    contractDetailReq.setCid(param.getCid());
                    contractDetailReq.setAddress(param.getParameter());
                    ContractDetail detail = contractService.getContractDetail(contractDetailReq);
                    query.setStruct(detail);
                    query.setType("contract");
                }
            }
            return query;
        }

        if (isHash) {
            //交易hash或者区块hash
            long transactionSum = calculateMapper.countTransaction(param.getParameter(),param.getCid());
            if(transactionSum > 0){
                TransactionDetailReq transactionDetailReq = new TransactionDetailReq();
                transactionDetailReq.setCid(param.getCid());
                transactionDetailReq.setTxHash(param.getParameter());
                TransactionDetail transactionDetail = transactionService.getTransactionDetail(transactionDetailReq);
                query.setStruct(transactionDetail);
                query.setType("transaction");
            }else {
                long blockSum = calculateMapper.countBlock(param.getParameter(),param.getCid());
                if(blockSum > 0){
                    BlockExample blockExample = new BlockExample();
                    blockExample.createCriteria().andChainIdEqualTo(param.getCid()).andHashEqualTo(param.getParameter());
                    List<Block> blocks = blockMapper.selectByExample(blockExample);
                    BlockDetail blockDetail = new BlockDetail();
                    Block block = blocks.get(0);
                    BeanUtils.copyProperties(block,blockDetail);
                    blockDetail.setHeight(block.getNumber());
                    blockDetail.setTimestamp(block.getTimestamp().getTime());
                    query.setType("block");
                    query.setStruct(blockDetail);
                }
            }
            long  pendingTxSum = calculateMapper.countPendingTx(param.getParameter(),param.getCid());
            if(pendingTxSum > 0){
                PendingTxDetailReq pendingTxDetailReq = new PendingTxDetailReq();
                pendingTxDetailReq.setCid(param.getCid());
                pendingTxDetailReq.setTxHash(param.getParameter());
                PendingOrTransaction pendingOrTransaction = pendingTxService.getTransactionDetail(pendingTxDetailReq);
                query.setType("pending");
                query.setStruct(pendingOrTransaction.getData());
            }
            return query;
        }
        //区块高度
        query.setType("block");
        BlockDetailReq req = new BlockDetailReq();
        req.setCid(param.getCid());
        req.setHeight(Long.valueOf(param.getParameter()));
        BlockDetail blockDetail = blockService.getBlockDetail(req);
        query.setStruct(blockDetail);

        return query;
    }

    public static void main(String args[]){
        String address = "0x493301712671ada506ba6ca7891f436d29185821";
        String fix = address.substring(0,2);
        System.out.println(address.length());
        boolean a = address.substring(0, 2).equals("0x") && address.length() == 42;
        System.out.println(fix);
        System.out.println(a);
    }
}
