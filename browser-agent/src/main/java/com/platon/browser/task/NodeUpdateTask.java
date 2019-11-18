package com.platon.browser.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.platon.browser.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.keybase.KeyBaseUser;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.KeyBaseAnalysis;

import lombok.extern.slf4j.Slf4j;


/**
 * 节点表补充
 * <pre>
 * <pre/>
 * @author chendai
 */
@Component
@Slf4j
public class NodeUpdateTask {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
	
    @Scheduled(cron = "0/5  * * * * ?")
    public void cron () {
    	// 只有程序正常运行才执行任务
		if(!AppStatusUtil.isRunning()) return;
		try {
			String keyBaseUrl = chainConfig.getKeyBase();
			String keyBaseApi = chainConfig.getKeyBaseApi();
			//查询待补充的质押信息
			NodeExample nodeExample = new NodeExample();
			nodeExample.createCriteria().andExternalIdNotEqualTo("");
			List<Node> nodeList = nodeMapper.selectByExample(nodeExample);
			
			Map<String, Optional<KeyBaseUser>> cache = new HashMap<>();
			List<Node> updateNodeList = new ArrayList<>();

			nodeList.forEach(node -> {
				Optional<KeyBaseUser> optional = cache.computeIfAbsent(node.getExternalId(), key -> {
					String url = keyBaseUrl.concat(keyBaseApi.concat(key));
					try {
						KeyBaseUser keyBaseUser = HttpUtil.get(url,KeyBaseUser.class);
						return Optional.of(keyBaseUser);
					} catch (HttpRequestException e) {
						log.error("get keybase error",key,e);
						return Optional.ofNullable(null);
					}
				});
				
				
				optional.ifPresent(keyBaseUser ->{
					String userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
					String icon = KeyBaseAnalysis.getKeyBaseIcon(keyBaseUser);
					boolean hasChange = false;
					if(StringUtils.isNotBlank(icon)&&!icon.equals(node.getNodeIcon())){
					    node.setNodeIcon(icon);
					    hasChange = true;
					}
					if(StringUtils.isNotBlank(userName)&&!userName.equals(node.getExternalName())){
					    node.setExternalName(userName);
					    hasChange = true;
					}
					
					if(hasChange) {
						updateNodeList.add(node);
					}
				});	
			});
			
			if(!updateNodeList.isEmpty()) {
				stakeBusinessMapper.updateNodeForTask(updateNodeList);
			}
			
		} catch (Exception e) {
            log.error("on NodeUpdateTask error",e);
		}
    }   
}
