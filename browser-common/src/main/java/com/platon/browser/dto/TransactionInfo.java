package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.json.CreateValidatorDto;
import com.platon.browser.enums.TxTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.EthBlock;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description:
 */
@Data
public class TransactionInfo extends TransactionWithBLOBs {

    public TransactionInfo(EthBlock.TransactionResult initData){
        BeanUtils.copyProperties(initData,this);
    }

    /**
     * 根据类型获取交易参数信息对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getTxJson(T clazz){
        TxTypeEnum typeEnum = TxTypeEnum.valueOf(this.getTxType());
        switch (typeEnum){
            case CREATEVALIDATOR:
                // 质押交易
                return (T)JSON.parseObject(this.getTxInfo(), CreateValidatorDto.class);
            case DELEGATE:
                // 委托交易
                break;
            default:
        }
        return null;
    }
}
