package com.platon.browser.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
*@program: InnerTxESRepository.java
*@description: 
*@author: Rongjin Zhang
*@create: 2020/9/22
*/
@Slf4j
@Repository
public class InnerTxESRepository extends ESRepository {
    @Value("${spring.elasticsearch.high-level-client.innerTxIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
    
    public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

    /**
     * 根据token合约地址分组统计token合约交易数
     * @return
     */
	public Map<String,Long> groupContractTxCount(){
	    Map<String,Long> countMap = new HashMap<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //根据姓名进行分组统计个数
            TermsAggregationBuilder field = AggregationBuilders.terms("token_contract").field("contract");
            ValueCountAggregationBuilder countField = AggregationBuilders.count("tx_count").field("contract");
            field.subAggregation(countField);
            searchSourceBuilder.aggregation(field);
            SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            //分组在es中是分桶
            ParsedStringTerms termsName = response.getAggregations().get("token_contract");
            List<? extends Terms.Bucket> buckets = termsName.getBuckets();
            buckets.forEach(naem -> {
                String key = (String) naem.getKey();
                ParsedValueCount countName = naem.getAggregations().get("tx_count");
                double value = countName.value();
                countMap.put(key,(long)value);
                log.info("name , count {} {}", key, value);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countMap;
    }

    /**
     * 根据from地址分组统计token合约交易数
     * @return
     */
    public Map<String,Long> groupFromTxCount(){
        Map<String,Long> countMap = new HashMap<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //根据姓名进行分组统计个数
            TermsAggregationBuilder field = AggregationBuilders.terms("from_group").field("from");
            ValueCountAggregationBuilder countField = AggregationBuilders.count("tx_count").field("from");
            field.subAggregation(countField);
            searchSourceBuilder.aggregation(field);
            SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            //分组在es中是分桶
            ParsedStringTerms termsName = response.getAggregations().get("from_group");
            List<? extends Terms.Bucket> buckets = termsName.getBuckets();
            buckets.forEach(naem -> {
                String key = (String) naem.getKey();
                ParsedValueCount countName = naem.getAggregations().get("tx_count");
                double value = countName.value();
                countMap.put(key,(long)value);
                log.info("name , count {} {}", key, value);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countMap;
    }

    /**
     * 根据from地址分组统计token合约交易数
     * @return
     */
    public Map<String,Long> groupTtoTxCount(){
        Map<String,Long> countMap = new HashMap<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //根据姓名进行分组统计个数
            TermsAggregationBuilder field = AggregationBuilders.terms("tto_group").field("tto");
            ValueCountAggregationBuilder countField = AggregationBuilders.count("tx_count").field("tto");
            field.subAggregation(countField);
            searchSourceBuilder.aggregation(field);
            SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            //分组在es中是分桶
            ParsedStringTerms termsName = response.getAggregations().get("tto_group");
            List<? extends Terms.Bucket> buckets = termsName.getBuckets();
            buckets.forEach(naem -> {
                String key = (String) naem.getKey();
                ParsedValueCount countName = naem.getAggregations().get("tx_count");
                double value = countName.value();
                countMap.put(key,(long)value);
                log.info("name , count {} {}", key, value);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countMap;
    }

    /**
     * 按地址分组查询from地址等于tto地址的交易数
     * @return
     */
    public Map<String,Long> groupFromEqualsTtoTxCount(){
        Map<String,Long> countMap = new HashMap<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0).size(0);

            Map<String, Object> params = new HashMap<>();//存放参数的map
            Script script =new Script(
                    ScriptType.INLINE,
                    "painless",
                    "doc['from'].value == doc['tto'].value",
                    params
            );//脚本文件名称，脚本类型
            ScriptQueryBuilder scriptQueryBuilder = QueryBuilders.scriptQuery(script);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder = boolQueryBuilder.must(scriptQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);

            //根据姓名进行分组统计个数
            TermsAggregationBuilder field = AggregationBuilders.terms("from_eq_tto").field("from");
            ValueCountAggregationBuilder countField = AggregationBuilders.count("tx_count").field("from");
            field.subAggregation(countField);
            searchSourceBuilder.aggregation(field);
            SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            //分组在es中是分桶
            ParsedStringTerms termsName = response.getAggregations().get("from_eq_tto");
            List<? extends Terms.Bucket> buckets = termsName.getBuckets();
            buckets.forEach(naem -> {
                String key = (String) naem.getKey();
                ParsedValueCount countName = naem.getAggregations().get("tx_count");
                double value = countName.value();
                countMap.put(key,(long)value);
                log.info("name , count {} {}", key, value);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countMap;
    }
}
