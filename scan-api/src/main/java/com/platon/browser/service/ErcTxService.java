package com.platon.browser.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.bean.Token1155HolderListBean;
import com.platon.browser.cache.TokenTransferRecordCacheDto;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.dao.custommapper.CustomToken1155HolderMapper;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.TokenTypeEnum;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.response.token.QueryTokenHolderListResp;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
import com.platon.browser.service.elasticsearch.AbstractEsRepository;
import com.platon.browser.service.elasticsearch.EsErc1155TxRepository;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.ConvertUtil;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合约内部转账交易记录
 *
 * @author AgentRJ
 * @create 2020-09-23 16:08
 */
@Service
@Slf4j
public class ErcTxService {

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private EsErc1155TxRepository esErc1155TxRepository;

    @Resource
    private I18nUtil i18n;

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private CustomToken1155HolderMapper customToken1155HolderMapper;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private TokenInventoryMapper token721InventoryMapper;

    @Resource
    private TokenMapper tokenMapper;

    /**
     * 默认精度
     */
    private static final Integer decimal = 6;

    public RespPage<QueryTokenTransferRecordListResp> token20TransferList(QueryTokenTransferRecordListReq req) {
        return this.getList(req, esErc20TxRepository, ErcTypeEnum.ERC20);
    }

    public RespPage<QueryTokenTransferRecordListResp> token721TransferList(QueryTokenTransferRecordListReq req) {
        return this.getList(req, esErc721TxRepository, ErcTypeEnum.ERC721);
    }

    public RespPage<QueryTokenTransferRecordListResp> token1155TransferList(QueryTokenTransferRecordListReq req) {
        return this.getList(req, esErc1155TxRepository, ErcTypeEnum.ERC1155);
    }

