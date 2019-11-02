package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.persistence.dao.param.ReportDuplicateSignParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: dongqile
 * @Date: 2019/11/1
 * @Description:
 */
public interface ReportDuplicateSignMapper {

    /**
     * 双签举报
     */
    @Transactional
    void reportDuplicateSign( ReportDuplicateSignParam reportDuplicateSignParam);
}