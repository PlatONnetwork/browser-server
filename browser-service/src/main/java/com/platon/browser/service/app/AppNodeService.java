package com.platon.browser.service.app;

import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 10:03
 * @Description:
 */
public interface AppNodeService {
    AppNodeListWrapper list(String chainId) throws Exception;

    AppNodeDetailDto detail(String chainId, String nodeId);
}
