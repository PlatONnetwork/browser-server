package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import com.platon.browser.now.service.Erc20TokenDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合约附加信息服务实现类
 *
 * @author AgentRJ
 * @create 2020-09-23 16:06
 */
@Slf4j
@Service
public class Erc20TokenDetailServiceImpl implements Erc20TokenDetailService {

    @Autowired
    private Erc20TokenDetailMapper erc20TokenDetailMapper;

    @Override
    public int save(Erc20TokenDetailWithBLOBs token) {
        return erc20TokenDetailMapper.insert(token);
    }

    @Override
    public int batchSave(List<Erc20TokenDetailWithBLOBs> list) {
        return erc20TokenDetailMapper.batchInsert(list);
    }
}
