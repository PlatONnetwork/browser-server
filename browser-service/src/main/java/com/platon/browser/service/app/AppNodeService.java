package com.platon.browser.service.app;

import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.req.app.AppUserNodeListReq;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 10:03
 * @Description:
 */
public interface AppNodeService {
    AppNodeListWrapper list(String chainId) throws Exception;
    AppNodeDetailDto detail(String chainId, String nodeId);
    List<AppNodeDto> getUserNodeList(String chainId, AppUserNodeListReq req);
}
