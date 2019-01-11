package com.platon.browser.agent.message;

import org.springframework.stereotype.Component;

/**
 * User: dongqile
 * Date: 2018/10/30
 * Time: 18:28
 */
//@Component
public class DBStorageService {
    /*private final Logger logger = LoggerFactory.getLogger(DBStorageService.class);

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Value("${chain.id}")
    private String chainId;

    @RabbitListener(queues = "#{platonQueue.name}")
    public void receive ( String msg ) {
        logger.debug(msg);
        Message message = JSON.parseObject(msg, Message.class);
        if (!message.getChainId().equals(chainId)) {
            return;
        }
        switch (MqMessageTypeEnum.valueOf(message.getType().toUpperCase())) {
            case BLOCK:
                logger.debug("STOMP区块信息入库: {}", msg);
                BlockDto blockDto = JSON.parseObject(message.getStruct(), BlockDto.class);
                //构建dto结构，转存数据库结构
                //区块相关block
                Block block = bulidBlock(blockDto, message);
                logger.debug("DBStorageService :{ DB blockNumber = " + block.getNumber()  + "}");
                try {
                    blockMapper.insertSelective(block);
                    logger.debug("block number :" + block.getNumber());
                    logger.debug("block data insert...");
                } catch (DuplicateKeyException e) {
                    logger.debug("block data repeat...", e.getMessage());
                    return;
                }

                //交易相关transaction
                if (blockDto.getTransaction().size() > 0) {
                    List <TransactionWithBLOBs> transactionList = buildTransaction(blockDto, message);
                    try {
                        transactionMapper.batchInsert(transactionList);
                        logger.debug("transaction data insert...");
                    } catch (DuplicateKeyException e) {
                        logger.debug("transaction data repeat...,update data", e.getMessage());
                        return;
                    }
                }
                break;

            case PENDING:
                logger.debug("STOMP挂起交易信息入库: {}", msg);
                //获取信息中pending交易列表
                List <PendingTransactionDto> list = JSON.parseArray(message.getStruct(), PendingTransactionDto.class);
                List <PendingTx> pendingTxes = buidPendingTx(list, message);
                for (PendingTx pendingTx : pendingTxes) {
                    try {
                        pendingTxMapper.insert(pendingTx);
                        logger.debug("pendingtransaction data insert...");
                    } catch (DuplicateKeyException e) {
                        logger.debug("pendingtransaction data repeat...", e.getMessage(), pendingTx.getHash());
                        continue;
                    }
                }
                break;
        }
    }

    private Block bulidBlock ( BlockDto blockDto, Message message ) {
        Block block = new Block();
        try {
            BeanUtils.copyProperties(blockDto, block);
            block.setNumber(Long.valueOf(blockDto.getNumber()));
            block.setTimestamp(new Date(blockDto.getTimestamp()));
            block.setSize((int) blockDto.getSize());//考虑转换格式类型，高精度转低精度可能会导致数据失准
            block.setChainId(message.getChainId());
            block.setEnergonAverage(blockDto.getEnergonAverage().toString());
            block.setEnergonLimit(blockDto.getEnergonLimit().toString());
            block.setEnergonUsed(blockDto.getEnergonUsed().toString());
            block.setTransactionNumber(blockDto.getTransactionNumber());
            block.setCreateTime(new Date());
            block.setUpdateTime(new Date());
            *//*block.setBlockReward("");*//*
            List <TransactionDto> transactionDtos = blockDto.getTransaction();
            if (transactionDtos.size() > 0 && null != transactionDtos) {
                BigInteger sum = new BigInteger("0");
                for (TransactionDto transactionDto : transactionDtos) {
                    sum = sum.add(transactionDto.getActualTxCoast());
                }
                block.setActualTxCostSum(sum.toString());
            }
        } catch (Exception e) {
            logger.error("数据转化异常", e.getMessage());
        }
        return block;
    }

    private List <TransactionWithBLOBs> buildTransaction ( BlockDto blockDto, Message message ) {
        List <TransactionWithBLOBs> transactionList = new ArrayList <>();
        List <TransactionDto> transactionDtos = blockDto.getTransaction();
        for (TransactionDto transactionDto : transactionDtos) {
            TransactionWithBLOBs transaction = new TransactionWithBLOBs();
            BeanUtils.copyProperties(transactionDto, transaction);
            transaction.setActualTxCost(transactionDto.getActualTxCoast().toString());
            transaction.setBlockNumber(Long.valueOf(transactionDto.getBlockNumber().toString()));
            transaction.setChainId(message.getChainId());
            transaction.setTimestamp(new Date(transactionDto.getTimestamp()));
            transaction.setCreateTime(new Date());
            transaction.setUpdateTime(new Date());
            transaction.setEnergonLimit(transactionDto.getEnergonLimit().toString());
            transaction.setEnergonPrice(transactionDto.getEnergonPrice().toString());
            transaction.setEnergonUsed(transactionDto.getEnergonUsed().toString());
            transaction.setTxReceiptStatus(Integer.parseInt(transactionDto.getTxReceiptStatus().substring(2), 16));
            transaction.setTransactionIndex(transactionDto.getTransactionIndex().intValue());
            transaction.setReceiveType(transactionDto.getReceiveType());
            transaction.setInput(transactionDto.getInput() != null ? transactionDto.getInput() : "0x");
            transactionList.add(transaction);
        }
        return transactionList;
    }

    private List <PendingTx> buidPendingTx ( List <PendingTransactionDto> list, Message message ) {
        List <PendingTx> pendingTxes = new ArrayList <>();
        if (list.size() > 0) {
            for (PendingTransactionDto pendingTransactionDto : list) {
                PendingTx pendingTx = new PendingTx();
                BeanUtils.copyProperties(pendingTransactionDto, pendingTx);
                pendingTx.setUpdateTime(new Date());
                pendingTx.setCreateTime(new Date());
                pendingTx.setEnergonLimit(pendingTransactionDto.getEnergonLimit().toString());
                pendingTx.setEnergonPrice(pendingTransactionDto.getEnergonPrice().toString());
                pendingTx.setReceiveType(pendingTransactionDto.getReceiveType());
                pendingTx.setEnergonUsed("pending");
                pendingTx.setTimestamp(new Date(pendingTransactionDto.getTimestamp()));
                pendingTx.setChainId(message.getChainId());
                pendingTxes.add(pendingTx);
            }
        }
        return pendingTxes;
    }




*/



}