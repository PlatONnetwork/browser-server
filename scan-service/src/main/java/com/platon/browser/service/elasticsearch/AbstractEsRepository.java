package com.platon.browser.service.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.EsIndexConfig;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.bean.ESSortDto;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: elasticsearch通用操作
 */
@Slf4j
public abstract class AbstractEsRepository {

    private final static String CLASSPATH_ES_TPL_DIR = "/estpl/";

    private final static String TPL_FILE_SUFFIX = ".estpl.yml";

    private static final String CONSUME_TIME_TIPS = "处理耗时:{} ms";

    @Resource(name = "restHighLevelClient")
    protected RestHighLevelClient client;

    @Resource
    protected EsIndexConfig config;

    @Resource
    private SpringUtils springUtils;

    public abstract String getIndexName();

    public abstract String getTemplateFileName();

    private String getTplJson() {
        String tplName = getTemplateFileName() + TPL_FILE_SUFFIX;
        log.info("template file:{}", tplName);
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(AbstractEsRepository.class.getResourceAsStream(CLASSPATH_ES_TPL_DIR + tplName));
            String json = JSON.toJSONString(result, true);
            log.info("template json:{}", json);
            return json;
        } catch (Exception e) {
            log.warn("解析文件{}出错：{}", tplName, e.getMessage());
            return "";
        }
    }

    @PostConstruct
    public void init() {
        try {
            String templateName = getIndexName() + "_tpl";
            String templateJson = getTplJson();
            putIndexTemplate(templateName, templateJson);
        } catch (IOException e) {
            log.error("Automatic detection of internal transaction index template failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 索引模板发布
     */
    public void putIndexTemplate(String indexTemplateName, String templateJson) throws IOException {
        if (existsTemplate(indexTemplateName)) {
            log.warn("{" + indexTemplateName + "} index template already exist.");
            return;
        }
        if (StringUtils.isBlank(templateJson)) {
            log.warn("template content empty.");
            return;
        }
        long startTime = System.currentTimeMillis();
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(indexTemplateName);
        request.source(templateJson, XContentType.JSON);

        AcknowledgedResponse response = client.indices().putTemplate(request, RequestOptions.DEFAULT);
        if (log.isDebugEnabled()) {
            log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
            log.debug("createIndexWithMapping:{}", JSON.toJSONString(response, true));
        }
    }

    public boolean existsTemplate(String templateName) throws IOException {
        long startTime = System.currentTimeMillis();
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(templateName);
        boolean response = client.indices().existsTemplate(request, RequestOptions.DEFAULT);
        if (log.isDebugEnabled()) {
            log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
            log.debug("existsTemplate: {}", response);
        }
        return response;
    }

    /**
     * 创建索引
     *
     * @throws IOException
     */
    public void createIndex(Map<String, ?> mapping) throws IOException {
        long startTime = System.currentTimeMillis();
        CreateIndexRequest request = new CreateIndexRequest(getIndexName());
        if (mapping != null && mapping.size() > 0) {
            request.mapping(mapping);
        }
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
        log.debug("createIndex:{}", JSON.toJSONString(response, true));
    }

    /**
     * 创建索引
     *
     * @throws IOException
     */
    public void createIndex(Map<String, ?> setting, Map<String, ?> mapping) throws IOException {
        long startTime = System.currentTimeMillis();
        CreateIndexRequest request = new CreateIndexRequest(getIndexName());
        if (mapping != null && mapping.size() > 0) {
            request.mapping(mapping);
        }
        if (setting != null && setting.size() > 0) {
            request.settings(setting);
        }
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
        log.debug("createIndex:{}", JSON.toJSONString(response, true));
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    public void deleteIndex() throws IOException {
        long startTime = System.currentTimeMillis();

        DeleteIndexRequest request = new DeleteIndexRequest(getIndexName());
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("deleteIndex:{}", JSON.toJSONString(response, true));
    }

    /**
     * 判断索引是否存在
     *
     * @return
     * @throws IOException
     */
    public boolean existsIndex() throws IOException {
        long startTime = System.currentTimeMillis();

        GetIndexRequest request = new GetIndexRequest(getIndexName());
        boolean response = client.indices().exists(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("existsIndex:{}", response);
        return response;
    }

    /**
     * 初始化index
     *
     * @return
     * @throws IOException
     */
    public boolean initIndex() throws IOException {
        if (this.existsIndex()) {
            this.deleteIndex();
        }
        Map<String, Object> setting = new HashMap(3);
        // 查询的返回数量，默认是10000
        setting.put("max_result_window", 2000000000);
        // 主碎片的数量
        setting.put("number_of_shards", 5);
        // 副本每个主碎片的数量
        setting.put("number_of_replicas", 1);
        this.createIndex(setting, null);
        return true;
    }

    /**
     * 增加记录
     *
     * @throws IOException
     */
    public <T> void add(String id, T doc) throws IOException {
        long startTime = System.currentTimeMillis();

        IndexRequest request = new IndexRequest(getIndexName());
        request.id(id).source(JSON.toJSONString(doc), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("add:{}", JSON.toJSONString(response, true));
    }

    /**
     * 判断记录是都存在
     *
     * @return
     * @throws IOException
     */
    public boolean exists(String id) throws IOException {
        long startTime = System.currentTimeMillis();

        GetRequest request = new GetRequest(getIndexName(), id);
        request.fetchSourceContext(new FetchSourceContext(false)).storedFields("_none_");
        boolean response = client.exists(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("add:{}", JSON.toJSONString(response, true));
        return response;
    }

    /**
     * 获取记录信息
     *
     * @param id
     * @throws IOException
     */
    public <T> T get(String id, Class<T> clazz) throws IOException {
        long startTime = System.currentTimeMillis();

        GetRequest request = new GetRequest(getIndexName(), id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        log.debug("get:{}", JSON.toJSONString(response, true));
        String res = response.getSourceAsString();

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        return JSON.parseObject(res, clazz);
    }

    /**
     * 更新记录信息
     *
     * @throws IOException
     */
    public <T> void update(String id, T block) throws IOException {
        long startTime = System.currentTimeMillis();

        UpdateRequest request = new UpdateRequest(getIndexName(), id);
        request.doc(JSON.toJSONString(block), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("update:{}", JSON.toJSONString(response, true));
    }

    /**
     * 删除记录
     *
     * @param id
     * @throws IOException
     */
    public void delete(String id) throws IOException {
        long startTime = System.currentTimeMillis();

        DeleteRequest request = new DeleteRequest(getIndexName(), id);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("delete:{}", JSON.toJSONString(response, true));
    }

    /**
     * 搜索
     *
     * @throws IOException
     */
    public <T> ESResult<T> search(Map<String, Object> filter, Class<T> clazz, List<ESSortDto> esSortDtos, int pageNo,
                                  int pageSize) throws IOException {
        long startTime = System.currentTimeMillis();

        if (pageNo <= 0) {
            pageNo = 1;
        }
        SearchRequest searchRequest = new SearchRequest(getIndexName());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((pageNo - 1) * pageSize).size(pageSize);
        if (filter != null) {
            filter.forEach((term, value) -> searchSourceBuilder.query(QueryBuilders.matchQuery(term, value)));
        }
        if (esSortDtos != null) {
            esSortDtos.forEach(esSortDto -> searchSourceBuilder.sort(esSortDto.getSortName(), esSortDto.getSortOrder()));
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        ESResult<T> esResult = new ESResult<>();
        SearchHits hits = response.getHits();
        esResult.setTotal(hits.getTotalHits().value);
        List<T> list = new ArrayList<>();
        Arrays.asList(hits.getHits()).forEach(hit -> list.add(JSON.parseObject(hit.getSourceAsString(), clazz)));
        esResult.setRsData(list);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        return esResult;
    }


    /**
     * 搜索
     *
     * @throws IOException
     */
    public <T> ESResult<T> search(ESQueryBuilderConstructor constructor, Class<T> clazz, int pageNo,
                                  int pageSize) throws IOException {
        long startTime = System.currentTimeMillis();

        if (pageNo <= 0) {
            pageNo = 1;
        }
        SearchRequest searchRequest = new SearchRequest(getIndexName());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //排序
        if (StringUtils.isNotEmpty(constructor.getAsc())) {
            FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(constructor.getAsc());
            fieldSortBuilder.order(SortOrder.ASC);
            if (StringUtils.isNotEmpty(constructor.getUnmappedType())) {
                fieldSortBuilder.unmappedType(constructor.getUnmappedType());
            }
            searchSourceBuilder.sort(fieldSortBuilder);
        }
        if (StringUtils.isNotEmpty(constructor.getDesc())) {
            FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(constructor.getDesc());
            fieldSortBuilder.order(SortOrder.DESC);
            if (StringUtils.isNotEmpty(constructor.getUnmappedType())) {
                fieldSortBuilder.unmappedType(constructor.getUnmappedType());
            }
            searchSourceBuilder.sort(fieldSortBuilder);
        }
        //设置查询体
        searchSourceBuilder.query(constructor.listBuilders());
        searchSourceBuilder.from((pageNo - 1) * pageSize).size(pageSize);
        if (constructor.getResult() != null) {
            searchSourceBuilder.fetchSource(constructor.getResult(), null);
        }
        // 设置SearchSourceBuilder查询属性
        searchRequest.source(searchSourceBuilder);
        log.debug("get rs" + searchSourceBuilder.toString());
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        ESResult<T> esResult = new ESResult<>();
        SearchHits hits = response.getHits();
        esResult.setTotal(hits.getTotalHits().value);
        List<T> list = new ArrayList<>();
        Arrays.asList(hits.getHits()).forEach(hit -> list.add(JSON.parseObject(hit.getSourceAsString(), clazz)));
        esResult.setRsData(list);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        return esResult;
    }

    /**
     * 查询总数
     *
     * @throws IOException
     */
    public ESResult<?> Count(ESQueryBuilderConstructor constructor) throws IOException {
        long startTime = System.currentTimeMillis();

        CountRequest searchRequest = new CountRequest(getIndexName());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询体
        searchSourceBuilder.query(constructor.listBuilders());
        // 设置SearchSourceBuilder查询属性
        searchRequest.source(searchSourceBuilder);
        log.debug("get rs" + searchSourceBuilder.toString());
        CountResponse response = client.count(searchRequest, RequestOptions.DEFAULT);
        ESResult<?> esResult = new ESResult<>();
        esResult.setTotal(response.getCount());

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        return esResult;
    }

    /**
     * 批量增加或更新
     *
     * @throws IOException
     */
    public <T> void bulkAddOrUpdate(Map<String, T> docs) throws IOException {
        long startTime = System.currentTimeMillis();

        BulkRequest br = new BulkRequest();
        for (Map.Entry<String, T> doc : docs.entrySet()) {
            IndexRequest ir = new IndexRequest(getIndexName());
            ir.id(doc.getKey());
            ir.source(JSON.toJSONString(doc.getValue()), XContentType.JSON);
            br.add(ir);
        }
        try {
            BulkResponse response = client.bulk(br, RequestOptions.DEFAULT);
            log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
            log.debug("bulkAdd:{}", JSON.toJSONString(response, true));
        } catch (Exception e) {
            log.error("ES批量增加或更新异常", e);
            if (e instanceof RuntimeException && e.getMessage().contains("Request cannot be executed; I/O reactor status: STOPPED")) {
                client = (RestHighLevelClient) springUtils.resetSpring("restHighLevelClient");
                BulkResponse response = client.bulk(br, RequestOptions.DEFAULT);
                log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);
                log.debug("bulkAdd:{}", JSON.toJSONString(response, true));
            }
            throw e;
        }

    }

    /**
     * 批量删除
     *
     * @throws IOException
     */
    public void bulkDelete(List<String> ids) throws IOException {
        long startTime = System.currentTimeMillis();

        BulkRequest br = new BulkRequest();
        for (String id : ids) {
            DeleteRequest dr = new DeleteRequest(getIndexName(), id);
            br.add(dr);
        }
        BulkResponse response = client.bulk(br, RequestOptions.DEFAULT);

        log.debug(CONSUME_TIME_TIPS, System.currentTimeMillis() - startTime);

        log.debug("bulkDelete:{}", JSON.toJSONString(response, true));
    }

}
