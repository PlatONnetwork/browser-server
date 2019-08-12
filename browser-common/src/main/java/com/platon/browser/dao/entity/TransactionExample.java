package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TransactionExample() {
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

        public Criteria andBlockNumberIsNull() {
            addCriterion("block_number is null");
            return (Criteria) this;
        }

        public Criteria andBlockNumberIsNotNull() {
            addCriterion("block_number is not null");
            return (Criteria) this;
        }

        public Criteria andBlockNumberEqualTo(Long value) {
            addCriterion("block_number =", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberNotEqualTo(Long value) {
            addCriterion("block_number <>", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberGreaterThan(Long value) {
            addCriterion("block_number >", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("block_number >=", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberLessThan(Long value) {
            addCriterion("block_number <", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberLessThanOrEqualTo(Long value) {
            addCriterion("block_number <=", value, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberIn(List<Long> values) {
            addCriterion("block_number in", values, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberNotIn(List<Long> values) {
            addCriterion("block_number not in", values, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberBetween(Long value1, Long value2) {
            addCriterion("block_number between", value1, value2, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockNumberNotBetween(Long value1, Long value2) {
            addCriterion("block_number not between", value1, value2, "blockNumber");
            return (Criteria) this;
        }

        public Criteria andBlockHashIsNull() {
            addCriterion("block_hash is null");
            return (Criteria) this;
        }

        public Criteria andBlockHashIsNotNull() {
            addCriterion("block_hash is not null");
            return (Criteria) this;
        }

        public Criteria andBlockHashEqualTo(String value) {
            addCriterion("block_hash =", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotEqualTo(String value) {
            addCriterion("block_hash <>", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashGreaterThan(String value) {
            addCriterion("block_hash >", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashGreaterThanOrEqualTo(String value) {
            addCriterion("block_hash >=", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLessThan(String value) {
            addCriterion("block_hash <", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLessThanOrEqualTo(String value) {
            addCriterion("block_hash <=", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLike(String value) {
            addCriterion("block_hash like", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotLike(String value) {
            addCriterion("block_hash not like", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashIn(List<String> values) {
            addCriterion("block_hash in", values, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotIn(List<String> values) {
            addCriterion("block_hash not in", values, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashBetween(String value1, String value2) {
            addCriterion("block_hash between", value1, value2, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotBetween(String value1, String value2) {
            addCriterion("block_hash not between", value1, value2, "blockHash");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexIsNull() {
            addCriterion("transaction_index is null");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexIsNotNull() {
            addCriterion("transaction_index is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexEqualTo(Integer value) {
            addCriterion("transaction_index =", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotEqualTo(Integer value) {
            addCriterion("transaction_index <>", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexGreaterThan(Integer value) {
            addCriterion("transaction_index >", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("transaction_index >=", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexLessThan(Integer value) {
            addCriterion("transaction_index <", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexLessThanOrEqualTo(Integer value) {
            addCriterion("transaction_index <=", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexIn(List<Integer> values) {
            addCriterion("transaction_index in", values, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotIn(List<Integer> values) {
            addCriterion("transaction_index not in", values, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexBetween(Integer value1, Integer value2) {
            addCriterion("transaction_index between", value1, value2, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("transaction_index not between", value1, value2, "transactionIndex");
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

        public Criteria andGasPriceIsNull() {
            addCriterion("gas_price is null");
            return (Criteria) this;
        }

        public Criteria andGasPriceIsNotNull() {
            addCriterion("gas_price is not null");
            return (Criteria) this;
        }

        public Criteria andGasPriceEqualTo(String value) {
            addCriterion("gas_price =", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceNotEqualTo(String value) {
            addCriterion("gas_price <>", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceGreaterThan(String value) {
            addCriterion("gas_price >", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceGreaterThanOrEqualTo(String value) {
            addCriterion("gas_price >=", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceLessThan(String value) {
            addCriterion("gas_price <", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceLessThanOrEqualTo(String value) {
            addCriterion("gas_price <=", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceLike(String value) {
            addCriterion("gas_price like", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceNotLike(String value) {
            addCriterion("gas_price not like", value, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceIn(List<String> values) {
            addCriterion("gas_price in", values, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceNotIn(List<String> values) {
            addCriterion("gas_price not in", values, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceBetween(String value1, String value2) {
            addCriterion("gas_price between", value1, value2, "gasPrice");
            return (Criteria) this;
        }

        public Criteria andGasPriceNotBetween(String value1, String value2) {
            addCriterion("gas_price not between", value1, value2, "gasPrice");
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

        public Criteria andGasUsedEqualTo(String value) {
            addCriterion("gas_used =", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotEqualTo(String value) {
            addCriterion("gas_used <>", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedGreaterThan(String value) {
            addCriterion("gas_used >", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedGreaterThanOrEqualTo(String value) {
            addCriterion("gas_used >=", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedLessThan(String value) {
            addCriterion("gas_used <", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedLessThanOrEqualTo(String value) {
            addCriterion("gas_used <=", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedLike(String value) {
            addCriterion("gas_used like", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotLike(String value) {
            addCriterion("gas_used not like", value, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedIn(List<String> values) {
            addCriterion("gas_used in", values, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotIn(List<String> values) {
            addCriterion("gas_used not in", values, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedBetween(String value1, String value2) {
            addCriterion("gas_used between", value1, value2, "gasUsed");
            return (Criteria) this;
        }

        public Criteria andGasUsedNotBetween(String value1, String value2) {
            addCriterion("gas_used not between", value1, value2, "gasUsed");
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

        public Criteria andGasLimitEqualTo(String value) {
            addCriterion("gas_limit =", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotEqualTo(String value) {
            addCriterion("gas_limit <>", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitGreaterThan(String value) {
            addCriterion("gas_limit >", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitGreaterThanOrEqualTo(String value) {
            addCriterion("gas_limit >=", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitLessThan(String value) {
            addCriterion("gas_limit <", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitLessThanOrEqualTo(String value) {
            addCriterion("gas_limit <=", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitLike(String value) {
            addCriterion("gas_limit like", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotLike(String value) {
            addCriterion("gas_limit not like", value, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitIn(List<String> values) {
            addCriterion("gas_limit in", values, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotIn(List<String> values) {
            addCriterion("gas_limit not in", values, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitBetween(String value1, String value2) {
            addCriterion("gas_limit between", value1, value2, "gasLimit");
            return (Criteria) this;
        }

        public Criteria andGasLimitNotBetween(String value1, String value2) {
            addCriterion("gas_limit not between", value1, value2, "gasLimit");
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

        public Criteria andActualTxCostIsNull() {
            addCriterion("actual_tx_cost is null");
            return (Criteria) this;
        }

        public Criteria andActualTxCostIsNotNull() {
            addCriterion("actual_tx_cost is not null");
            return (Criteria) this;
        }

        public Criteria andActualTxCostEqualTo(String value) {
            addCriterion("actual_tx_cost =", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostNotEqualTo(String value) {
            addCriterion("actual_tx_cost <>", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostGreaterThan(String value) {
            addCriterion("actual_tx_cost >", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostGreaterThanOrEqualTo(String value) {
            addCriterion("actual_tx_cost >=", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostLessThan(String value) {
            addCriterion("actual_tx_cost <", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostLessThanOrEqualTo(String value) {
            addCriterion("actual_tx_cost <=", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostLike(String value) {
            addCriterion("actual_tx_cost like", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostNotLike(String value) {
            addCriterion("actual_tx_cost not like", value, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostIn(List<String> values) {
            addCriterion("actual_tx_cost in", values, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostNotIn(List<String> values) {
            addCriterion("actual_tx_cost not in", values, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostBetween(String value1, String value2) {
            addCriterion("actual_tx_cost between", value1, value2, "actualTxCost");
            return (Criteria) this;
        }

        public Criteria andActualTxCostNotBetween(String value1, String value2) {
            addCriterion("actual_tx_cost not between", value1, value2, "actualTxCost");
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

        public Criteria andTxReceiptStatusIsNull() {
            addCriterion("tx_receipt_status is null");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusIsNotNull() {
            addCriterion("tx_receipt_status is not null");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusEqualTo(Integer value) {
            addCriterion("tx_receipt_status =", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotEqualTo(Integer value) {
            addCriterion("tx_receipt_status <>", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusGreaterThan(Integer value) {
            addCriterion("tx_receipt_status >", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_receipt_status >=", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusLessThan(Integer value) {
            addCriterion("tx_receipt_status <", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusLessThanOrEqualTo(Integer value) {
            addCriterion("tx_receipt_status <=", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusIn(List<Integer> values) {
            addCriterion("tx_receipt_status in", values, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotIn(List<Integer> values) {
            addCriterion("tx_receipt_status not in", values, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusBetween(Integer value1, Integer value2) {
            addCriterion("tx_receipt_status between", value1, value2, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_receipt_status not between", value1, value2, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andSequenceIsNull() {
            addCriterion("`sequence` is null");
            return (Criteria) this;
        }

        public Criteria andSequenceIsNotNull() {
            addCriterion("`sequence` is not null");
            return (Criteria) this;
        }

        public Criteria andSequenceEqualTo(Long value) {
            addCriterion("`sequence` =", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceNotEqualTo(Long value) {
            addCriterion("`sequence` <>", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceGreaterThan(Long value) {
            addCriterion("`sequence` >", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceGreaterThanOrEqualTo(Long value) {
            addCriterion("`sequence` >=", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceLessThan(Long value) {
            addCriterion("`sequence` <", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceLessThanOrEqualTo(Long value) {
            addCriterion("`sequence` <=", value, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceIn(List<Long> values) {
            addCriterion("`sequence` in", values, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceNotIn(List<Long> values) {
            addCriterion("`sequence` not in", values, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceBetween(Long value1, Long value2) {
            addCriterion("`sequence` between", value1, value2, "sequence");
            return (Criteria) this;
        }

        public Criteria andSequenceNotBetween(Long value1, Long value2) {
            addCriterion("`sequence` not between", value1, value2, "sequence");
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