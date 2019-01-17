package com.platon.browser.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class NodeServiceImpl implements NodeService {
    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private RedisCacheServiceImpl redisCacheService;
    @Autowired
    private BlockMapper blockMapper;

    @Override
    public RespPage<NodeListItem> getPage(NodePageReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getKeyword())){
            if(req.getKeyword().startsWith("0x")){
                // 根据节点ID查询
                criteria.andNodeIdEqualTo(req.getKeyword());
            }else{
                // 根据账户名称查询
                criteria.andNameLike("%"+req.getKeyword()+"%");
            }
        }
        if(req.getIsValid()!=null){
            // 根据是否有效属性查询
            criteria.andIsValidEqualTo(req.getIsValid());
        }
        if(req.getNodeType()!=null){
            // 根据节点类型查询
            criteria.andTypeEqualTo(req.getNodeType());
        }
        condition.setOrderByClause("ranking asc");

        Page<NodeListItem> page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        List<NodeListItem> data = new LinkedList<>();

        RespPage<NodeListItem> returnData = PageUtil.getRespPage(page,data);
        if(nodes.size()==0) return returnData;

        nodes.forEach(initData -> {
            NodeListItem bean = new NodeListItem();
            bean.init(initData);
            data.add(bean);
        });
        return returnData;
    }

    @Override
    public void updatePushData(String chainId,Set<NodeRanking> data) {
        redisCacheService.updateNodePushCache(chainId,data);
    }

    @Override
    public void clearPushCache(String chainId) {
        redisCacheService.clearNodePushCache(chainId);
    }

    @Override
    public List<NodePushItem> getPushCache(String chainId) {
        List<NodePushItem> returnData = redisCacheService.getNodePushCache(chainId);
        return returnData;
    }

    @Override
    public NodeDetail getDetail(NodeDetailReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andIdEqualTo(req.getId());
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        if (nodes.size()>1){
            logger.error("duplicate node: node {} {}",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_DUPLICATE));
        }
        if(nodes.size()==0){
            logger.error("invalid node {} {}",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }
        NodeRanking initData = nodes.get(0);
        NodeDetail returnData = new NodeDetail();
        returnData.init(initData);
        return returnData;
    }

    @Override
    public List<BlockListItem> getBlockList(BlockDownloadReq req) {
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

        List<BlockListItem> returnData = new LinkedList<>();
        blocks.forEach(initData -> {
            BlockListItem bean = new BlockListItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

}
