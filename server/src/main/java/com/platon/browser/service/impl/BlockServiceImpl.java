package com.platon.browser.service.impl;

import com.platon.browser.common.dto.BlockDetail;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.service.BlockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    private BlockMapper blockMapper;

    @Override
    public List<BlockDetail> getBlockDetailList() {

        List<Block> blockList = blockMapper.selectByExample(new BlockExample());

        List<BlockDetail> blockDetails = new ArrayList<>();
        blockList.forEach(block -> {
            BlockDetail bd = new BlockDetail();
            BeanUtils.copyProperties(block,bd);
            blockDetails.add(bd);
        });

        return blockDetails;
    }
}
