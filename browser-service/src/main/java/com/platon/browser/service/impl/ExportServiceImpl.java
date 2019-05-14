package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.enums.TransactionStatusEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.ExportService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.utils.Convert;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
    private final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Autowired
    private PlatonClient platonClient;

    @Autowired
    private NodeService nodeService;

    @Override
    public AccountDownload exportAccountCsv(AccountDownloadReq req) {
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",ymdhms.format(req.getStartDate()),ymdhms.format(req.getEndDate()));

        List<String> txTypes = new ArrayList<>();

        List<Object[]> rows = new ArrayList<>();
        List<TransactionTypeEnum> types = new ArrayList<>();
        String[] headers = null;
        String downloadFileName = "";
        switch (req.getTab()){
            case 0:
                // 交易
                logger.debug("下载类型：交易");
                txTypes.add(TransactionTypeEnum.TRANSACTION_TRANSFER.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CONTRACT_CREATE.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_TRANSACTION_EXECUTE.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_MPC_TRANSACTION.code);
                // 表头
                headers = new String[]{
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TIME),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE)
                };
                downloadFileName="transaction-";
                break;
            case 1:
                // 投票
                logger.debug("下载类型：投票");
                txTypes.add(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
                // 表头
                headers = new String[]{
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TIME),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TARGET),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TICKET_COUNT),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TICKET_PRICE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VOTE_VALUE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_REWARD),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE)
                };
                downloadFileName="vote-";
                break;
            case 2:
                logger.debug("下载类型：声明(质押、减持质押、提取质押)");
                // 声明(质押、减持质押、提取质押)
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_DEPOSIT.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_APPLY_WITHDRAW.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_WITHDRAW.code);
                // 表头
                headers = new String[]{
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TIME),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_NODE_NAME),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_DEPOSIT_VALUE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE),
                };
                downloadFileName="declaration-";
                break;
            case 3:
                // 交易
                logger.debug("下载类型：所有");
                // 用于查询合约详情中的交易
                txTypes.add(TransactionTypeEnum.TRANSACTION_TRANSFER.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CONTRACT_CREATE.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_TRANSACTION_EXECUTE.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_MPC_TRANSACTION.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_DEPOSIT.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_APPLY_WITHDRAW.code);
                txTypes.add(TransactionTypeEnum.TRANSACTION_CANDIDATE_WITHDRAW.code);
                // 表头
                headers = new String[]{
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TIME),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE),
                        i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE)
                };
                downloadFileName="declaration-";
                break;
        }

        if(headers==null){
            throw new RuntimeException("Header is null!");
        }



        // 临时数据变量
        List<AccTransactionItem> transactionItems = new ArrayList<>();
        List<String> hashList = new ArrayList <>();
        Set<String> nodeIds = new HashSet<>();
        // 根据条件查询已完成交易信息
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(req.getCid()).andFromEqualTo(req.getAddress())
                .andTimestampGreaterThanOrEqualTo(req.getStartDate()).andTimestampLessThanOrEqualTo(req.getEndDate())
                .andTxTypeIn(txTypes);
        TransactionExample.Criteria tsecond = transactionExample.createCriteria().andChainIdEqualTo(req.getCid()).andToEqualTo(req.getAddress())
                .andTimestampGreaterThanOrEqualTo(req.getStartDate()).andTimestampLessThanOrEqualTo(req.getEndDate())
                .andTxTypeIn(txTypes);
        transactionExample.or(tsecond);
        List<Transaction> transactions = transactionMapper.selectByExample(transactionExample);
        transactions.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            hashList.add(initData.getHash());

            if(StringUtils.isNotBlank(bean.getNodeId())) {
                bean.setNodeId(bean.getNodeId().startsWith("0x")?bean.getNodeId():"0x"+bean.getNodeId());
                nodeIds.add(bean.getNodeId());
            }
            transactionItems.add(bean);
        });

        // 根据条件查询待处理交易信息
        PendingTxExample pendingTxExample = new PendingTxExample();
        pendingTxExample.createCriteria().andChainIdEqualTo(req.getCid()).andFromEqualTo(req.getAddress())
                .andTimestampGreaterThanOrEqualTo(req.getStartDate()).andTimestampLessThanOrEqualTo(req.getEndDate())
                .andTxTypeIn(txTypes);
        PendingTxExample.Criteria psecond = pendingTxExample.createCriteria().andChainIdEqualTo(req.getCid()).andToEqualTo(req.getAddress())
                .andTimestampGreaterThanOrEqualTo(req.getStartDate()).andTimestampLessThanOrEqualTo(req.getEndDate())
                .andTxTypeIn(txTypes);
        pendingTxExample.or(psecond);
        List<PendingTx> pendingTxes = pendingTxMapper.selectByExample(pendingTxExample);
        pendingTxes.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            if(StringUtils.isNotBlank(bean.getNodeId())) {
                bean.setNodeId(bean.getNodeId().startsWith("0x")?bean.getNodeId():"0x"+bean.getNodeId());
                nodeIds.add(bean.getNodeId());
            }
            transactionItems.add(bean);
        });

        // 按时间倒排
        Collections.sort(transactionItems,(c1, c2)->{
            long t1 = c1.getTimestamp().getTime(),t2 = c2.getTimestamp().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });

        //根据交易hash列表获取所有hash对应的交易有效票列表
        TicketContract ticketContract = platonClient.getTicketContract(req.getCid());
        Map<String,Integer> voteHashMap = new HashMap <>();
        try {
            StringBuffer txHash = new StringBuffer();
            for(AccTransactionItem accTransactionItem : transactionItems){
                txHash.append(accTransactionItem.getTxHash()).append(":");
            }
            if(null != txHash){
                String hashs = txHash.toString();
                hashs = hashs.substring(0,hashs.lastIndexOf(":"));
                String voteNumber = ticketContract.GetTicketCountByTxHash(hashs).send();
                voteHashMap = JSON.parseObject(voteNumber,Map.class);
            }
        }catch (Exception e){
            for(AccTransactionItem accTransactionItem : transactionItems){
                voteHashMap.put(accTransactionItem.getTxHash(),0);
            }
            logger.error("get transaction voteNumber Exception !!!");
        }

        // 设置节点名称
        Map<String,String> nodeIdToName=nodeService.getNodeNameMap(req.getCid(),new ArrayList<>(nodeIds));
        Map<String,Integer> voteHashMapRef = voteHashMap;
        transactionItems.forEach(item->{
            String nodeName = nodeIdToName.get(item.getNodeId());
            if(StringUtils.isNotBlank(nodeName)) item.setNodeName(nodeName);
            else item.setNodeName("");
            Integer number = voteHashMapRef.get(item.getTxHash());
            item.setValidVoteCount(number==null?0:number);
        });


        //设置交易收益
        //分组计算收益
        Map<String, BigDecimal> incomeMap = new HashMap <>();

        //根据投票交易hash查询区块列表
        if(hashList.size()>0){
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(req.getCid()).andVoteHashIn(hashList);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            Map<String,List<Block>> groupMap = new HashMap <>();
            //根据hash分组hash-block
            blocks.forEach(block->{
                List<Block> group=groupMap.get(block.getVoteHash());
                if(group==null){
                    group=new ArrayList <>();
                    groupMap.put(block.getVoteHash(),group);
                }
                group.add(block);
            });

            groupMap.forEach((txHash,group)->{
                BigDecimal txIncome = BigDecimal.ZERO;
                for (Block block:group){
                    txIncome=txIncome.add(new BigDecimal(block.getBlockReward()).multiply(BigDecimal.valueOf(1-block.getRewardRatio())));
                }
                incomeMap.put(txHash,txIncome);
            });
        }


        transactionItems.forEach(item -> {
            BigDecimal inCome = incomeMap.get(item.getTxHash());
            if(null == inCome) item.setIncome(BigDecimal.ZERO);
            else item.setIncome(inCome);
        });


        // 生成Markdown格式内容
        int columnNum = headers.length;
        transactionItems.forEach(transaction->{
            String transactionType = i18n.i(I18nEnum.UNKNOWN_TYPE);
            try {
                TransactionTypeEnum type = TransactionTypeEnum.getEnum(transaction.getTxType());
                if (type==TransactionTypeEnum.TRANSACTION_TRANSFER){
                    if (transaction.getFrom().equals(req.getAddress())){
                        transactionType=i18n.i(I18nEnum.TRANSACTION_TRANSFER_SEND);
                    }
                    if (transaction.getTo().equals(req.getAddress())){
                        transactionType=i18n.i(I18nEnum.TRANSACTION_TRANSFER_RECEIVE);
                    }
                }else{
                    transactionType = i18n.i(I18nEnum.valueOf(type.name()));
                }
            } catch (IllegalArgumentException iae){
                logger.error("Transaction type error:{}",iae.getMessage());
            }

            Object[] row = new Object[columnNum];
            // 设置通用属性
            row[0]= transaction.getTxHash();
            row[1]= ymdhms.format(new Date(transaction.getBlockTime()));

            switch (req.getTab()){
                case 0:
                case 3:
                    row[2]= transactionType;
                    row[3]= transaction.getFrom();
                    row[4]= transaction.getTo();
                    row[5]= transaction.getValue()+"Energon";
                    row[6]= transaction.getActualTxCost()+"Energon";
                    break;
                case 1:
                    row[2]=  transaction.getNodeName();// 投票给
                    row[3]=  transaction.getValidVoteCount()+"/"+transaction.getVoteCount();// 有效票/投票数
                    row[4]=  transaction.getTicketPrice();// 票价
                    row[5]=  transaction.getValue()+"Energon";;// 投票质押
                    row[6]=  EnergonUtil.format(Convert.fromWei(transaction.getIncome(), Convert.Unit.ETHER))+"Energon"; // 奖励
                    row[7]= transaction.getActualTxCost()+"Energon"; // 交易费用
                    break;
                case 2:
                    row[2]= transactionType;
                    row[3]= transaction.getNodeName(); // 节点名称
                    row[4]= transaction.getDeposit()+"Energon"; // 质押金
                    row[5]= transaction.getActualTxCost()+"Energon"; // 交易费用
                    break;
            }
            if(row!=null) rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(headers);
        writer.writeRowsAndClose(rows);
        AccountDownload accountDownload = new AccountDownload();
        accountDownload.setData(baos.toByteArray());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        accountDownload.setFilename(downloadFileName+req.getAddress()+"-"+ymd.format(req.getEndDate())+".csv");
        accountDownload.setLength(baos.size());
        return accountDownload;
    }

    @Override
    public BlockDownload exportNodeBlockCsv(BlockDownloadReq req) {
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",ymdhms.format(req.getStartDate()),ymdhms.format(req.getEndDate()));
        // 限制最多导出3万条记录
        PageHelper.startPage(1,30000);
        List<Block> blockList = blockService.getList(req);

        List<Object[]> rows = new ArrayList<>();
        blockList.forEach(block->{
            Object[] row = {
                    block.getNumber(),
                    ymdhms.format(block.getTimestamp()),
                    block.getTransactionNumber(),
                    EnergonUtil.format(Convert.fromWei(block.getBlockReward(), Convert.Unit.ETHER).setScale(18,RoundingMode.DOWN))
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TRANSACTION_COUNT),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_REWARD)
        );
        writer.writeRowsAndClose(rows);
        BlockDownload blockDownload = new BlockDownload();
        blockDownload.setData(baos.toByteArray());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        blockDownload.setFilename("block-"+req.getNodeId()+"-"+ymd.format(req.getEndDate())+".csv");
        blockDownload.setLength(baos.size());
        return blockDownload;
    }
}
