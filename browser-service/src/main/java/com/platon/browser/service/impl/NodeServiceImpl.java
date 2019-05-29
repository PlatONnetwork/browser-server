package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.NodeRespPage;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticsCache;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NodeTypeEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.cache.NodeCacheService;
import com.platon.browser.service.cache.StatisticCacheService;
import com.platon.browser.util.LocalCacheTool;
import com.platon.browser.util.EnergonUtil;
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
    private NodeCacheService nodeCacheService;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private PlatonClient platon;

    @Override
    public NodeRespPage<NodeListItem> getPage(NodePageReq req) {

        NodeRespPage returnData = LocalCacheTool.API_CHAINID_NODES_MAP.get(req.getCid());
        if(returnData==null){
            updateLocalNodeCache(req.getCid());
            returnData = LocalCacheTool.API_CHAINID_NODES_MAP.get(req.getCid());
        }

        // 根据关键字过滤数据
        String keyword = req.getKeyword();
        if(StringUtils.isNotBlank(keyword)){
            List<NodeListItem> oriData = returnData.getData();
            List<NodeListItem> tarData = new ArrayList<>();
            oriData.forEach(node->{
                if(req.getKeyword().startsWith("0x")){
                    // 根据节点ID查询
                    if(keyword.equals(node.getNodeId())) tarData.add(node);
                }else{
                    // 根据节点名称查询
                    if(StringUtils.isNotBlank(node.getName()) && node.getName().contains(keyword)) tarData.add(node);
                }
            });
            NodeRespPage oldReturnData = returnData;
            returnData = new NodeRespPage();
            BeanUtils.copyProperties(oldReturnData,returnData);
            returnData.setData(tarData);
        }
        return returnData;
    }

    @Override
    public void updatePushData(String chainId,Set<NodeRanking> data) {
        nodeCacheService.updateNodePushCache(chainId,data);
    }

    @Override
    public void clearPushCache(String chainId) {
        nodeCacheService.clearNodePushCache(chainId);
    }

    @Override
    public List<NodePushItem> getPushCache(String chainId) {
        List<NodePushItem> returnData = nodeCacheService.getNodePushCache(chainId);
        return returnData;
    }

    @Override
    public NodeDetail getDetail(NodeDetailReq req) {
        long beginTime = System.currentTimeMillis();
        long startTime = beginTime;
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
        logger.debug("nodeRankingMapper.selectByExample(condition) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

        // 取第一条
        NodeRanking initData = nodes.get(0);
        returnData.init(initData);

        // 先设置一般的平均出块时长
        beginTime = System.currentTimeMillis();
        StatisticsCache statisticsCache = statisticCacheService.getStatisticsCache(req.getCid());
        returnData.setAvgBlockTime(statisticsCache.getAvgTime().doubleValue());
        logger.debug("statisticCacheService.getStatisticsCache(req.getCid()) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

        beginTime = System.currentTimeMillis();
        TicketContract ticketContract = platon.getTicketContract(req.getCid());
        if(initData.getNodeType().equals(NodeTypeEnum.VALIDATOR.name().toLowerCase())){
            returnData.setTicketCount(initData.getCount().intValue());
        }else {
            // 设置得票数
            try {
                String numberOfVotes = ticketContract.GetCandidateTicketCount(returnData.getNodeId()).send();
                if(StringUtils.isNotBlank(numberOfVotes)){
                    Map<String,Integer> map = JSON.parseObject(numberOfVotes,Map.class);
                    Integer count = map.get(returnData.getNodeId().replace("0x",""));
                    returnData.setTicketCount(count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug("GetCandidateTicketCount() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);



        // 设置票龄
        beginTime = System.currentTimeMillis();
        returnData.setTicketEpoch(0l);
        try {
            String epochStr = ticketContract.GetCandidateEpoch(returnData.getNodeId()).send();
            if(StringUtils.isNotBlank(epochStr)){
                Long epoch = Long.valueOf(epochStr);
                returnData.setTicketEpoch(epoch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("GetCandidateEpoch() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);



        // 中选次数
        beginTime = System.currentTimeMillis();
        returnData.setHitCount(0l);
        Long beginNumber=returnData.getBeginNumber(),endNumber=returnData.getEndNumber();
        if(endNumber==null){
            endNumber=0l;
            // 到区块表查当前节点最新块的块号
            /*BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(initData.getChainId()).andNodeIdEqualTo(initData.getNodeId());
            blockExample.setOrderByClause("number DESC");
            PageHelper.startPage(1,1);
            List<Block> bLocks = blockMapper.selectByExample(blockExample);
            if(bLocks.size()>0){
                Block block = bLocks.get(0);
                endNumber=block.getNumber();
            }*/
            // 更换为从缓存中获取当前节点最新块号
            String maxBlockNum = nodeCacheService.getNodeMaxBlockNum(req.getCid(),req.getNodeId());
            if(StringUtils.isNotBlank(maxBlockNum)) endNumber = Long.valueOf(maxBlockNum);
        }

        if(endNumber>beginNumber){
            BigDecimal hitCount = BigDecimal.valueOf(endNumber-beginNumber).divide(BigDecimal.valueOf(250),0,RoundingMode.DOWN);
            returnData.setHitCount(hitCount.longValue());
        }
        logger.debug("setHitCount(hitCount.longValue()) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);


        logger.debug("*********Total Time Consuming: {}ms",System.currentTimeMillis()-startTime);
        return returnData;
    }

    @Override
    public List<BlockListItem> getBlockList(BlockListReq req) {
        List<BlockListItem> returnData = new LinkedList<>();

        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        NodeRanking nodeRanking = nodeRankingMapper.selectByPrimaryKey(Long.valueOf(req.getId()));
        if(nodeRanking==null) return returnData;

        downloadReq.setNodeId(nodeRanking.getNodeId());
        downloadReq.setBeginNumber(nodeRanking.getBeginNumber());
        downloadReq.setEndNumber(nodeRanking.getEndNumber());

        // 取20条最新记录
        PageHelper.startPage(1,20);
        List<Block> blocks = blockService.getList(downloadReq);

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

    /**
     * 根据请求参数查询节点列表
     * @param req
     */
    private NodeRespPage getNodePage(NodePageReq req){
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
            BigDecimal highestDeposit=BigDecimal.ZERO,lowestDeposit=BigDecimal.valueOf(Long.MAX_VALUE);
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

        // 取投票数
        try {
            String remain = ticketContract.GetPoolRemainder().send();
            if (StringUtils.isNotBlank(remain)){
                returnData.setVoteCount(51200-Long.valueOf(remain));
            }else{
                returnData.setVoteCount(0);
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
            if(initData.getNodeType() != NodeTypeEnum.VALIDATOR.name().toLowerCase()){
                nodeIds.append(initData.getNodeId()).append(":");
            }
        });

        returnData.setSelectedNodeCount(holder.selectedCount);
        returnData.setHighestDeposit(holder.highestDeposit);
        returnData.setLowestDeposit(holder.lowestDeposit);

        // 设置占比
        BigDecimal proportion = BigDecimal.valueOf(returnData.getVoteCount()).divide(BigDecimal.valueOf(51200),4, RoundingMode.DOWN);
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
            // 设置得票数
            try {
                String numberOfVote = ticketContract.GetCandidateTicketCount(ids).send();
                if(StringUtils.isNotBlank(numberOfVote)){
                    Map<String,Integer> map = JSON.parseObject(numberOfVote,Map.class);
                    data.forEach(node->{
                        Integer count = map.get(node.getNodeId().replace("0x",""));
                        if(count!=null) node.setTicketCount(count);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnData;
    }

    /**
     * 更新本地进程缓存
     * @param chainId
     */
    @Override
    public void updateLocalNodeCache(String chainId) {
        NodePageReq req = new NodePageReq();
        req.setCid(chainId);
        req.setIsValid(1);
        req.setPageNo(1);
        req.setPageSize(200);
        NodeRespPage returnData = getNodePage(req);
        LocalCacheTool.API_CHAINID_NODES_MAP.put(chainId,returnData);
    }
}
