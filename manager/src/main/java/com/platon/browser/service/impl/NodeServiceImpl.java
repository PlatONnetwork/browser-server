package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.NodeRespPage;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    @Autowired
    private PlatonClient platon;

    @Override
    public NodeRespPage<NodeListItem> getPage(NodePageReq req) {
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

        RespPage<NodeListItem> pageData = PageUtil.getRespPage(page,data);
        NodeRespPage returnData = new NodeRespPage();
        BeanUtils.copyProperties(pageData,returnData);

        if(nodes.size()==0) return returnData;

        class CountHolder{
            BigDecimal highestDeposit=BigDecimal.ZERO,lowestDeposit=BigDecimal.ZERO;
            Long selectedCount=0l;
        }
        CountHolder holder = new CountHolder();

        // 取票价
        TicketContract ticketContract = platon.getTicketContract(req.getCid());
        try {
            String price = ticketContract.GetTicketPrice().send();
            if (StringUtils.isNotBlank(price)){
                returnData.setTicketPrice(Convert.fromWei(price, Convert.Unit.ETHER));
            }else {
                returnData.setTicketPrice(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            returnData.setTicketPrice(BigDecimal.ZERO);
            e.printStackTrace();
        }
        try {
            String voteCount = ticketContract.GetPoolRemainder().send();
            if (StringUtils.isNotBlank(voteCount)){
                returnData.setVoteCount(Long.valueOf(voteCount));
            }else{
                returnData.setTicketPrice(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            returnData.setVoteCount(0l);
            e.printStackTrace();
        }

        StringBuilder nodeIds = new StringBuilder();
        nodes.forEach(initData -> {
            NodeListItem bean = new NodeListItem();
            bean.init(initData);
            // 计算最低、最高质押金
            if(StringUtils.isNotBlank(bean.getDeposit())&&bean.getIsValid()==1){
                BigDecimal deposit = new BigDecimal(bean.getDeposit());
                if(holder.highestDeposit.compareTo(deposit)<0){
                    holder.highestDeposit=deposit;
                }
                if(holder.lowestDeposit.compareTo(deposit)>0){
                    holder.lowestDeposit=deposit;
                }
            }
            if(bean.getIsValid()==1){
                holder.selectedCount++;
            }
            data.add(bean);
            nodeIds.append(initData.getNodeId()).append(":");
        });

        returnData.setSelectedNodeCount(holder.selectedCount);
        returnData.setHighestDeposit(holder.highestDeposit);
        returnData.setLowestDeposit(holder.lowestDeposit);

        // 设置占比
        BigDecimal proportion = BigDecimal.valueOf(returnData.getVoteCount()).divide(BigDecimal.valueOf(51200),2, RoundingMode.DOWN);
        returnData.setProportion(proportion);

        // 取区块奖励
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(req.getCid());
        blockExample.setOrderByClause("number DESC");
        PageHelper.startPage(1,1);
        List<Block> blocks = blockMapper.selectByExample(blockExample);
        if(blocks.size()>0){
            Block block = blocks.get(0);
            returnData.setBlockReward(new BigDecimal(EnergonUtil.format(Convert.fromWei(block.getBlockReward(), Convert.Unit.ETHER))));
        }else{
            returnData.setBlockReward(BigDecimal.ZERO);
        }

        if(nodeIds.length()>0){
            String ids = nodeIds.toString();
            ids = ids.substring(0,ids.lastIndexOf(":"));
            // 设置得票数
            try {
                String ticketIds = ticketContract.GetBatchCandidateTicketIds(ids).send();
                if(StringUtils.isNotBlank(ticketIds)){
                    Map<String,List<String>> map = JSON.parseObject(ticketIds,Map.class);
                    data.forEach(node->{
                        List<String> count = map.get(node.getNodeId());
                        if(count!=null) node.setTicketCount(count.size());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        NodeDetail returnData = new NodeDetail();
        NodeRankingExample condition = new NodeRankingExample();
        condition.setOrderByClause(" create_time desc ");
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(req.getId()!=null){
            criteria.andIdEqualTo(req.getId());
        }else{
            criteria.andNodeIdEqualTo(req.getNodeId());
        }
        //criteria.andIsValidEqualTo(1);
        PageHelper.startPage(1,1);
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        if(nodes.size()==0){
            logger.error("invalid node id:{}",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }
        // 取第一条
        NodeRanking initData = nodes.get(0);
        returnData.init(initData);

        TicketContract ticketContract = platon.getTicketContract(req.getCid());
        // 设置得票数
        try {
            String ticketIds = ticketContract.GetCandidateTicketIds(returnData.getNodeId()).send();
            if(StringUtils.isNotBlank(ticketIds)){
                List<String> list = JSON.parseObject(ticketIds,List.class);
                if(list!=null) returnData.setTicketCount(list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnData;
    }

    @Override
    public List<BlockListItem> getBlockList(BlockListReq req) {
        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        List<Block> blocks = blockService.getList(downloadReq);
        List<BlockListItem> returnData = new LinkedList<>();
        blocks.forEach(initData -> {
            BlockListItem bean = new BlockListItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

    @Override
    public Map<String, String> getNodeNameMap(String chainId,List<String> nodeIds) {
        Map<String, String> map = new HashMap<>();
        if(nodeIds.size()==0) return map;
        NodeRankingExample example = new NodeRankingExample();
        example.createCriteria().andChainIdEqualTo(chainId).andNodeIdIn(nodeIds);
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(example);
        nodes.forEach(node->map.put(node.getNodeId(),node.getName()));
        return map;
    }
}
