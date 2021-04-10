package com.platon.browser.service.elasticsearch.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.param.sync.AddressTokenQtyUpdateParam;
import com.platon.browser.param.sync.Erc20TokenAddressRelTxCountUpdateParam;
import com.platon.browser.param.sync.Erc20TokenTxCountUpdateParam;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TokenTxSummary {

    // token交易数信息列表：<合约地址,交易信息详情>
    private Map<String, TokenTxCount> contractTxCountMap = new HashMap<>();

    // 所有token交易数量
    private Long addressTxCount = 0L;

    // token交易数按地址分组：<地址,交易数>
    private Map<String, Long> addressTxCountMap = new HashMap<>();

    public void calculate() {
        for (Map.Entry<String, TokenTxCount> e : contractTxCountMap.entrySet()) {
            String tokenAddress = e.getKey();
            TokenTxCount ttc = e.getValue();
            addressTxCount += ttc.getTokenTxCount();
            for (Map.Entry<String, Long> entry : ttc.getTokenTxCountMap().entrySet()) {
                String address = entry.getKey();
                Long count = entry.getValue();
                Long sum = addressTxCountMap.get(address);
                if (sum == null)
                    sum = 0L;
                sum += count;
                addressTxCountMap.put(address, sum);
            }
        }
    }

    /**
     * 获取address表token_qty字段更新参数列表
     *
     * @return
     */
    public List<AddressTokenQtyUpdateParam> addressTokenQtyUpdateParamList() {
        List<AddressTokenQtyUpdateParam> params = new ArrayList<>();
        addressTxCountMap.forEach((address, count) -> {
            AddressTokenQtyUpdateParam param = new AddressTokenQtyUpdateParam();
            param.setAddress(address);
            param.setTokenQty(count.intValue());
            params.add(param);
        });
        return params;
    }

    /**
     * 获取erc20_token_address_rel表tx_count字段更新参数列表
     *
     * @return
     */
    public List<Erc20TokenAddressRelTxCountUpdateParam> erc20TokenAddressRelTxCountUpdateParamList() {
        List<Erc20TokenAddressRelTxCountUpdateParam> params = new ArrayList<>();
        contractTxCountMap.forEach((contract, ttc) -> {
            ttc.getTokenTxCountMap().forEach((address, count) -> {
                Erc20TokenAddressRelTxCountUpdateParam param = new Erc20TokenAddressRelTxCountUpdateParam();
                param.setContract(contract);
                param.setAddress(address);
                param.setTxCount(count.intValue());
                params.add(param);
            });
        });
        return params;
    }

    /**
     * 获取erc20_token表tx_count字段更新参数列表
     *
     * @return
     */
    @JsonIgnore
    public List<Erc20TokenTxCountUpdateParam> erc20TokenTxCountUpdateParamList() {
        List<Erc20TokenTxCountUpdateParam> params = new ArrayList<>();
        contractTxCountMap.forEach((contract, ttc) -> {
            Erc20TokenTxCountUpdateParam param = new Erc20TokenTxCountUpdateParam();
            param.setAddress(contract);
            param.setTxCount(ttc.getTokenTxCount().intValue());
            params.add(param);
        });
        return params;
    }

}
