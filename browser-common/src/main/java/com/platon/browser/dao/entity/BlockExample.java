package com.platon.browser.dao.entity;

import java.math.BigDecimal;
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

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return (Criteria) this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return (Criteria) this;
        }

        public Criteria andNumEqualTo(Long value) {
            addCriterion("num =", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotEqualTo(Long value) {
            addCriterion("num <>", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThan(Long value) {
            addCriterion("num >", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThanOrEqualTo(Long value) {
            addCriterion("num >=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThan(Long value) {
            addCriterion("num <", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThanOrEqualTo(Long value) {
            addCriterion("num <=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumIn(List<Long> values) {
            addCriterion("num in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotIn(List<Long> values) {
            addCriterion("num not in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumBetween(Long value1, Long value2) {
            addCriterion("num between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotBetween(Long value1, Long value2) {
            addCriterion("num not between", value1, value2, "num");
            return (Criteria) this;
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

        public Criteria andPHashIsNull() {
            addCriterion("p_hash is null");
            return (Criteria) this;
        }

        public Criteria andPHashIsNotNull() {
            addCriterion("p_hash is not null");
            return (Criteria) this;
        }

        public Criteria andPHashEqualTo(String value) {
            addCriterion("p_hash =", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashNotEqualTo(String value) {
            addCriterion("p_hash <>", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashGreaterThan(String value) {
            addCriterion("p_hash >", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashGreaterThanOrEqualTo(String value) {
            addCriterion("p_hash >=", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashLessThan(String value) {
            addCriterion("p_hash <", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashLessThanOrEqualTo(String value) {
            addCriterion("p_hash <=", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashLike(String value) {
            addCriterion("p_hash like", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashNotLike(String value) {
            addCriterion("p_hash not like", value, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashIn(List<String> values) {
            addCriterion("p_hash in", values, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashNotIn(List<String> values) {
            addCriterion("p_hash not in", values, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashBetween(String value1, String value2) {
            addCriterion("p_hash between", value1, value2, "pHash");
            return (Criteria) this;
        }

        public Criteria andPHashNotBetween(String value1, String value2) {
            addCriterion("p_hash not between", value1, value2, "pHash");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("`time` is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("`time` is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("`time` =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("`time` <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("`time` >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`time` >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("`time` <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("`time` <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("`time` in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("`time` not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("`time` between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("`time` not between", value1, value2, "time");
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

        public Criteria andGasLimitIsNull() {
            addCriterion("gas_limit is null");
            return (Criteria) this;
        }

        public Criteria andGasLimitIsNotNull() {
            addCriterion("gas_limit is not null");
            return (Criteria) this;
        }

        public Criteria andGasLimitEqualTo(BigDecimal value) {
            addCriterion("gas_limit =", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotEqualTo(BigDecimal value) {
            addCriterion("gas_limit <>", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitGreaterThan(BigDecimal value) {
            addCriterion("gas_limit >", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("gas_limit >=", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitLessThan(BigDecimal value) {
            addCriterion("gas_limit <", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("gas_limit <=", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitIn(List<BigDecimal> values) {
            addCriterion("gas_limit in", values, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotIn(List<BigDecimal> values) {
            addCriterion("gas_limit not in", values, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gas_limit between", value1, value2, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gas_limit not between", value1, value2, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasUsedIsNull() {
            addCriterion("gas_used is null");
            return (Criteria) this;
        }

        public Criteria andGasUsedIsNotNull() {
            addCriterion("gas_used is not null");
            return (Criteria) this;
        }

        public Criteria andGasUsedEqualTo(BigDecimal value) {
            addCriterion("gas_used =", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotEqualTo(BigDecimal value) {
            addCriterion("gas_used <>", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedGreaterThan(BigDecimal value) {
            addCriterion("gas_used >", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("gas_used >=", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedLessThan(BigDecimal value) {
            addCriterion("gas_used <", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("gas_used <=", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedIn(List<BigDecimal> values) {
            addCriterion("gas_used in", values, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotIn(List<BigDecimal> values) {
            addCriterion("gas_used not in", values, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gas_used between", value1, value2, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("gas_used not between", value1, value2, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andTxQtyIsNull() {
            addCriterion("tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andTxQtyIsNotNull() {
            addCriterion("tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTxQtyEqualTo(Integer value) {
            addCriterion("tx_qty =", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotEqualTo(Integer value) {
            addCriterion("tx_qty <>", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyGreaterThan(Integer value) {
            addCriterion("tx_qty >", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_qty >=", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyLessThan(Integer value) {
            addCriterion("tx_qty <", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("tx_qty <=", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyIn(List<Integer> values) {
            addCriterion("tx_qty in", values, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotIn(List<Integer> values) {
            addCriterion("tx_qty not in", values, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyBetween(Integer value1, Integer value2) {
            addCriterion("tx_qty between", value1, value2, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_qty not between", value1, value2, "txQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyIsNull() {
            addCriterion("tran_qty is null");
            return (Criteria) this;
        }

        public Criteria andTranQtyIsNotNull() {
            addCriterion("tran_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTranQtyEqualTo(Integer value) {
            addCriterion("tran_qty =", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyNotEqualTo(Integer value) {
            addCriterion("tran_qty <>", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyGreaterThan(Integer value) {
            addCriterion("tran_qty >", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("tran_qty >=", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyLessThan(Integer value) {
            addCriterion("tran_qty <", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyLessThanOrEqualTo(Integer value) {
            addCriterion("tran_qty <=", value, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyIn(List<Integer> values) {
            addCriterion("tran_qty in", values, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyNotIn(List<Integer> values) {
            addCriterion("tran_qty not in", values, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyBetween(Integer value1, Integer value2) {
            addCriterion("tran_qty between", value1, value2, "tranQty");
            return (Criteria) this;
        }

        public Criteria andTranQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("tran_qty not between", value1, value2, "tranQty");
            return (Criteria) this;
        }

        public Criteria andSQtyIsNull() {
            addCriterion("s_qty is null");
            return (Criteria) this;
        }

        public Criteria andSQtyIsNotNull() {
            addCriterion("s_qty is not null");
            return (Criteria) this;
        }

        public Criteria andSQtyEqualTo(Integer value) {
            addCriterion("s_qty =", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyNotEqualTo(Integer value) {
            addCriterion("s_qty <>", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyGreaterThan(Integer value) {
            addCriterion("s_qty >", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("s_qty >=", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyLessThan(Integer value) {
            addCriterion("s_qty <", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyLessThanOrEqualTo(Integer value) {
            addCriterion("s_qty <=", value, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyIn(List<Integer> values) {
            addCriterion("s_qty in", values, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyNotIn(List<Integer> values) {
            addCriterion("s_qty not in", values, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyBetween(Integer value1, Integer value2) {
            addCriterion("s_qty between", value1, value2, "sQty");
            return (Criteria) this;
        }

        public Criteria andSQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("s_qty not between", value1, value2, "sQty");
            return (Criteria) this;
        }

        public Criteria andPQtyIsNull() {
            addCriterion("p_qty is null");
            return (Criteria) this;
        }

        public Criteria andPQtyIsNotNull() {
            addCriterion("p_qty is not null");
            return (Criteria) this;
        }

        public Criteria andPQtyEqualTo(Integer value) {
            addCriterion("p_qty =", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyNotEqualTo(Integer value) {
            addCriterion("p_qty <>", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyGreaterThan(Integer value) {
            addCriterion("p_qty >", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("p_qty >=", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyLessThan(Integer value) {
            addCriterion("p_qty <", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyLessThanOrEqualTo(Integer value) {
            addCriterion("p_qty <=", value, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyIn(List<Integer> values) {
            addCriterion("p_qty in", values, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyNotIn(List<Integer> values) {
            addCriterion("p_qty not in", values, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyBetween(Integer value1, Integer value2) {
            addCriterion("p_qty between", value1, value2, "pQty");
            return (Criteria) this;
        }

        public Criteria andPQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("p_qty not between", value1, value2, "pQty");
            return (Criteria) this;
        }

        public Criteria andDQtyIsNull() {
            addCriterion("d_qty is null");
            return (Criteria) this;
        }

        public Criteria andDQtyIsNotNull() {
            addCriterion("d_qty is not null");
            return (Criteria) this;
        }

        public Criteria andDQtyEqualTo(Integer value) {
            addCriterion("d_qty =", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyNotEqualTo(Integer value) {
            addCriterion("d_qty <>", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyGreaterThan(Integer value) {
            addCriterion("d_qty >", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("d_qty >=", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyLessThan(Integer value) {
            addCriterion("d_qty <", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyLessThanOrEqualTo(Integer value) {
            addCriterion("d_qty <=", value, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyIn(List<Integer> values) {
            addCriterion("d_qty in", values, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyNotIn(List<Integer> values) {
            addCriterion("d_qty not in", values, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyBetween(Integer value1, Integer value2) {
            addCriterion("d_qty between", value1, value2, "dQty");
            return (Criteria) this;
        }

        public Criteria andDQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("d_qty not between", value1, value2, "dQty");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitIsNull() {
            addCriterion("tx_gas_limit is null");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitIsNotNull() {
            addCriterion("tx_gas_limit is not null");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitEqualTo(BigDecimal value) {
            addCriterion("tx_gas_limit =", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitNotEqualTo(BigDecimal value) {
            addCriterion("tx_gas_limit <>", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitGreaterThan(BigDecimal value) {
            addCriterion("tx_gas_limit >", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tx_gas_limit >=", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitLessThan(BigDecimal value) {
            addCriterion("tx_gas_limit <", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tx_gas_limit <=", value, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitIn(List<BigDecimal> values) {
            addCriterion("tx_gas_limit in", values, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitNotIn(List<BigDecimal> values) {
            addCriterion("tx_gas_limit not in", values, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tx_gas_limit between", value1, value2, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxGasLimitNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tx_gas_limit not between", value1, value2, "txGasLimit");
            return (Criteria) this;
        }

        public Criteria andTxFeeIsNull() {
            addCriterion("tx_fee is null");
            return (Criteria) this;
        }

        public Criteria andTxFeeIsNotNull() {
            addCriterion("tx_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTxFeeEqualTo(BigDecimal value) {
            addCriterion("tx_fee =", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeNotEqualTo(BigDecimal value) {
            addCriterion("tx_fee <>", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeGreaterThan(BigDecimal value) {
            addCriterion("tx_fee >", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tx_fee >=", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeLessThan(BigDecimal value) {
            addCriterion("tx_fee <", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tx_fee <=", value, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeIn(List<BigDecimal> values) {
            addCriterion("tx_fee in", values, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeNotIn(List<BigDecimal> values) {
            addCriterion("tx_fee not in", values, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tx_fee between", value1, value2, "txFee");
            return (Criteria) this;
        }

        public Criteria andTxFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tx_fee not between", value1, value2, "txFee");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNull() {
            addCriterion("node_name is null");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNotNull() {
            addCriterion("node_name is not null");
            return (Criteria) this;
        }

        public Criteria andNodeNameEqualTo(String value) {
            addCriterion("node_name =", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotEqualTo(String value) {
            addCriterion("node_name <>", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThan(String value) {
            addCriterion("node_name >", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("node_name >=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThan(String value) {
            addCriterion("node_name <", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThanOrEqualTo(String value) {
            addCriterion("node_name <=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLike(String value) {
            addCriterion("node_name like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotLike(String value) {
            addCriterion("node_name not like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameIn(List<String> values) {
            addCriterion("node_name in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotIn(List<String> values) {
            addCriterion("node_name not in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameBetween(String value1, String value2) {
            addCriterion("node_name between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotBetween(String value1, String value2) {
            addCriterion("node_name not between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNull() {
            addCriterion("node_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNotNull() {
            addCriterion("node_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIdEqualTo(String value) {
            addCriterion("node_id =", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotEqualTo(String value) {
            addCriterion("node_id <>", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThan(String value) {
            addCriterion("node_id >", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("node_id >=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThan(String value) {
            addCriterion("node_id <", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThanOrEqualTo(String value) {
            addCriterion("node_id <=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLike(String value) {
            addCriterion("node_id like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotLike(String value) {
            addCriterion("node_id not like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIn(List<String> values) {
            addCriterion("node_id in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotIn(List<String> values) {
            addCriterion("node_id not in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdBetween(String value1, String value2) {
            addCriterion("node_id between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotBetween(String value1, String value2) {
            addCriterion("node_id not between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andRewardIsNull() {
            addCriterion("reward is null");
            return (Criteria) this;
        }

        public Criteria andRewardIsNotNull() {
            addCriterion("reward is not null");
            return (Criteria) this;
        }

        public Criteria andRewardEqualTo(BigDecimal value) {
            addCriterion("reward =", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotEqualTo(BigDecimal value) {
            addCriterion("reward <>", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThan(BigDecimal value) {
            addCriterion("reward >", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reward >=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThan(BigDecimal value) {
            addCriterion("reward <", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reward <=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardIn(List<BigDecimal> values) {
            addCriterion("reward in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotIn(List<BigDecimal> values) {
            addCriterion("reward not in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward between", value1, value2, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward not between", value1, value2, "reward");
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

        public Criteria andCreTimeIsNull() {
            addCriterion("cre_time is null");
            return (Criteria) this;
        }

        public Criteria andCreTimeIsNotNull() {
            addCriterion("cre_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreTimeEqualTo(Date value) {
            addCriterion("cre_time =", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeNotEqualTo(Date value) {
            addCriterion("cre_time <>", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeGreaterThan(Date value) {
            addCriterion("cre_time >", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("cre_time >=", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeLessThan(Date value) {
            addCriterion("cre_time <", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeLessThanOrEqualTo(Date value) {
            addCriterion("cre_time <=", value, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeIn(List<Date> values) {
            addCriterion("cre_time in", values, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeNotIn(List<Date> values) {
            addCriterion("cre_time not in", values, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeBetween(Date value1, Date value2) {
            addCriterion("cre_time between", value1, value2, "creTime");
            return (Criteria) this;
        }

        public Criteria andCreTimeNotBetween(Date value1, Date value2) {
            addCriterion("cre_time not between", value1, value2, "creTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeIsNull() {
            addCriterion("upd_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdTimeIsNotNull() {
            addCriterion("upd_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdTimeEqualTo(Date value) {
            addCriterion("upd_time =", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeNotEqualTo(Date value) {
            addCriterion("upd_time <>", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeGreaterThan(Date value) {
            addCriterion("upd_time >", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("upd_time >=", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeLessThan(Date value) {
            addCriterion("upd_time <", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeLessThanOrEqualTo(Date value) {
            addCriterion("upd_time <=", value, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeIn(List<Date> values) {
            addCriterion("upd_time in", values, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeNotIn(List<Date> values) {
            addCriterion("upd_time not in", values, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeBetween(Date value1, Date value2) {
            addCriterion("upd_time between", value1, value2, "updTime");
            return (Criteria) this;
        }

        public Criteria andUpdTimeNotBetween(Date value1, Date value2) {
            addCriterion("upd_time not between", value1, value2, "updTime");
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