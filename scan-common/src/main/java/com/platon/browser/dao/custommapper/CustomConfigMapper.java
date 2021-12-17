package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface CustomConfigMapper {
    /**
     * 配置值轮换：value旧值覆盖到stale_value，参数中的新值覆盖value
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void rotateConfig(@Param("list") List<Config> configList);
}