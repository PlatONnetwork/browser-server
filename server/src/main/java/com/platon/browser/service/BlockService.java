package com.platon.browser.service;

import com.platon.browser.common.dto.BlockDetail;
import com.platon.browser.dao.entity.Block;

import java.util.List;

public interface BlockService {

    public List<BlockDetail> getBlockDetailList();
}
