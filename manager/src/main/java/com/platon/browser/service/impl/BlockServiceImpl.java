package com.platon.browser.service.impl;

import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockDetailNavigate;
import com.platon.browser.dto.block.BlockList;
import com.platon.browser.enums.BlockErrorEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {

    private final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<BlockList> getBlockList(BlockListReq req) {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid());
        condition.setOrderByClause("number desc");
        List<Block> blocks = blockMapper.selectByExample(condition);
        List<BlockList> blockList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        blocks.forEach(block -> {
            BlockList bean = new BlockList();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getNumber());
            bean.setServerTime(serverTime);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setTransaction(block.getTransactionNumber());
            blockList.add(bean);
        });
        return blockList;
    }

    @Override
    public BlockDetail getBlockDetail(BlockDetailReq req) {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andNumberEqualTo(req.getHeight());
        List<Block> blocks = blockMapper.selectByExample(condition);
        if (blocks.size()>1){
            logger.error("duplicate block: block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), BlockErrorEnum.DUPLICATE.desc);
        }
        if(blocks.size()==0){
            logger.error("invalid block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), BlockErrorEnum.NOT_EXIST.desc);
        }
        BlockDetail blockDetail = new BlockDetail();
        Block block = blocks.get(0);
        BeanUtils.copyProperties(block,blockDetail);
        blockDetail.setHeight(block.getNumber());
        blockDetail.setTimestamp(block.getTimestamp().getTime());

        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andBlockNumberEqualTo(block.getNumber());
        long tradeCount = transactionMapper.countByExample(transactionExample);
        blockDetail.setTransaction(tradeCount);

        // 取上一个区块
        condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andNumberEqualTo(req.getHeight()-1);
        blocks = blockMapper.selectByExample(condition);
        if (blocks.size()>1){
            logger.error("duplicate block: block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), BlockErrorEnum.DUPLICATE.desc);
        }
        if(blocks.size()==0){
            blockDetail.setTimeDiff(0);
            return blockDetail;
        }
        block = blocks.get(0);
        blockDetail.setTimeDiff(blockDetail.getTimestamp()-block.getTimestamp().getTime());
        return blockDetail;
    }

    /**
     * req中的height参数是当前页面上的区块高度，如果direction是prev，则此方法返回的是当前页面区块的上一个区块，如果是next则返回下一个区块
     * @param req
     * @return
     */
    @Override
    public BlockDetailNavigate getBlockDetailNavigate(BlockDetailNavigateReq req) {
        BlockDetailReq detailReq = new BlockDetailReq();
        BeanUtils.copyProperties(req,detailReq);

        switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
            case PREV:
                detailReq.setHeight(req.getHeight()-1);
                break;
            case NEXT:
                detailReq.setHeight(req.getHeight()+1);
                break;
        }

        /** 取得下一个块，记为A块 **/
        BlockDetail blockDetail = getBlockDetail(detailReq);
        BlockDetailNavigate blockDetailNavigate = new BlockDetailNavigate();
        BeanUtils.copyProperties(blockDetail,blockDetailNavigate);

        /** 取A块的上一个块，用来决定first的值 **/
        detailReq.setHeight(detailReq.getHeight()-1);
        try {
            getBlockDetail(detailReq);
        }catch (BusinessException be){
            logger.warn("已浏览至第一个区块！");
            blockDetailNavigate.setFirst(true);
        }

        /** 取A块的下一个块，用来决定last的值 **/
        detailReq.setHeight(detailReq.getHeight()+2);
        try {
            getBlockDetail(detailReq);
        }catch (BusinessException be){
            logger.warn("已浏览至最后一个区块！");
            blockDetailNavigate.setLast(true);
        }

        return blockDetailNavigate;
    }

}