    private RespPage<QueryTokenTransferRecordListResp> getList(QueryTokenTransferRecordListReq req,
                                                               AbstractEsRepository repository,
                                                               ErcTypeEnum typeEnum) {
        RespPage<QueryTokenTransferRecordListResp> result = new RespPage<>();
        if (req.getPageNo() * req.getPageSize() > 20000) {
            result.setCode(500);
            result.setErrMsg("请求数据过大，请规范页面请求[PageNo()*PageSize()<=20000]");
            return result;
        }
        if (log.isDebugEnabled()) {
            log.debug("~ queryTokenRecordList, params: " + JSON.toJSONString(req));
        }
        // logic:
        // 1、合约内部交易列表中，数据存储于ES，列表的获取走ES获取
        // 2、所有查询直接走ES，不进行DB检索
        // 如果查询0地址，直接返回
        if (StrUtil.isNotBlank(req.getAddress()) && com.platon.browser.utils.AddressUtil.isAddrZero(req.getAddress())) {
            return result;
        }
        List<ErcTx> records;
        long totalCount = 0;
        long displayTotalCount = 0;
        if (StringUtils.isEmpty(req.getContract()) && StringUtils.isEmpty(req.getAddress()) && StringUtils.isEmpty(req.getTokenId())) {
            // 仅分页查询，直接走缓存
            TokenTransferRecordCacheDto tokenTransferRecordCacheDto = statisticCacheService.getTokenTransferCache(req.getPageNo(),
                                                                                                                  req.getPageSize(),
                                                                                                                  typeEnum);
            records = tokenTransferRecordCacheDto.getTransferRecordList();
            totalCount = tokenTransferRecordCacheDto.getPage().getTotalCount();
            displayTotalCount = tokenTransferRecordCacheDto.getPage().getTotalPages();
        } else {
            // construct of params
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            ESResult<ErcTx> queryResultFromES = new ESResult<>();
            // condition: txHash/contract/txFrom/transferTo
            if (StringUtils.isNotEmpty(req.getContract())) {
                constructor.must(new ESQueryBuilders().terms("contract", Collections.singletonList(req.getContract())));
            }
            if (StrUtil.isNotBlank(req.getTokenId())) {
                // 兼容旧数据
                constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("tokenId", req.getTokenId()))
                                                            .should(QueryBuilders.termQuery("value", req.getTokenId())));
            }
            if (StringUtils.isNotEmpty(req.getAddress())) {
                constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", req.getAddress()))
                                                            .should(QueryBuilders.termQuery("to", req.getAddress())));
            }
            if (StringUtils.isNotEmpty(req.getTxHash())) {
                constructor.must(new ESQueryBuilders().term("hash", req.getTxHash()));
            }
            // Set sort field
            constructor.setDesc("seq");
            // response filed to show.
            constructor.setResult(new String[]{"seq",
                                               "hash",
                                               "bn",
                                               "from",
                                               "contract",
                                               "to",
                                               "tokenId",
                                               "value",
                                               "decimal",
                                               "name",
                                               "symbol",
                                               "result",
                                               "bTime"});

            ESQueryBuilderConstructor count = new ESQueryBuilderConstructor();
            if (StringUtils.isNotEmpty(req.getContract())) {
                count.must(new ESQueryBuilders().terms("contract", Collections.singletonList(req.getContract())));
            }
            if (StringUtils.isNotEmpty(req.getAddress())) {
                count.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", req.getAddress()))
                                                      .should(QueryBuilders.termQuery("to", req.getAddress())));
            }

            try {
                queryResultFromES = repository.search(constructor, ErcTx.class, req.getPageNo(), req.getPageSize());
                ESResult<?> res = repository.Count(count);
                totalCount = res.getTotal();
                displayTotalCount = res.getTotal();
            } catch (Exception e) {
                log.error("检索代币交易列表失败", e);
                return result;
            }

            records = queryResultFromES.getRsData();
            if (null == records || records.size() == 0) {
                log.debug("未检索到有效数据，参数：" + JSON.toJSONString(req));
                return result;
            }
        }

        List<QueryTokenTransferRecordListResp> recordListResp = records.parallelStream()
                                                                       .filter(Objects::nonNull)
                                                                       .map(p -> this.toQueryTokenTransferRecordListResp(req.getAddress(),
                                                                                                                         p,
                                                                                                                         typeEnum))
                                                                       .collect(Collectors.toList());
        result.init(recordListResp, totalCount, displayTotalCount, totalCount / req.getPageSize() + 1);
        return result;
    }

    public AccountDownload exportToken20TransferList(String address, String contract, Long date, String local, String timeZone) {
        return this.exportTokenTransferList(address, contract, date, local, timeZone, esErc20TxRepository, null, TokenTypeEnum.ERC20);
    }

    public AccountDownload exportToken721TransferList(String address, String contract, Long date, String local, String timeZone, String tokenId) {
        return this.exportTokenTransferList(address, contract, date, local, timeZone, esErc721TxRepository, tokenId, TokenTypeEnum.ERC721);
    }

    public AccountDownload exportToken1155TransferList(String address, String contract, Long date, String local, String timeZone, String tokenId) {
        AccountDownload accountDownload = new AccountDownload();
        if (StringUtils.isBlank(address) && StringUtils.isBlank(contract)) {
            return accountDownload;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        log.info("导出地址交易列表数据起始日期：{},结束日期：{}", dateFormat.format(date), dateFormat.format(currentServerTime));
        // construct of params
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().range("bTime", new Date(date).getTime(), currentServerTime.getTime()));
        ESResult<ErcTx> queryResultFromES = new ESResult<>();
        // condition: txHash/contract/txFrom/transferTo
        if (StringUtils.isNotBlank(contract)) {
            constructor.must(new ESQueryBuilders().term("contract", contract));
        }
        if (StrUtil.isNotBlank(tokenId)) {
            constructor.must(new ESQueryBuilders().term("tokenId", tokenId));
        }
        if (StringUtils.isNotBlank(address)) {
            constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address))
                                                        .should(QueryBuilders.termQuery("to", address)));
        }
        // Set sort field
        constructor.setDesc("seq");
        // response filed to show.
        constructor.setResult(new String[]{"hash", "bTime", "from", "contract", "tokenId", "value", "to"});
        try {
            queryResultFromES = esErc1155TxRepository.search(constructor, ErcTx.class, 1, 30000);
        } catch (Exception e) {
            log.error("检索代币交易列表失败", e);
            return accountDownload;
        }
        List<Object[]> rows = new ArrayList<>();
        queryResultFromES.getRsData().stream().forEach(esTokenTransferRecord -> {
            Object[] row = {esTokenTransferRecord.getHash(),
                            DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(),
                                                      "0",
                                                      timeZone),
                            esTokenTransferRecord.getFrom(),
                            esTokenTransferRecord.getContract(),
                            esTokenTransferRecord.getTokenId(),
                            esTokenTransferRecord.getValue(),
                            esTokenTransferRecord.getTo()};
            rows.add(row);
        });
        String[] headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP,
                                                    local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                                    local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT,
                                                    local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TOKEN_ID,
                                                    local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE,
                                                    local),
                                        this.i18n.i(
                                                I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                                                local)};
        String fileName = "";
        if (StrUtil.isNotBlank(address)) {
            fileName = address;
        } else if (StrUtil.isNotBlank(contract)) {
            fileName = contract;
        }
        return this.downFileCommon.writeDate("InnerTransaction-" + fileName + "-" + date + ".CSV", rows, headers);
    }

    public AccountDownload exportTokenTransferList(String address,
                                                   String contract,
                                                   Long date,
                                                   String local,
                                                   String timeZone,
                                                   AbstractEsRepository repository,
                                                   String tokenId,
                                                   TokenTypeEnum tokenTypeEnum) {
        AccountDownload accountDownload = new AccountDownload();
        if (StringUtils.isBlank(address) && StringUtils.isBlank(contract)) {
            return accountDownload;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        log.info("导出地址交易列表数据起始日期：{},结束日期：{}", dateFormat.format(date), dateFormat.format(currentServerTime));

        // construct of params
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().range("bTime", new Date(date).getTime(), currentServerTime.getTime()));
        ESResult<ErcTx> queryResultFromES = new ESResult<>();
        // condition: txHash/contract/txFrom/transferTo
        if (StringUtils.isNotBlank(contract)) {
            constructor.must(new ESQueryBuilders().term("contract", contract));
        }
        if (StrUtil.isNotBlank(tokenId)) {
            constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("tokenId", tokenId))
                                                        .should(QueryBuilders.termQuery("value", tokenId)));
        }
        if (StringUtils.isNotBlank(address)) {
            constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address))
                                                        .should(QueryBuilders.termQuery("to", address)));
        }
        // Set sort field
        constructor.setDesc("seq");
        // response filed to show.
        constructor.setResult(new String[]{"seq",
                                           "hash",
                                           "bn",
                                           "from",
                                           "contract",
                                           "tokenId",
                                           "to",
                                           "value",
                                           "decimal",
                                           "name",
                                           "symbol",
                                           "result",
                                           "bTime"});
        try {
            queryResultFromES = repository.search(constructor, ErcTx.class, 1, 30000);
        } catch (Exception e) {
            log.error("检索代币交易列表失败", e);
            return accountDownload;
        }
        List<Object[]> rows = new ArrayList<>();
        queryResultFromES.getRsData().stream().forEach(esTokenTransferRecord -> {
            if (StringUtils.isNotBlank(address)) {
                boolean toIsAddress = address.equals(esTokenTransferRecord.getTo());
                String valueIn = toIsAddress ? esTokenTransferRecord.getValue() : "0";
                String valueOut = !toIsAddress ? esTokenTransferRecord.getValue() : "0";
                if (tokenTypeEnum.equals(TokenTypeEnum.ERC20)) {
                    valueIn = ConvertUtil.convertByFactor(new BigDecimal(valueIn), esTokenTransferRecord.getDecimal()).toString();
                    valueOut = ConvertUtil.convertByFactor(new BigDecimal(valueOut), esTokenTransferRecord.getDecimal()).toString();
                } else if (tokenTypeEnum.equals(TokenTypeEnum.ERC721) || tokenTypeEnum.equals(TokenTypeEnum.ERC1155)) {
                    if (address.equalsIgnoreCase(esTokenTransferRecord.getFrom())) {
                        if (ObjectUtil.isNull(esTokenTransferRecord.getTokenId())) {
                            valueOut = esTokenTransferRecord.getValue();
                        } else {
                            valueOut = esTokenTransferRecord.getTokenId();
                        }
                    } else if (address.equalsIgnoreCase(esTokenTransferRecord.getTo())) {
                        if (ObjectUtil.isNull(esTokenTransferRecord.getTokenId())) {
                            valueIn = esTokenTransferRecord.getValue();
                        } else {
                            valueIn = esTokenTransferRecord.getTokenId();
                        }
                    }
                }
                Object[] row = {esTokenTransferRecord.getHash(),
                                DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(),
                                                          "0",
                                                          timeZone),
                                esTokenTransferRecord.getFrom(),
                                esTokenTransferRecord.getTo(),
                                /** 数值von转换成lat，并保留十八位精确度 */
                                HexUtil.append(valueIn),
                                HexUtil.append(valueOut),
                                esTokenTransferRecord.getSymbol()};
                rows.add(row);
            } else if (StringUtils.isNotBlank(contract)) {
                String symbol = "";
                String value = "0";
                if (tokenTypeEnum.equals(TokenTypeEnum.ERC20)) {
                    symbol = esTokenTransferRecord.getSymbol();
                    value = ConvertUtil.convertByFactor(new BigDecimal(esTokenTransferRecord.getValue()), esTokenTransferRecord.getDecimal())
                                       .toString();
                }
                if (tokenTypeEnum.equals(TokenTypeEnum.ERC721) || tokenTypeEnum.equals(TokenTypeEnum.ERC1155)) {
                    symbol = StrUtil.format("{}({})", esTokenTransferRecord.getName(), esTokenTransferRecord.getSymbol());
                    if (ObjectUtil.isNull(esTokenTransferRecord.getTokenId())) {
                        value = esTokenTransferRecord.getValue();
                    } else {
                        value = esTokenTransferRecord.getTokenId();
                    }
                }
                Object[] row = {esTokenTransferRecord.getHash(),
                                DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(),
                                                          "0",
                                                          timeZone),
                                esTokenTransferRecord.getFrom(),
                                esTokenTransferRecord.getTo(),
                                /** 数值von转换成lat，并保留十八位精确度 */
                                HexUtil.append(value),
                                symbol};
                rows.add(row);
            }
        });
        String[] headers = {};
        if (StringUtils.isNotBlank(address)) {
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                                               local),
                                   this.i18n.i(
                                           I18nEnum.DOWNLOAD_CONTRACT_CSV_VALUE_IN,
                                           local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_VALUE_OUT, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local)};
        } else if (StringUtils.isNotBlank(contract)) {
            if (tokenTypeEnum.equals(TokenTypeEnum.ERC20)) {
                headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL,
                                                   local)};
            }
            if (tokenTypeEnum.equals(TokenTypeEnum.ERC721)) {
                headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TOKEN_ID,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TOKEN,
                                                   local)};
            }
            // 导出1155的
            if (tokenTypeEnum.equals(TokenTypeEnum.ERC1155)) {
                headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TOKEN_ID,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE,
                                                   local),
                                       this.i18n.i(
                                               I18nEnum.DOWNLOAD_CONTRACT_CSV_TOKEN,
                                               local)};
            }
        }
        String fileName = "";
        if (StrUtil.isNotBlank(address)) {
            fileName = address;
        } else if (StrUtil.isNotBlank(contract)) {
            fileName = contract;
        }
        return this.downFileCommon.writeDate("InnerTransaction-" + fileName + "-" + date + ".CSV", rows, headers);
    }

    public RespPage<QueryTokenHolderListResp> tokenHolderList(QueryTokenHolderListReq req) {
        if ("erc1155".equalsIgnoreCase(req.getErcType())) {
            return token1155HolderList(req);
        } else {
            return token20And721HolderList(req);
        }
    }

    public RespPage<QueryTokenHolderListResp> token20And721HolderList(QueryTokenHolderListReq req) {
        RespPage<QueryTokenHolderListResp> result = new RespPage<>();
        if (req.getPageNo() * req.getPageSize() > 50000) {
            result.setCode(500);
            result.setErrMsg("请求数据过大，请规范页面请求[PageNo()*PageSize()<=50000]");
            return result;
        }
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<CustomTokenHolder> ids = this.customTokenHolderMapper.selectListByParams(req.getContract(), null, null);
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        List<QueryTokenHolderListResp> respList = new ArrayList<>();
        TokenInventoryExample token721Example = new TokenInventoryExample();
        token721Example.createCriteria().andTokenAddressEqualTo(req.getContract());
        Page<TokenInventory> totalTokenInventory = token721InventoryMapper.selectByExample(token721Example);
        Map<String, Long> map = totalTokenInventory.getResult()
                                                   .stream()
                                                   .collect(Collectors.groupingBy(TokenInventory::getOwner, Collectors.counting()));
        ids.getResult().forEach(tokenHolder -> {
            QueryTokenHolderListResp resp = new QueryTokenHolderListResp();
            resp.setAddress(tokenHolder.getAddress());
            BigDecimal originBalance = getAddressBalance(tokenHolder);
            originBalance = (originBalance == null) ? BigDecimal.ZERO : originBalance;
            if (tokenHolder.getDecimal() != null) {
                //金额转换成对应的值
                BigDecimal balance = ConvertUtil.convertByFactor(originBalance, tokenHolder.getDecimal());
                resp.setBalance(balance);
                //erc20
                if (ErcTypeEnum.ERC20.getDesc().equalsIgnoreCase(tokenHolder.getType())) {

                    //计算总供应量
                    String originTotalSupply = tokenHolder.getTotalSupply();
                    if (StrUtil.isBlank(originTotalSupply) || Convert.toLong(originTotalSupply, 0L).compareTo(0L) <= 0) {
                        // 总供应量小于等于0，则占比设置为0%
                        resp.setPercent("0.0000%");
                    } else {
                        BigDecimal totalSupply = ConvertUtil.convertByFactor(new BigDecimal(originTotalSupply), tokenHolder.getDecimal());
                        // 总供应量大于0, 使用实际的余额除以总供应量
                        resp.setPercent(balance.divide(totalSupply, decimal, RoundingMode.HALF_UP)
                                               .multiply(BigDecimal.valueOf(100))
                                               .setScale(decimal, RoundingMode.HALF_UP)
                                               .stripTrailingZeros()
                                               .toPlainString() + "%");
                    }
                } else if (ErcTypeEnum.ERC721.getDesc().equalsIgnoreCase(tokenHolder.getType())) {
                    //erc721
                    int holderNum = map.get(tokenHolder.getAddress()).intValue();
                    long total = totalTokenInventory.size();
                    String percent = new BigDecimal(holderNum).divide(new BigDecimal(total), decimal, RoundingMode.HALF_UP)
                                                              .multiply(BigDecimal.valueOf(100))
                                                              .setScale(decimal, RoundingMode.HALF_UP)
                                                              .stripTrailingZeros()
                                                              .toPlainString() + "%";
                    resp.setPercent(percent);
                }
            } else {
                resp.setBalance(originBalance);
                resp.setPercent("0.0000%");
            }
            respList.add(resp);
        });
        Token token = tokenMapper.selectByPrimaryKey(req.getContract());
        if (ErcTypeEnum.ERC721.getDesc().equalsIgnoreCase(token.getType())) {
            respList.sort((v1, v2) -> {
                BigDecimal value1 = new BigDecimal(StrUtil.removeAll(v1.getPercent(), '%'));
                BigDecimal value2 = new BigDecimal(StrUtil.removeAll(v2.getPercent(), '%'));
                return value2.subtract(value1).compareTo(BigDecimal.ZERO);
            });
        }
        result.init(ids, respList);
        return result;
    }

    public RespPage<QueryTokenHolderListResp> token1155HolderList(QueryTokenHolderListReq req) {
        RespPage<QueryTokenHolderListResp> result = new RespPage<>();
        if (req.getPageNo() * req.getPageSize() > 50000) {
            result.setCode(500);
            result.setErrMsg("请求数据过大，请规范页面请求[PageNo()*PageSize()<=50000]");
            return result;
        }
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<Token1155HolderListBean> token1155HolderList = customToken1155HolderMapper.findToken1155HolderList(req.getContract());
        List<QueryTokenHolderListResp> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(token1155HolderList)) {
            for (Token1155HolderListBean token1155HolderListBean : token1155HolderList) {
                QueryTokenHolderListResp queryTokenHolderListResp = new QueryTokenHolderListResp();
                queryTokenHolderListResp.setAddress(token1155HolderListBean.getAddress());
                queryTokenHolderListResp.setPercent(token1155HolderListBean.getPercent()
                                                                           .multiply(BigDecimal.valueOf(100))
                                                                           .setScale(decimal, RoundingMode.HALF_UP)
                                                                           .stripTrailingZeros()
                                                                           .toPlainString() + "%");
                queryTokenHolderListResp.setBalance(new BigDecimal(token1155HolderListBean.getBalance()));
                list.add(queryTokenHolderListResp);
            }
            list.sort((v1, v2) -> {
                BigDecimal value1 = new BigDecimal(StrUtil.removeAll(v1.getPercent(), '%'));
                BigDecimal value2 = new BigDecimal(StrUtil.removeAll(v2.getPercent(), '%'));
                return value2.subtract(value1).compareTo(BigDecimal.ZERO);
            });
            result.init(token1155HolderList, list);
        }
        return result;
    }

    public RespPage<QueryHolderTokenListResp> holderTokenList(@Valid QueryHolderTokenListReq req) {
        RespPage<QueryHolderTokenListResp> result = new RespPage<>();
        if (req.getPageNo() * req.getPageSize() > 50000) {
            result.setCode(500);
            result.setErrMsg("请求数据过大，请规范页面请求[PageNo()*PageSize()<=50000]");
            return result;
        }
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<CustomTokenHolder> ids = new Page<>();
        if ("erc20".equalsIgnoreCase(req.getType())) {
            ids = this.customTokenHolderMapper.selectListByParams(null, req.getAddress(), req.getType());
        }
        if ("erc721".equalsIgnoreCase(req.getType())) {
            ids = customTokenHolderMapper.selectListByERC721(null, req.getAddress());
        }
        if ("erc1155".equalsIgnoreCase(req.getType())) {
            ids = customToken1155HolderMapper.selectListByERC1155(null, req.getAddress());
        }
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        List<QueryHolderTokenListResp> listResps = new ArrayList<>();
        List<String> contractAddressList = new ArrayList<>();
        Map<String, List<QueryHolderTokenListResp>> respMap = new HashMap<>();
        ids.stream().forEach(tokenHolder -> {
            String contractAddress = tokenHolder.getTokenAddress();
            QueryHolderTokenListResp queryHolderTokenListResp = new QueryHolderTokenListResp();
            BeanUtils.copyProperties(tokenHolder, queryHolderTokenListResp);
            queryHolderTokenListResp.setName(StrUtil.emptyIfNull(tokenHolder.getName()));
            queryHolderTokenListResp.setContract(contractAddress);
            BigDecimal balance = this.getAddressBalance(tokenHolder);
            //金额转换成对应的值
            if (null != balance) {
                BigDecimal actualTransferValue;
                if (ObjectUtil.isNotNull(tokenHolder.getDecimal())) {
                    actualTransferValue = ConvertUtil.convertByFactor(balance, tokenHolder.getDecimal());
                } else {
                    actualTransferValue = balance;
                }
                queryHolderTokenListResp.setBalance(actualTransferValue);
            } else {
                queryHolderTokenListResp.setBalance(BigDecimal.ZERO);
            }
            listResps.add(queryHolderTokenListResp);
            contractAddressList.add(contractAddress);

            List<QueryHolderTokenListResp> respList = respMap.computeIfAbsent(contractAddress, k -> new ArrayList<>());
            respList.add(queryHolderTokenListResp);
        });

        // 批量查询地址表,并设置响应结果中的合约状态
        AddressExample condition = new AddressExample();
        condition.createCriteria().andAddressIn(contractAddressList);
        List<Address> contractList = addressMapper.selectByExample(condition);
        if (!contractList.isEmpty()) {
            contractList.forEach(e -> {
                int isDestroy = StringUtils.isBlank(e.getContractDestroyHash()) ? 0 : 1;
                List<QueryHolderTokenListResp> respList = respMap.get(e.getAddress());
                if (respList != null) {
                    respList.forEach(r -> r.setIsContractDestroy(isDestroy));
                }
            });
        }
        listResps.sort(Comparator.comparing(QueryHolderTokenListResp::getIsContractDestroy)
                                 .thenComparing(QueryHolderTokenListResp::getName)
                                 .thenComparing(QueryHolderTokenListResp::getTxCount)
                                 .thenComparing((o1, o2) -> cn.hutool.core.date.DateUtil.compare(o2.getCreateTime(), o1.getCreateTime())));
        result.init(ids, listResps);
        return result;
    }

    public AccountDownload exportTokenHolderList(String contract, String local, String timeZone, String ercType) {
        if (ercType.equalsIgnoreCase(TokenTypeEnum.ERC1155.getType())) {
            return exportErc1155TokenHolderList(contract, local, timeZone);
        } else {
            return exportErc20And721TokenHolderList(contract, local, timeZone);
        }
    }

    public AccountDownload exportErc1155TokenHolderList(String contract, String local, String timeZone) {
        PageHelper.startPage(1, 30000);
        Page<Token1155HolderListBean> token1155HolderList = customToken1155HolderMapper.findToken1155HolderList(contract);
        List<QueryTokenHolderListResp> list = new ArrayList<>();
        List<Object[]> rows = new ArrayList<>();
        if (CollUtil.isNotEmpty(token1155HolderList)) {
            for (Token1155HolderListBean token1155HolderListBean : token1155HolderList) {
                Object[] row = {token1155HolderListBean.getAddress(),
                                HexUtil.append(token1155HolderListBean.getBalance()),
                                token1155HolderListBean.getPercent()
                                                       .multiply(BigDecimal.valueOf(100))
                                                       .setScale(decimal, RoundingMode.HALF_UP)
                                                       .stripTrailingZeros()
                                                       .toPlainString() + "%"};
                rows.add(row);
            }
        }

        String[] headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE,
                                                    local),
                                        this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
        return this.downFileCommon.writeDate("TokenHolder-" + contract + "-" + System.currentTimeMillis() + ".CSV", rows, headers);
    }

    public AccountDownload exportErc20And721TokenHolderList(String contract, String local, String timeZone) {
        PageHelper.startPage(1, 30000);
        Page<CustomTokenHolder> rs = this.customTokenHolderMapper.selectListByParams(contract, null, null);
        List<Object[]> rows = new ArrayList<>();
        TokenInventoryExample example = new TokenInventoryExample();
        example.createCriteria().andTokenAddressEqualTo(contract);
        Page<TokenInventory> totalTokenInventory = token721InventoryMapper.selectByExample(example);
        Map<String, Long> maps = totalTokenInventory.getResult()
                                                    .stream()
                                                    .collect(Collectors.groupingBy(TokenInventory::getOwner, Collectors.counting()));
        String[] headers = new String[0];
        for (CustomTokenHolder customTokenHolder : rs) {
            BigDecimal balance = this.getAddressBalance(customTokenHolder);
            BigDecimal originBalance = ConvertUtil.convertByFactor(balance, customTokenHolder.getDecimal());
            String percent = "";
            if (ErcTypeEnum.ERC20.getDesc().equalsIgnoreCase(customTokenHolder.getType())) {
                //计算总供应量
                String originTotalSupply = customTokenHolder.getTotalSupply();
                if (StrUtil.isBlank(originTotalSupply) || Convert.toLong(originTotalSupply, 0L).compareTo(0L) <= 0) {
                    // 总供应量小于等于0，则占比设置为0%
                    percent = "0.0000%";
                } else {
                    BigDecimal totalSupply = ConvertUtil.convertByFactor(new BigDecimal(originTotalSupply), customTokenHolder.getDecimal());
                    // 总供应量大于0, 使用实际的余额除以总供应量
                    percent = originBalance.divide(totalSupply, decimal, RoundingMode.HALF_UP)
                                           .multiply(BigDecimal.valueOf(100))
                                           .stripTrailingZeros()
                                           .toPlainString() + "%";
                }
                headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
            } else {
                //erc721 erc1155
                int holderNum = maps.get(customTokenHolder.getAddress()).intValue();
                long total = totalTokenInventory.size();
                percent = new BigDecimal(holderNum).divide(new BigDecimal(total), decimal, RoundingMode.HALF_UP)
                                                   .multiply(BigDecimal.valueOf(100))
                                                   .stripTrailingZeros()
                                                   .toPlainString() + "%";
                headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_AMOUNT,
                                                   local),
                                       this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
            }
            Object[] row = {customTokenHolder.getAddress(),
                            HexUtil.append(ConvertUtil.convertByFactor(balance, customTokenHolder.getDecimal()).toString()),
                            percent};
            rows.add(row);
        }
        if (CollUtil.isEmpty(rows)) {
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
            Object[] row = {"", "", ""};
            rows.add(row);
        }
        return this.downFileCommon.writeDate("TokenHolder-" + contract + "-" + System.currentTimeMillis() + ".CSV", rows, headers);
    }

    public AccountDownload exportHolderTokenList(String address, String local, String timeZone, String type) {

        PageHelper.startPage(1, 30000);

        String[] tokenType = {"erc20", "erc721", "erc1155"};
        if (!ArrayUtil.contains(tokenType, type)) {
            type = null;
        }
        List<Object[]> rows = new ArrayList<>();
        String[] headers = {};
        if (ErcTypeEnum.ERC721.getDesc().equalsIgnoreCase(type)) {
            Page<CustomTokenHolder> rs = this.customTokenHolderMapper.findErc721TokenHolder(null, address, type);
            rs.forEach(customTokenHolder -> {
                Object[] row = {customTokenHolder.getName(),
                                customTokenHolder.getSymbol(),
                                customTokenHolder.getTokenId(),
                                customTokenHolder.getTxCount(),
                                customTokenHolder.getTokenAddress()};
                rows.add(row);
            });
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_NAME, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN_ID,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TXCOUNT,
                                               local),
                                   this.i18n.i(
                                           I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT,
                                           local)};
        } else if (ErcTypeEnum.ERC1155.getDesc().equalsIgnoreCase(type)) {
            Page<CustomTokenHolder> rs = customToken1155HolderMapper.findErc1155TokenHolder(address);
            rs.forEach(customTokenHolder -> {
                Object[] row = {StrUtil.emptyIfNull(customTokenHolder.getName()),
                                customTokenHolder.getTokenAddress(),
                                customTokenHolder.getTokenId(),
                                customTokenHolder.getBalance(),
                                customTokenHolder.getTxCount(),
                                customTokenHolder.getDecimal()};
                rows.add(row);
            });
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_NAME, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN_ID,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TXCOUNT,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_DECIMALS,
                                               local)};

        } else {
            Page<CustomTokenHolder> rs = this.customTokenHolderMapper.selectListByParams(null, address, type);
            rs.forEach(customTokenHolder -> {
                BigDecimal balance = this.getAddressBalance(customTokenHolder);
                Object[] row = {customTokenHolder.getName(),
                                customTokenHolder.getSymbol(),
                                HexUtil.append(ConvertUtil.convertByFactor(balance, customTokenHolder.getDecimal())
                                                          .toString()),
                                customTokenHolder.getDecimal(),
                                customTokenHolder.getTxCount(),
                                customTokenHolder.getTokenAddress()};
                rows.add(row);
            });
            headers = new String[]{this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_NAME, local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_DECIMALS,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_TXCOUNT,
                                               local),
                                   this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT,
                                               local)};
        }
        return this.downFileCommon.writeDate("HolderToken-" + address + "-" + System.currentTimeMillis() + ".CSV", rows, headers);
    }

    public QueryTokenTransferRecordListResp toQueryTokenTransferRecordListResp(String address, ErcTx record, ErcTypeEnum typeEnum) {
        QueryTokenTransferRecordListResp resp = QueryTokenTransferRecordListResp.builder()
                                                                                .seq(record.getSeq())
                                                                                .txHash(record.getHash())
                                                                                .blockNumber(record.getBn())
                                                                                .txFrom(record.getFrom())
                                                                                .contract(record.getContract())
                                                                                .transferTo(record.getTo())
                                                                                .name(record.getName())
                                                                                .decimal(record.getDecimal())
                                                                                .symbol(record.getSymbol())
                                                                                .tokenId(record.getTokenId())
                                                                                .value(new BigDecimal(record.getValue()))
                                                                                .blockTimestamp(record.getBTime())
                                                                                .systemTimestamp(System.currentTimeMillis())
                                                                                .value(null == record.getValue() ? BigDecimal.ZERO : new BigDecimal(
                                                                                        record.getValue()))
                                                                                .fromType(record.getFromType())
                                                                                .toType(record.getToType())
                                                                                .build();
        // Processing accuracy calculation.
        if (null != record.getValue()) {
            BigDecimal transferValue = new BigDecimal(record.getValue());
            if (record.getDecimal() != null && record.getDecimal() > 0) {
                transferValue = ConvertUtil.convertByFactor(transferValue, record.getDecimal());
            }
            resp.setTransferValue(transferValue);
        } else {
            resp.setTransferValue(BigDecimal.ZERO);
        }
        // 兼容旧数据
        if (StrUtil.isBlank(resp.getTokenId()) && typeEnum.equals(ErcTypeEnum.ERC721)) {
            resp.setTokenId(resp.getTransferValue().toPlainString());
        }
        // input or out
        if (null != address && address.equals(record.getFrom())) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.OUT.val());
        } else {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.INPUT.val());
        }
        if (null == address) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.NONE.val());
        }
        return resp;
    }

    private BigDecimal getAddressBalance(CustomTokenHolder tokenHolder) {
        //暂时由后台统计余额
        return new BigDecimal(tokenHolder.getBalance());
    }

}
