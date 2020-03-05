package com.platon.browser.task;

import com.platon.browser.client.NodeVersion;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.keybase.KeyBaseUserInfo;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.HttpUtil;
import com.platon.browser.util.KeyBaseAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


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
	@Autowired
	private PlatOnClient platOnClient;
    @Autowired
	private SpecialApi specialApi;
	
    @Scheduled(cron = "0/5  * * * * ?")
    public void cron () {
    	// 只有程序正常运行才执行任务
		if(!AppStatusUtil.isRunning()) return;
		try {
			//查询待补充的质押信息
			NodeExample nodeExample = new NodeExample();
			nodeExample.createCriteria().andExternalIdNotEqualTo("");
			List<Node> nodeList = nodeMapper.selectByExample(null);
			
			Map<String, Optional<KeyBaseUserInfo>> cache = new HashMap<>();
			List<Node> updateNodeList = new ArrayList<>();

			// 查询节点版本号列表
			List<NodeVersion> versionList = specialApi.getNodeVersionList(platOnClient.getWeb3jWrapper().getWeb3j());
			Map<String,NodeVersion> versionMap = new HashMap<>();
			versionList.forEach(v->versionMap.put(v.getNodeId(),v));

			// 请求URL前缀
			String prefix = chainConfig.getKeyBase().concat(chainConfig.getKeyBaseApi());
			nodeList.forEach(node -> {

				// 更新keybase相关信息
				if( node.getExternalId().trim().length() == 16){
					Optional<KeyBaseUserInfo> optional = cache.computeIfAbsent(node.getExternalId(), key -> {
						String url = prefix.concat(key);
						try {
							KeyBaseUserInfo keyBaseUser = HttpUtil.get(url,KeyBaseUserInfo.class);
							return Optional.of(keyBaseUser);
						} catch (HttpRequestException e) {
							log.error("get keybase error:key={}",key,e);
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
				}

				// 更新节点版本号相关信息
				NodeVersion version = versionMap.get(node.getNodeId());
				if(version!=null&& !version.getBigVersion().equals(node.getBigVersion())){
					node.setBigVersion(version.getBigVersion());
					updateNodeList.add(node);
				}
			});
			
			if(!updateNodeList.isEmpty()) {
				stakeBusinessMapper.updateNodeForTask(updateNodeList);
			}
		} catch (Exception e) {
            log.error("on NodeUpdateTask error",e);
		}
    }   
}
