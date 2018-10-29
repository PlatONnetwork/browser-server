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
            BlockList bl = new BlockList();
            BeanUtils.copyProperties(block,bl);
            bl.setHeight(block.getNumber());
            bl.setServerTime(serverTime);
            bl.setTimestamp(block.getTimestamp().getTime());
            blockList.add(bl);
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

        return blockDetail;
    }

    @Override
    public BlockDetailNavigate getBlockDetailNavigate(BlockDetailNavigateReq req) {
        BlockDetailReq detailReq = new BlockDetailReq();
        BeanUtils.copyProperties(req,detailReq);
        BlockDetail blockDetail = getBlockDetail(detailReq);
        BlockDetailNavigate blockDetailNavigate = new BlockDetailNavigate();
        BeanUtils.copyProperties(blockDetail,blockDetailNavigate);

        switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
            case PREV:
                detailReq.setHeight(req.getHeight()-1);
                break;
            case NEXT:
                detailReq.setHeight(req.getHeight()+1);
                break;
        }
        try {
            getBlockDetail(detailReq);
            blockDetailNavigate.setLast(false);
        }catch (BusinessException be){
            blockDetailNavigate.setLast(true);
            logger.warn("已浏览至第一个或最后一个区块！");
        }

        return blockDetailNavigate;
    }

}
