package com.platon.browser.service.impl;

import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BlockServiceImpl implements BlockService {

    private final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private I18nUtil i18n;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public RespPage<BlockListItem> getPage(BlockPageReq req) {
        RespPage<BlockListItem> returnData = redisCacheService.getBlockPage(req.getCid(),req.getPageNo(),req.getPageSize());
        return returnData;

        /*
        BlockPage page = new BlockPage();
        BeanUtils.copyProperties(req,page);
        int startPage = req.getPageNo()<=1?0:req.getPageNo()-1;
        int offset = startPage*req.getPageSize();
        page.setOffset(offset);
        List<Block> blocks = customBlockMapper.selectByPage(page);
        List<BlockListItem> blockList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        blocks.forEach(block -> {
            BlockListItem bean = new BlockListItem();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getNumber());
            bean.setServerTime(serverTime);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setTransaction(block.getTransactionNumber());
            blockList.add(bean);
        });
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid());
        condition.setOrderByClause("number desc");
        Long count = blockMapper.countByExample(condition);
        Long totalPages = count/req.getPageSize();
        if(count%req.getPageSize()!=0){
            totalPages+=1;
        }
        RespPage<BlockListItem> respPage = new RespPage<>();
        respPage.setTotalCount(count.intValue());
        respPage.setTotalPages(totalPages.intValue());
        respPage.setData(blockList);
        return respPage;*/
    }

    @Override
    public BlockDetail getDetail(BlockDetailReq req) {
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(req.getCid()).andNumberEqualTo(req.getHeight());
        List<Block> blocks = blockMapper.selectByExample(blockExample);
        if (blocks.size()>1){
            logger.error("duplicate block: block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.BLOCK_ERROR_DUPLICATE));
        }
        if(blocks.size()==0){
            logger.error("invalid block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.BLOCK_ERROR_NOT_EXIST));
        }
        BlockDetail returnData = new BlockDetail();
        Block initData = blocks.get(0);
        returnData.init(initData);

        // 取上一个区块
        blockExample = new BlockExample();
        blockExample.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andNumberEqualTo(req.getHeight()-1);
        blocks = blockMapper.selectByExample(blockExample);
        if (blocks.size()>1){
            logger.error("duplicate block: block number {}",req.getHeight());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.BLOCK_ERROR_DUPLICATE));
        }
        if(blocks.size()==0){
            returnData.setTimeDiff(0);
            // 当前块没有上一个块证明这是第一个块, 设置first标识
            returnData.setFirst(true);
        }else{
            Block prevBlock = blocks.get(0);
            returnData.setTimeDiff(returnData.getTimestamp()-prevBlock.getTimestamp().getTime());
        }

        /** 设置last标识 **/
        blockExample = new BlockExample();
        blockExample.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andNumberEqualTo(req.getHeight()+1);
        blocks = blockMapper.selectByExample(blockExample);
        if(blocks.size()==0){
            // 当前区块没有下一个块，则表示这是最后一个块，设置last标识
            returnData.setLast(true);
        }

        return returnData;
    }

    /**
     * req中的height参数是当前页面上的区块高度，如果direction是prev，则此方法返回的是当前页面区块的上一个区块，如果是next则返回下一个区块
     * @param req
     * @return
     */
    @Override
    public BlockDetail getDetailNavigate(BlockDetailNavigateReq req) {
        BlockDetailReq detailReq = new BlockDetailReq();
        BeanUtils.copyProperties(req,detailReq);
        // 取得上一个或下一个块
        switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
            case PREV:
                detailReq.setHeight(req.getHeight()-1);
                break;
            case NEXT:
                detailReq.setHeight(req.getHeight()+1);
                break;
        }
        BlockDetail blockDetail = getDetail(detailReq);
        return blockDetail;
    }

    @Override
    public List<Block> getList(BlockDownloadReq req) {
        BlockExample condition = new BlockExample();
        BlockExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getAddress())){
            criteria.andNodeIdEqualTo(req.getAddress());
        }
        if(req.getStartDate()!=null){
            criteria.andTimestampGreaterThanOrEqualTo(req.getStartDate());
        }
        if(req.getEndDate()!=null){
            criteria.andTimestampLessThanOrEqualTo(req.getEndDate());
        }
        condition.setOrderByClause("number desc");
        List<Block> blocks = blockMapper.selectByExample(condition);
        return blocks;
    }

    @Override
    public void clearCache(String chainId) {
        redisCacheService.clearBlockCache(chainId);
    }

    @Override
    public void updateCache(String chainId,Set<Block> data) {
        redisCacheService.updateBlockCache(chainId,data);
    }


}
