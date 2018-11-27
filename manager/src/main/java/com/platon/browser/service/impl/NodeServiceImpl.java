package com.platon.browser.service.impl;

import com.maxmind.geoip.Location;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.node.NodeItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;

    @Override
    public List<NodeInfo> getNodeInfoList() {
        List<Node> nodeList = nodeMapper.selectByExample(new NodeExample());
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfoList.add(nodeInfo);
            BeanUtils.copyProperties(node,nodeInfo);
        });
        return nodeInfoList;
    }

    @Override
    public List<NodeItem> getNodeItemList(NodeListReq req) {
        NodeExample condition = new NodeExample();
        NodeExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getKeyword())){
            // 根据账户名称查询
            criteria.andNameEqualTo(req.getKeyword());
        }
        condition.setOrderByClause("ranking asc");
        List<Node> list = nodeMapper.selectByExample(condition);
        List<NodeItem> itemList = new LinkedList<>();
        list.forEach(node -> {
            NodeItem bean = new NodeItem();
            BeanUtils.copyProperties(node,bean);
            try {
                Location location=GeoUtil.getLocation(node.getIp());
                bean.setCountryCode(location.countryCode);
                if(StringUtils.isNotBlank(location.countryName)){
                    bean.setLocation(location.countryName);
                }
                if(StringUtils.isNotBlank(location.city)){
                    bean.setLocation(bean.getLocation()+" "+location.city);
                }
            }catch (Exception e){
                bean.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
            }
            itemList.add(bean);
        });
        return itemList;
    }

    @Override
    public NodeDetail getNodeDetail(NodeDetailReq req) {
        NodeExample condition = new NodeExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid())
            .andAddressEqualTo(req.getAddress());
        List<Node> list = nodeMapper.selectByExample(condition);
        if (list.size()>1){
            logger.error("duplicate node: node address {}",req.getAddress());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_DUPLICATE));
        }
        if(list.size()==0){
            logger.error("invalid node address {}",req.getAddress());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }

        NodeDetail nodeDetail = new NodeDetail();
        Node currentNode = list.get(0);
        BeanUtils.copyProperties(currentNode,nodeDetail);
        nodeDetail.setJoinTime(currentNode.getJoinTime().getTime());
        nodeDetail.setNodeAddress(currentNode.getIp()+":"+currentNode.getPort());

        try{
            Location location = GeoUtil.getLocation(currentNode.getIp());
            nodeDetail.setLocation(location.countryName+" "+location.city);
        }catch (Exception e){
            nodeDetail.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
        }

        try {
            Location location = GeoUtil.getLocation(currentNode.getIp());
            if(StringUtils.isNotBlank(location.countryName)){
                nodeDetail.setLocation(location.countryName);
            }
            if(StringUtils.isNotBlank(location.city)){
                nodeDetail.setLocation(nodeDetail.getLocation()+" "+location.city);
            }
        }catch (Exception e){
            nodeDetail.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
        }

        return nodeDetail;
    }

    @Override
    public List<BlockItem> getBlockList(BlockListReq req) {
        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        List<Block> blocks = blockService.getBlockList(downloadReq);
        List<BlockItem> blockItemList = new LinkedList<>();
        blocks.forEach(block -> {
            BlockItem bean = new BlockItem();
            BeanUtils.copyProperties(block,bean);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setHeight(block.getNumber());
            bean.setTransaction(block.getTransactionNumber());
            blockItemList.add(bean);
        });
        return blockItemList;
    }


}
