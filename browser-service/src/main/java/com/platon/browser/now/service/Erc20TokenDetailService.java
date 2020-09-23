package com.platon.browser.now.service;

import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;

import java.util.List;

/**
 * Token附加数据服务接口定义
 *
 * @Author: AgentRJ
 * @Date: 2020/9/23
 * @Version 1.0
 */
public interface Erc20TokenDetailService {

    /**
     * 保存Token记录
     *
     * @param token token 记录
     * @return
     */
    int save(Erc20TokenDetailWithBLOBs token);

    /**
     * 批量保存token记录
     *
     * @param list 批量数据
     * @return
     */
    int batchSave(List<Erc20TokenDetailWithBLOBs> list);
}
