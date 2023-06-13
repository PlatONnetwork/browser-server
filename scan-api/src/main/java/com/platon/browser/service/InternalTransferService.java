package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.platon.browser.request.internaltransfer.QueryByAddressRequest;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.transaction.InternalTransferParam;
import com.platon.browser.service.elasticsearch.EsTransferTxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InternalTransferService {

    private final Logger logger = LoggerFactory.getLogger(InternalTransferService.class);

    @Resource
    private EsTransferTxRepository esTransferTxRepository;

    private static final String ERROR_TIPS = "获取内部转账错误。";

    public RespPage<InternalTransferParam> listByOwnerAddress(QueryByAddressRequest req) {
        RespPage<InternalTransferParam> result = new RespPage<>();

        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();

        ESResult<InternalTransferParam> items = new ESResult<>();
        constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", req.getAddress())).should(QueryBuilders.termQuery("to", req.getAddress())));
        constructor.setDesc("seq");
        constructor.setUnmappedType("long");
        constructor.setResult(new String[]{"hash", "from", "fromType", "to", "toType", "value", "bn"});
        try {
            items = esTransferTxRepository.search(constructor, InternalTransferParam.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return result;
        }
        List<InternalTransferParam> lists = items.getRsData();
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(items.getTotal());
        return result;
    }
}
