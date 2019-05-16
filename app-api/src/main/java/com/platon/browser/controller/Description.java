package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionVoteReq;
import com.platon.browser.dto.transaction.VoteInfo;
import com.platon.browser.dto.transaction.VoteSummary;
import com.platon.browser.dto.transaction.VoteTransaction;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.transaction.CandidateTicketCountReq;
import com.platon.browser.req.transaction.TicketCountByTxHashReq;
import com.platon.browser.req.transaction.VoteSummaryReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.ApiService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:39
 */

public class Description {
    /**
     * @api 接口请求头设置说明
     * @apiGroup request
     * @apiDescription 接口请求头设置说明
     * @apiParamExample :
     *
     * 本接口文档中定义的接口中的通用参数统一使用请求头传输，例如每个接口都需要传链ID，例如访问交易列表接口：
     * curl -X POST \
     *   http://localhost:28060/browser-server/transaction/list \
     *   -H 'Accept: application/json' \
            -H 'Content-Type: application/json' \
            -H 'x-aton-cid: 2055555' \ # 链ID
            -H 'content-length: 136' \
            -d '{
                "beginSequence":1,
                "listSize":30,
                "address":"0x493301712671ada506ba6ca7891f436d29185821",
                "cid":"20d 3"
            }'
     *
     */
}
