package com.platon.browser.analyzer.statistic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomAddress;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ContractDescEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StatisticsAddressAnalyzer {

    @Resource
    private NewAddressCache newAddressCache;

    @Resource
    private AddressMapper addressMapper;


    public void analyze(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})",
                block.getNum(),
                event.getBlock().getDtoTransactions().size(),
                epochMessage.getConsensusEpochRound(),
                epochMessage.getSettleEpochRound(),
                epochMessage.getIssueEpochRound());

        if (block.getNum() == 0) {
            List<Address> innerAddressList = new ArrayList<>();
            for (ContractDescEnum contractDescEnum : ContractDescEnum.values()) {
                Address address = CustomAddress.createNewAccountAddress(contractDescEnum.getAddress());
                address.setType(AddressTypeEnum.INNER_CONTRACT.getCode());
                innerAddressList.add(address);
            }
            addressMapper.batchInsertNewly(innerAddressList);
            log.debug("初始化内置地址入库成功:{}", JSONUtil.toJsonStr(innerAddressList));
            return;
        } else {
            /*//获取本区块所有可能是新的地址
            Set<String> pendingNewAddressSet = newAddressCache.listPendingAddressInBlockCtx();

            // 从数据库中查询出与缓存中对应的地址信息
            //pending_new的地址，肯定是EOA地址，不需要load blob字段
            Set<String> exists = addressMapper.filterExists(pendingNewAddressSet);
            pendingNewAddressSet.removeAll(exists);
            newAddressCache.identifyNewAddressInBlockCtx(pendingNewAddressSet);*/

            //增加新地址到address表，放在最前面
            List<Address> newAddrList = newAddressCache.listNewAddressInBlockCtx();
            if (CollUtil.isNotEmpty(newAddrList)) {
                addressMapper.batchInsertNewly(newAddrList);
                log.debug("新增地址成功{}", JSONUtil.toJsonStr(newAddrList));
            }
            //修改地址类型
            List<Address> resetTypeAddrList = newAddressCache.listResetTypeAddressInBlockCtx();
            if (CollUtil.isNotEmpty(resetTypeAddrList)) {
                addressMapper.batchResetType(resetTypeAddrList);
                log.debug("修改地址类型成功{}", JSONUtil.toJsonStr(resetTypeAddrList));
            }

            //更新领取委托奖励，修改的放中间
            List<Address> rewardClaimAddrList = newAddressCache.listRewardClaimAddressInBlockCtx();
            if (CollUtil.isNotEmpty(rewardClaimAddrList)) {
                addressMapper.batchUpdateReward(rewardClaimAddrList);
            }

            //更新销毁的地址，放在最后面
            List<Address> suicidedAddrList = newAddressCache.listSuicidedAddressInBlockCtx();
            if (CollUtil.isNotEmpty(suicidedAddrList)) {
                addressMapper.batchUpdateSuicided(suicidedAddrList);
            }
        }
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
