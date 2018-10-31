package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BlockExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andHashIsNull() {
            addCriterion("hash is null");
            return (Criteria) this;
        }

        public Criteria andHashIsNotNull() {
            addCriterion("hash is not null");
            return (Criteria) this;
        }

        public Criteria andHashEqualTo(String value) {
            addCriterion("hash =", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotEqualTo(String value) {
            addCriterion("hash <>", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThan(String value) {
            addCriterion("hash >", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashGreaterThanOrEqualTo(String value) {
            addCriterion("hash >=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThan(String value) {
            addCriterion("hash <", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLessThanOrEqualTo(String value) {
            addCriterion("hash <=", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashLike(String value) {
            addCriterion("hash like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotLike(String value) {
            addCriterion("hash not like", value, "hash");
            return (Criteria) this;
        }

        public Criteria andHashIn(List<String> values) {
            addCriterion("hash in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotIn(List<String> values) {
            addCriterion("hash not in", values, "hash");
            return (Criteria) this;
        }

        public Criteria andHashBetween(String value1, String value2) {
            addCriterion("hash between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andHashNotBetween(String value1, String value2) {
            addCriterion("hash not between", value1, value2, "hash");
            return (Criteria) this;
        }

        public Criteria andNumberIsNull() {
            addCriterion("`number` is null");
            return (Criteria) this;
        }

        public Criteria andNumberIsNotNull() {
            addCriterion("`number` is not null");
            return (Criteria) this;
        }

        public Criteria andNumberEqualTo(Long value) {
            addCriterion("`number` =", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotEqualTo(Long value) {
            addCriterion("`number` <>", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThan(Long value) {
            addCriterion("`number` >", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("`number` >=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThan(Long value) {
            addCriterion("`number` <", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThanOrEqualTo(Long value) {
            addCriterion("`number` <=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberIn(List<Long> values) {
            addCriterion("`number` in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotIn(List<Long> values) {
            addCriterion("`number` not in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberBetween(Long value1, Long value2) {
            addCriterion("`number` between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotBetween(Long value1, Long value2) {
            addCriterion("`number` not between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andParentHashIsNull() {
            addCriterion("parent_hash is null");
            return (Criteria) this;
        }

        public Criteria andParentHashIsNotNull() {
            addCriterion("parent_hash is not null");
            return (Criteria) this;
        }

        public Criteria andParentHashEqualTo(String value) {
            addCriterion("parent_hash =", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashNotEqualTo(String value) {
            addCriterion("parent_hash <>", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashGreaterThan(String value) {
            addCriterion("parent_hash >", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashGreaterThanOrEqualTo(String value) {
            addCriterion("parent_hash >=", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashLessThan(String value) {
            addCriterion("parent_hash <", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashLessThanOrEqualTo(String value) {
            addCriterion("parent_hash <=", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashLike(String value) {
            addCriterion("parent_hash like", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashNotLike(String value) {
            addCriterion("parent_hash not like", value, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashIn(List<String> values) {
            addCriterion("parent_hash in", values, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashNotIn(List<String> values) {
            addCriterion("parent_hash not in", values, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashBetween(String value1, String value2) {
            addCriterion("parent_hash between", value1, value2, "parentHash");
            return (Criteria) this;
        }

        public Criteria andParentHashNotBetween(String value1, String value2) {
            addCriterion("parent_hash not between", value1, value2, "parentHash");
            return (Criteria) this;
        }

        public Criteria andNonceIsNull() {
            addCriterion("nonce is null");
            return (Criteria) this;
        }

        public Criteria andNonceIsNotNull() {
            addCriterion("nonce is not null");
            return (Criteria) this;
        }

        public Criteria andNonceEqualTo(String value) {
            addCriterion("nonce =", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceNotEqualTo(String value) {
            addCriterion("nonce <>", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceGreaterThan(String value) {
            addCriterion("nonce >", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceGreaterThanOrEqualTo(String value) {
            addCriterion("nonce >=", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceLessThan(String value) {
            addCriterion("nonce <", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceLessThanOrEqualTo(String value) {
            addCriterion("nonce <=", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceLike(String value) {
            addCriterion("nonce like", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceNotLike(String value) {
            addCriterion("nonce not like", value, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceIn(List<String> values) {
            addCriterion("nonce in", values, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceNotIn(List<String> values) {
            addCriterion("nonce not in", values, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceBetween(String value1, String value2) {
            addCriterion("nonce between", value1, value2, "nonce");
            return (Criteria) this;
        }

        public Criteria andNonceNotBetween(String value1, String value2) {
            addCriterion("nonce not between", value1, value2, "nonce");
            return (Criteria) this;
        }

        public Criteria andMinerIsNull() {
            addCriterion("miner is null");
            return (Criteria) this;
        }

        public Criteria andMinerIsNotNull() {
            addCriterion("miner is not null");
            return (Criteria) this;
        }

        public Criteria andMinerEqualTo(String value) {
            addCriterion("miner =", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerNotEqualTo(String value) {
            addCriterion("miner <>", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerGreaterThan(String value) {
            addCriterion("miner >", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerGreaterThanOrEqualTo(String value) {
            addCriterion("miner >=", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerLessThan(String value) {
            addCriterion("miner <", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerLessThanOrEqualTo(String value) {
            addCriterion("miner <=", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerLike(String value) {
            addCriterion("miner like", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerNotLike(String value) {
            addCriterion("miner not like", value, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerIn(List<String> values) {
            addCriterion("miner in", values, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerNotIn(List<String> values) {
            addCriterion("miner not in", values, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerBetween(String value1, String value2) {
            addCriterion("miner between", value1, value2, "miner");
            return (Criteria) this;
        }

        public Criteria andMinerNotBetween(String value1, String value2) {
            addCriterion("miner not between", value1, value2, "miner");
            return (Criteria) this;
        }

        public Criteria andExtraDataIsNull() {
            addCriterion("extra_data is null");
            return (Criteria) this;
        }

        public Criteria andExtraDataIsNotNull() {
            addCriterion("extra_data is not null");
            return (Criteria) this;
        }

        public Criteria andExtraDataEqualTo(String value) {
            addCriterion("extra_data =", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataNotEqualTo(String value) {
            addCriterion("extra_data <>", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataGreaterThan(String value) {
            addCriterion("extra_data >", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataGreaterThanOrEqualTo(String value) {
            addCriterion("extra_data >=", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataLessThan(String value) {
            addCriterion("extra_data <", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataLessThanOrEqualTo(String value) {
            addCriterion("extra_data <=", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataLike(String value) {
            addCriterion("extra_data like", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataNotLike(String value) {
            addCriterion("extra_data not like", value, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataIn(List<String> values) {
            addCriterion("extra_data in", values, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataNotIn(List<String> values) {
            addCriterion("extra_data not in", values, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataBetween(String value1, String value2) {
            addCriterion("extra_data between", value1, value2, "extraData");
            return (Criteria) this;
        }

        public Criteria andExtraDataNotBetween(String value1, String value2) {
            addCriterion("extra_data not between", value1, value2, "extraData");
            return (Criteria) this;
        }

        public Criteria andSizeIsNull() {
            addCriterion("`size` is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("`size` is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(Integer value) {
            addCriterion("`size` =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(Integer value) {
            addCriterion("`size` <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(Integer value) {
            addCriterion("`size` >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`size` >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(Integer value) {
            addCriterion("`size` <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(Integer value) {
            addCriterion("`size` <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<Integer> values) {
            addCriterion("`size` in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<Integer> values) {
            addCriterion("`size` not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(Integer value1, Integer value2) {
            addCriterion("`size` between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("`size` not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedIsNull() {
            addCriterion("energon_used is null");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedIsNotNull() {
            addCriterion("energon_used is not null");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedEqualTo(String value) {
            addCriterion("energon_used =", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotEqualTo(String value) {
            addCriterion("energon_used <>", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedGreaterThan(String value) {
            addCriterion("energon_used >", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedGreaterThanOrEqualTo(String value) {
            addCriterion("energon_used >=", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedLessThan(String value) {
            addCriterion("energon_used <", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedLessThanOrEqualTo(String value) {
            addCriterion("energon_used <=", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedLike(String value) {
            addCriterion("energon_used like", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotLike(String value) {
            addCriterion("energon_used not like", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedIn(List<String> values) {
            addCriterion("energon_used in", values, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotIn(List<String> values) {
            addCriterion("energon_used not in", values, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedBetween(String value1, String value2) {
            addCriterion("energon_used between", value1, value2, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotBetween(String value1, String value2) {
            addCriterion("energon_used not between", value1, value2, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitIsNull() {
            addCriterion("energon_limit is null");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitIsNotNull() {
            addCriterion("energon_limit is not null");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitEqualTo(String value) {
            addCriterion("energon_limit =", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotEqualTo(String value) {
            addCriterion("energon_limit <>", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitGreaterThan(String value) {
            addCriterion("energon_limit >", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitGreaterThanOrEqualTo(String value) {
            addCriterion("energon_limit >=", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitLessThan(String value) {
            addCriterion("energon_limit <", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitLessThanOrEqualTo(String value) {
            addCriterion("energon_limit <=", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitLike(String value) {
            addCriterion("energon_limit like", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotLike(String value) {
            addCriterion("energon_limit not like", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitIn(List<String> values) {
            addCriterion("energon_limit in", values, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotIn(List<String> values) {
            addCriterion("energon_limit not in", values, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitBetween(String value1, String value2) {
            addCriterion("energon_limit between", value1, value2, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotBetween(String value1, String value2) {
            addCriterion("energon_limit not between", value1, value2, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageIsNull() {
            addCriterion("energon_average is null");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageIsNotNull() {
            addCriterion("energon_average is not null");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageEqualTo(String value) {
            addCriterion("energon_average =", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageNotEqualTo(String value) {
            addCriterion("energon_average <>", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageGreaterThan(String value) {
            addCriterion("energon_average >", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageGreaterThanOrEqualTo(String value) {
            addCriterion("energon_average >=", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageLessThan(String value) {
            addCriterion("energon_average <", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageLessThanOrEqualTo(String value) {
            addCriterion("energon_average <=", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageLike(String value) {
            addCriterion("energon_average like", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageNotLike(String value) {
            addCriterion("energon_average not like", value, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageIn(List<String> values) {
            addCriterion("energon_average in", values, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageNotIn(List<String> values) {
            addCriterion("energon_average not in", values, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageBetween(String value1, String value2) {
            addCriterion("energon_average between", value1, value2, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andEnergonAverageNotBetween(String value1, String value2) {
            addCriterion("energon_average not between", value1, value2, "energonAverage");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("`timestamp` is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("`timestamp` is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Date value) {
            addCriterion("`timestamp` =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Date value) {
            addCriterion("`timestamp` <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Date value) {
            addCriterion("`timestamp` >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("`timestamp` >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Date value) {
            addCriterion("`timestamp` <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Date value) {
            addCriterion("`timestamp` <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Date> values) {
            addCriterion("`timestamp` in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Date> values) {
            addCriterion("`timestamp` not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Date value1, Date value2) {
            addCriterion("`timestamp` between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Date value1, Date value2) {
            addCriterion("`timestamp` not between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andBlockRewardIsNull() {
            addCriterion("block_reward is null");
            return (Criteria) this;
        }

        public Criteria andBlockRewardIsNotNull() {
            addCriterion("block_reward is not null");
            return (Criteria) this;
        }

        public Criteria andBlockRewardEqualTo(String value) {
            addCriterion("block_reward =", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotEqualTo(String value) {
            addCriterion("block_reward <>", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardGreaterThan(String value) {
            addCriterion("block_reward >", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardGreaterThanOrEqualTo(String value) {
            addCriterion("block_reward >=", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardLessThan(String value) {
            addCriterion("block_reward <", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardLessThanOrEqualTo(String value) {
            addCriterion("block_reward <=", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardLike(String value) {
            addCriterion("block_reward like", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotLike(String value) {
            addCriterion("block_reward not like", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardIn(List<String> values) {
            addCriterion("block_reward in", values, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotIn(List<String> values) {
            addCriterion("block_reward not in", values, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardBetween(String value1, String value2) {
            addCriterion("block_reward between", value1, value2, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotBetween(String value1, String value2) {
            addCriterion("block_reward not between", value1, value2, "blockReward");
            return (Criteria) this;
        }

        public Criteria andChainIdIsNull() {
            addCriterion("chain_id is null");
            return (Criteria) this;
        }

        public Criteria andChainIdIsNotNull() {
            addCriterion("chain_id is not null");
            return (Criteria) this;
        }

        public Criteria andChainIdEqualTo(String value) {
            addCriterion("chain_id =", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotEqualTo(String value) {
            addCriterion("chain_id <>", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdGreaterThan(String value) {
            addCriterion("chain_id >", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdGreaterThanOrEqualTo(String value) {
            addCriterion("chain_id >=", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLessThan(String value) {
            addCriterion("chain_id <", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLessThanOrEqualTo(String value) {
            addCriterion("chain_id <=", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLike(String value) {
            addCriterion("chain_id like", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotLike(String value) {
            addCriterion("chain_id not like", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdIn(List<String> values) {
            addCriterion("chain_id in", values, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotIn(List<String> values) {
            addCriterion("chain_id not in", values, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdBetween(String value1, String value2) {
            addCriterion("chain_id between", value1, value2, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotBetween(String value1, String value2) {
            addCriterion("chain_id not between", value1, value2, "chainId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}