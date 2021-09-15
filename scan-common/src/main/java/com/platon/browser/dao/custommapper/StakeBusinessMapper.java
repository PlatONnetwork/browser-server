package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.param.BusinessParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface StakeBusinessMapper {
    /**
     * 发起质押
     * @param param
     */
    @Transactional
    void create(BusinessParam param);
    /**
     * 增持质押
     */
    @Transactional
    void increase(BusinessParam param);
    /**
     * 修改质押信息
     */
    @Transactional
    void modify(BusinessParam param );

    /**
     * 质押金被锁定状态下，退出质押
     */
    @Transactional
    void lockedExit(BusinessParam param);

    /**
     * 质押金未被锁定状态下，退出质押
     */
    @Transactional
    void unlockExit(BusinessParam param);

    /**
     * 更新节点信息：keybase信息，程序版本号信息
     * @param updateNodeList
     * @return
     */
	int updateNodeForTask(@Param("list") List<Node> updateNodeList);
}