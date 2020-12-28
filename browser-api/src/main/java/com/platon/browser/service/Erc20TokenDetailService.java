package com.platon.browser.service;

import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 合约附加信息服务实现类
 *
 * @author AgentRJ
 * @create 2020-09-23 16:06
 */
@Slf4j
@Service
public class Erc20TokenDetailService {

    @Resource
    private Erc20TokenDetailMapper erc20TokenDetailMapper;

    public int save(Erc20TokenDetailWithBLOBs token) {
        return erc20TokenDetailMapper.insert(token);
    }

    public int batchSave(List<Erc20TokenDetailWithBLOBs> list) {
        return erc20TokenDetailMapper.batchInsert(list);
    }
}
