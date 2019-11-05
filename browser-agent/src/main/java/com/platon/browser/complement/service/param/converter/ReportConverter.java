package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.slash.Report;
import com.platon.browser.param.ReportParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ReportConverter extends BusinessParamConverter<Report> {

    @Override
    public Report convert(CollectionTransaction tx) {
        // 修改质押信息
        ReportParam txParam = tx.getTxParam(ReportParam.class);
        Report businessParam= Report.builder()
                .nodeId(txParam.getVerify())
                .slashData(txParam.getData())
                .time(tx.getTime())
                .bNum(BigInteger.valueOf(tx.getNum()))
                .txHash(tx.getHash())
                .build();
        BeanUtils.copyProperties(txParam,businessParam);
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getVerify()),txParam.getNodeName());
        return businessParam;
    }
}
