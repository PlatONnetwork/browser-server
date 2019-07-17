package com.platon.browser.util;

import com.github.pagehelper.Page;
import com.platon.browser.dto.RespPage;

import java.util.List;

public class PageUtil {
    public static <T> RespPage<T> getRespPage(Page page, List<T> data){
        RespPage<T> respPage = new RespPage<>();
        respPage.setTotalCount(page.getTotal());
        respPage.setDisplayTotalCount(page.getTotal());
        respPage.setTotalPages(page.getPages());
        respPage.setData(data);
        return respPage;
    }
}
