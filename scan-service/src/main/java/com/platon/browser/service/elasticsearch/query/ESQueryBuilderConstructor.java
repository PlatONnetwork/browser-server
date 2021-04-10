package com.platon.browser.service.elasticsearch.query;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * es查询 构造器制造工厂
 *
 * @author zhangrj
 * @file ESQueryBuilderConstructor.java
 * @description
 * @data 2019年10月31日
 */
public class ESQueryBuilderConstructor {

    private int size = Integer.MAX_VALUE;

    private int from = 0;

    /**
     * 正序排序字段
     */
    private String asc;

    /**
     * 倒序排序字段
     */
    private String desc;

    /**
     * 排序字段类型
     */
    private String unmappedType;

    //查询条件容器
    private List<ESCriterion> mustCriterions = new ArrayList<>();

    private List<ESCriterion> shouldCriterions = new ArrayList<>();

    private List<ESCriterion> mustNotCriterions = new ArrayList<>();

    private List<BoolQueryBuilder> queryMustBuilders = new ArrayList<>();

    private List<BoolQueryBuilder> queryShouldBuilders = new ArrayList<>();

    private String[] result;

    //构造builder
    public QueryBuilder listBuilders() {
        int count = mustCriterions.size() + shouldCriterions.size() + mustNotCriterions.size() + queryMustBuilders.size() + queryShouldBuilders.size();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder queryBuilder = null;

        if (count >= 1) {
            //must容器
            if (!CollectionUtils.isEmpty(mustCriterions)) {
                for (ESCriterion criterion : mustCriterions) {
                    for (QueryBuilder builder : criterion.listBuilders()) {
                        queryBuilder = boolQueryBuilder.must(builder);
                    }
                }
            }
            //should容器
            if (!CollectionUtils.isEmpty(shouldCriterions)) {
                for (ESCriterion criterion : shouldCriterions) {
                    for (QueryBuilder builder : criterion.listBuilders()) {
                        queryBuilder = boolQueryBuilder.should(builder);
                    }

                }
            }
            //must not 容器
            if (!CollectionUtils.isEmpty(mustNotCriterions)) {
                for (ESCriterion criterion : mustNotCriterions) {
                    for (QueryBuilder builder : criterion.listBuilders()) {
                        queryBuilder = boolQueryBuilder.mustNot(builder);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(queryMustBuilders)) {
                for (BoolQueryBuilder boolQueryBuilder2 : queryMustBuilders) {
                    queryBuilder = boolQueryBuilder.must(boolQueryBuilder2);
                }
            }

            if (!CollectionUtils.isEmpty(queryShouldBuilders)) {
                for (BoolQueryBuilder boolQueryBuilder2 : queryShouldBuilders) {
                    queryBuilder = boolQueryBuilder.should(boolQueryBuilder2);
                }
            }
            return queryBuilder;
        } else {
            return null;
        }
    }

    /**
     * 增加简单且条件表达式
     */
    public ESQueryBuilderConstructor must(ESCriterion criterion) {
        if (criterion != null) {
            mustCriterions.add(criterion);
        }
        return this;
    }

    /**
     * 增加简单或条件表达式
     */
    public ESQueryBuilderConstructor should(ESCriterion criterion) {
        if (criterion != null) {
            shouldCriterions.add(criterion);
        }
        return this;
    }

    /**
     * 增加简单条件表达式
     */
    public ESQueryBuilderConstructor mustNot(ESCriterion criterion) {
        if (criterion != null) {
            mustNotCriterions.add(criterion);
        }
        return this;
    }

    /**
     * 增加复杂条件表达式
     */
    public ESQueryBuilderConstructor buildMust(BoolQueryBuilder boolQueryBuilder) {
        if (boolQueryBuilder != null) {
            queryMustBuilders.add(boolQueryBuilder);
        }
        return this;
    }

    /**
     * 增加复杂条件表达式
     */
    public ESQueryBuilderConstructor buildShould(BoolQueryBuilder boolQueryBuilder) {
        if (boolQueryBuilder != null) {
            queryShouldBuilders.add(boolQueryBuilder);
        }
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }

    public String getUnmappedType() {
        return unmappedType;
    }

    public void setUnmappedType(String unmappedType) {
        this.unmappedType = unmappedType;
    }

}