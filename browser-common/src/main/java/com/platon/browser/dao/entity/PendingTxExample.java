package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PendingTxExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PendingTxExample() {
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

        public Criteria andFromIsNull() {
            addCriterion("`from` is null");
            return (Criteria) this;
        }

        public Criteria andFromIsNotNull() {
            addCriterion("`from` is not null");
            return (Criteria) this;
        }

        public Criteria andFromEqualTo(String value) {
            addCriterion("`from` =", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromNotEqualTo(String value) {
            addCriterion("`from` <>", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromGreaterThan(String value) {
            addCriterion("`from` >", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromGreaterThanOrEqualTo(String value) {
            addCriterion("`from` >=", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromLessThan(String value) {
            addCriterion("`from` <", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromLessThanOrEqualTo(String value) {
            addCriterion("`from` <=", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromLike(String value) {
            addCriterion("`from` like", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromNotLike(String value) {
            addCriterion("`from` not like", value, "from");
            return (Criteria) this;
        }

        public Criteria andFromIn(List<String> values) {
            addCriterion("`from` in", values, "from");
            return (Criteria) this;
        }

        public Criteria andFromNotIn(List<String> values) {
            addCriterion("`from` not in", values, "from");
            return (Criteria) this;
        }

        public Criteria andFromBetween(String value1, String value2) {
            addCriterion("`from` between", value1, value2, "from");
            return (Criteria) this;
        }

        public Criteria andFromNotBetween(String value1, String value2) {
            addCriterion("`from` not between", value1, value2, "from");
            return (Criteria) this;
        }

        public Criteria andToIsNull() {
            addCriterion("`to` is null");
            return (Criteria) this;
        }

        public Criteria andToIsNotNull() {
            addCriterion("`to` is not null");
            return (Criteria) this;
        }

        public Criteria andToEqualTo(String value) {
            addCriterion("`to` =", value, "to");
            return (Criteria) this;
        }

        public Criteria andToNotEqualTo(String value) {
            addCriterion("`to` <>", value, "to");
            return (Criteria) this;
        }

        public Criteria andToGreaterThan(String value) {
            addCriterion("`to` >", value, "to");
            return (Criteria) this;
        }

        public Criteria andToGreaterThanOrEqualTo(String value) {
            addCriterion("`to` >=", value, "to");
            return (Criteria) this;
        }

        public Criteria andToLessThan(String value) {
            addCriterion("`to` <", value, "to");
            return (Criteria) this;
        }

        public Criteria andToLessThanOrEqualTo(String value) {
            addCriterion("`to` <=", value, "to");
            return (Criteria) this;
        }

        public Criteria andToLike(String value) {
            addCriterion("`to` like", value, "to");
            return (Criteria) this;
        }

        public Criteria andToNotLike(String value) {
            addCriterion("`to` not like", value, "to");
            return (Criteria) this;
        }

        public Criteria andToIn(List<String> values) {
            addCriterion("`to` in", values, "to");
            return (Criteria) this;
        }

        public Criteria andToNotIn(List<String> values) {
            addCriterion("`to` not in", values, "to");
            return (Criteria) this;
        }

        public Criteria andToBetween(String value1, String value2) {
            addCriterion("`to` between", value1, value2, "to");
            return (Criteria) this;
        }

        public Criteria andToNotBetween(String value1, String value2) {
            addCriterion("`to` not between", value1, value2, "to");
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

        public Criteria andEnergonPriceIsNull() {
            addCriterion("energon_price is null");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceIsNotNull() {
            addCriterion("energon_price is not null");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceEqualTo(String value) {
            addCriterion("energon_price =", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceNotEqualTo(String value) {
            addCriterion("energon_price <>", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceGreaterThan(String value) {
            addCriterion("energon_price >", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceGreaterThanOrEqualTo(String value) {
            addCriterion("energon_price >=", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceLessThan(String value) {
            addCriterion("energon_price <", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceLessThanOrEqualTo(String value) {
            addCriterion("energon_price <=", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceLike(String value) {
            addCriterion("energon_price like", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceNotLike(String value) {
            addCriterion("energon_price not like", value, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceIn(List<String> values) {
            addCriterion("energon_price in", values, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceNotIn(List<String> values) {
            addCriterion("energon_price not in", values, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceBetween(String value1, String value2) {
            addCriterion("energon_price between", value1, value2, "energonPrice");
            return (Criteria) this;
        }

        public Criteria andEnergonPriceNotBetween(String value1, String value2) {
            addCriterion("energon_price not between", value1, value2, "energonPrice");
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

        public Criteria andTxTypeIsNull() {
            addCriterion("tx_type is null");
            return (Criteria) this;
        }

        public Criteria andTxTypeIsNotNull() {
            addCriterion("tx_type is not null");
            return (Criteria) this;
        }

        public Criteria andTxTypeEqualTo(String value) {
            addCriterion("tx_type =", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotEqualTo(String value) {
            addCriterion("tx_type <>", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeGreaterThan(String value) {
            addCriterion("tx_type >", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeGreaterThanOrEqualTo(String value) {
            addCriterion("tx_type >=", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeLessThan(String value) {
            addCriterion("tx_type <", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeLessThanOrEqualTo(String value) {
            addCriterion("tx_type <=", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeLike(String value) {
            addCriterion("tx_type like", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotLike(String value) {
            addCriterion("tx_type not like", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeIn(List<String> values) {
            addCriterion("tx_type in", values, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotIn(List<String> values) {
            addCriterion("tx_type not in", values, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeBetween(String value1, String value2) {
            addCriterion("tx_type between", value1, value2, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotBetween(String value1, String value2) {
            addCriterion("tx_type not between", value1, value2, "txType");
            return (Criteria) this;
        }

        public Criteria andValueIsNull() {
            addCriterion("`value` is null");
            return (Criteria) this;
        }

        public Criteria andValueIsNotNull() {
            addCriterion("`value` is not null");
            return (Criteria) this;
        }

        public Criteria andValueEqualTo(String value) {
            addCriterion("`value` =", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotEqualTo(String value) {
            addCriterion("`value` <>", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThan(String value) {
            addCriterion("`value` >", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThanOrEqualTo(String value) {
            addCriterion("`value` >=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThan(String value) {
            addCriterion("`value` <", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThanOrEqualTo(String value) {
            addCriterion("`value` <=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLike(String value) {
            addCriterion("`value` like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotLike(String value) {
            addCriterion("`value` not like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueIn(List<String> values) {
            addCriterion("`value` in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotIn(List<String> values) {
            addCriterion("`value` not in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueBetween(String value1, String value2) {
            addCriterion("`value` between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotBetween(String value1, String value2) {
            addCriterion("`value` not between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeIsNull() {
            addCriterion("receive_type is null");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeIsNotNull() {
            addCriterion("receive_type is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeEqualTo(String value) {
            addCriterion("receive_type =", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeNotEqualTo(String value) {
            addCriterion("receive_type <>", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeGreaterThan(String value) {
            addCriterion("receive_type >", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeGreaterThanOrEqualTo(String value) {
            addCriterion("receive_type >=", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeLessThan(String value) {
            addCriterion("receive_type <", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeLessThanOrEqualTo(String value) {
            addCriterion("receive_type <=", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeLike(String value) {
            addCriterion("receive_type like", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeNotLike(String value) {
            addCriterion("receive_type not like", value, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeIn(List<String> values) {
            addCriterion("receive_type in", values, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeNotIn(List<String> values) {
            addCriterion("receive_type not in", values, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeBetween(String value1, String value2) {
            addCriterion("receive_type between", value1, value2, "receiveType");
            return (Criteria) this;
        }

        public Criteria andReceiveTypeNotBetween(String value1, String value2) {
            addCriterion("receive_type not between", value1, value2, "receiveType");
            return (Criteria) this;
        }

        public Criteria andTxInfoIsNull() {
            addCriterion("tx_info is null");
            return (Criteria) this;
        }

        public Criteria andTxInfoIsNotNull() {
            addCriterion("tx_info is not null");
            return (Criteria) this;
        }

        public Criteria andTxInfoEqualTo(String value) {
            addCriterion("tx_info =", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoNotEqualTo(String value) {
            addCriterion("tx_info <>", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoGreaterThan(String value) {
            addCriterion("tx_info >", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoGreaterThanOrEqualTo(String value) {
            addCriterion("tx_info >=", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoLessThan(String value) {
            addCriterion("tx_info <", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoLessThanOrEqualTo(String value) {
            addCriterion("tx_info <=", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoLike(String value) {
            addCriterion("tx_info like", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoNotLike(String value) {
            addCriterion("tx_info not like", value, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoIn(List<String> values) {
            addCriterion("tx_info in", values, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoNotIn(List<String> values) {
            addCriterion("tx_info not in", values, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoBetween(String value1, String value2) {
            addCriterion("tx_info between", value1, value2, "txInfo");
            return (Criteria) this;
        }

        public Criteria andTxInfoNotBetween(String value1, String value2) {
            addCriterion("tx_info not between", value1, value2, "txInfo");
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