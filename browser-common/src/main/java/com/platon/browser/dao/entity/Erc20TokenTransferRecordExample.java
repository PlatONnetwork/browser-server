package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Erc20TokenTransferRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Erc20TokenTransferRecordExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTxHashIsNull() {
            addCriterion("tx_hash is null");
            return (Criteria) this;
        }

        public Criteria andTxHashIsNotNull() {
            addCriterion("tx_hash is not null");
            return (Criteria) this;
        }

        public Criteria andTxHashEqualTo(String value) {
            addCriterion("tx_hash =", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotEqualTo(String value) {
            addCriterion("tx_hash <>", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThan(String value) {
            addCriterion("tx_hash >", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThanOrEqualTo(String value) {
            addCriterion("tx_hash >=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThan(String value) {
            addCriterion("tx_hash <", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThanOrEqualTo(String value) {
            addCriterion("tx_hash <=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLike(String value) {
            addCriterion("tx_hash like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotLike(String value) {
            addCriterion("tx_hash not like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashIn(List<String> values) {
            addCriterion("tx_hash in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotIn(List<String> values) {
            addCriterion("tx_hash not in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashBetween(String value1, String value2) {
            addCriterion("tx_hash between", value1, value2, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotBetween(String value1, String value2) {
            addCriterion("tx_hash not between", value1, value2, "txHash");
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

        public Criteria andTxFromIsNull() {
            addCriterion("tx_from is null");
            return (Criteria) this;
        }

        public Criteria andTxFromIsNotNull() {
            addCriterion("tx_from is not null");
            return (Criteria) this;
        }

        public Criteria andTxFromEqualTo(String value) {
            addCriterion("tx_from =", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromNotEqualTo(String value) {
            addCriterion("tx_from <>", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromGreaterThan(String value) {
            addCriterion("tx_from >", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromGreaterThanOrEqualTo(String value) {
            addCriterion("tx_from >=", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromLessThan(String value) {
            addCriterion("tx_from <", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromLessThanOrEqualTo(String value) {
            addCriterion("tx_from <=", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromLike(String value) {
            addCriterion("tx_from like", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromNotLike(String value) {
            addCriterion("tx_from not like", value, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromIn(List<String> values) {
            addCriterion("tx_from in", values, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromNotIn(List<String> values) {
            addCriterion("tx_from not in", values, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromBetween(String value1, String value2) {
            addCriterion("tx_from between", value1, value2, "txFrom");
            return (Criteria) this;
        }

        public Criteria andTxFromNotBetween(String value1, String value2) {
            addCriterion("tx_from not between", value1, value2, "txFrom");
            return (Criteria) this;
        }

        public Criteria andContractIsNull() {
            addCriterion("contract is null");
            return (Criteria) this;
        }

        public Criteria andContractIsNotNull() {
            addCriterion("contract is not null");
            return (Criteria) this;
        }

        public Criteria andContractEqualTo(String value) {
            addCriterion("contract =", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractNotEqualTo(String value) {
            addCriterion("contract <>", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractGreaterThan(String value) {
            addCriterion("contract >", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractGreaterThanOrEqualTo(String value) {
            addCriterion("contract >=", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractLessThan(String value) {
            addCriterion("contract <", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractLessThanOrEqualTo(String value) {
            addCriterion("contract <=", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractLike(String value) {
            addCriterion("contract like", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractNotLike(String value) {
            addCriterion("contract not like", value, "contract");
            return (Criteria) this;
        }

        public Criteria andContractIn(List<String> values) {
            addCriterion("contract in", values, "contract");
            return (Criteria) this;
        }

        public Criteria andContractNotIn(List<String> values) {
            addCriterion("contract not in", values, "contract");
            return (Criteria) this;
        }

        public Criteria andContractBetween(String value1, String value2) {
            addCriterion("contract between", value1, value2, "contract");
            return (Criteria) this;
        }

        public Criteria andContractNotBetween(String value1, String value2) {
            addCriterion("contract not between", value1, value2, "contract");
            return (Criteria) this;
        }

        public Criteria andTransferToIsNull() {
            addCriterion("transfer_to is null");
            return (Criteria) this;
        }

        public Criteria andTransferToIsNotNull() {
            addCriterion("transfer_to is not null");
            return (Criteria) this;
        }

        public Criteria andTransferToEqualTo(String value) {
            addCriterion("transfer_to =", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToNotEqualTo(String value) {
            addCriterion("transfer_to <>", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToGreaterThan(String value) {
            addCriterion("transfer_to >", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToGreaterThanOrEqualTo(String value) {
            addCriterion("transfer_to >=", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToLessThan(String value) {
            addCriterion("transfer_to <", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToLessThanOrEqualTo(String value) {
            addCriterion("transfer_to <=", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToLike(String value) {
            addCriterion("transfer_to like", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToNotLike(String value) {
            addCriterion("transfer_to not like", value, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToIn(List<String> values) {
            addCriterion("transfer_to in", values, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToNotIn(List<String> values) {
            addCriterion("transfer_to not in", values, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToBetween(String value1, String value2) {
            addCriterion("transfer_to between", value1, value2, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferToNotBetween(String value1, String value2) {
            addCriterion("transfer_to not between", value1, value2, "transferTo");
            return (Criteria) this;
        }

        public Criteria andTransferValueIsNull() {
            addCriterion("transfer_value is null");
            return (Criteria) this;
        }

        public Criteria andTransferValueIsNotNull() {
            addCriterion("transfer_value is not null");
            return (Criteria) this;
        }

        public Criteria andTransferValueEqualTo(BigDecimal value) {
            addCriterion("transfer_value =", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueNotEqualTo(BigDecimal value) {
            addCriterion("transfer_value <>", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueGreaterThan(BigDecimal value) {
            addCriterion("transfer_value >", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("transfer_value >=", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueLessThan(BigDecimal value) {
            addCriterion("transfer_value <", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("transfer_value <=", value, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueIn(List<BigDecimal> values) {
            addCriterion("transfer_value in", values, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueNotIn(List<BigDecimal> values) {
            addCriterion("transfer_value not in", values, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("transfer_value between", value1, value2, "transferValue");
            return (Criteria) this;
        }

        public Criteria andTransferValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("transfer_value not between", value1, value2, "transferValue");
            return (Criteria) this;
        }

        public Criteria andDecimalIsNull() {
            addCriterion("`decimal` is null");
            return (Criteria) this;
        }

        public Criteria andDecimalIsNotNull() {
            addCriterion("`decimal` is not null");
            return (Criteria) this;
        }

        public Criteria andDecimalEqualTo(Integer value) {
            addCriterion("`decimal` =", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotEqualTo(Integer value) {
            addCriterion("`decimal` <>", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalGreaterThan(Integer value) {
            addCriterion("`decimal` >", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalGreaterThanOrEqualTo(Integer value) {
            addCriterion("`decimal` >=", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalLessThan(Integer value) {
            addCriterion("`decimal` <", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalLessThanOrEqualTo(Integer value) {
            addCriterion("`decimal` <=", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalIn(List<Integer> values) {
            addCriterion("`decimal` in", values, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotIn(List<Integer> values) {
            addCriterion("`decimal` not in", values, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalBetween(Integer value1, Integer value2) {
            addCriterion("`decimal` between", value1, value2, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotBetween(Integer value1, Integer value2) {
            addCriterion("`decimal` not between", value1, value2, "decimal");
            return (Criteria) this;
        }

        public Criteria andSymbolIsNull() {
            addCriterion("symbol is null");
            return (Criteria) this;
        }

        public Criteria andSymbolIsNotNull() {
            addCriterion("symbol is not null");
            return (Criteria) this;
        }

        public Criteria andSymbolEqualTo(String value) {
            addCriterion("symbol =", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotEqualTo(String value) {
            addCriterion("symbol <>", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolGreaterThan(String value) {
            addCriterion("symbol >", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolGreaterThanOrEqualTo(String value) {
            addCriterion("symbol >=", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLessThan(String value) {
            addCriterion("symbol <", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLessThanOrEqualTo(String value) {
            addCriterion("symbol <=", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLike(String value) {
            addCriterion("symbol like", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotLike(String value) {
            addCriterion("symbol not like", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolIn(List<String> values) {
            addCriterion("symbol in", values, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotIn(List<String> values) {
            addCriterion("symbol not in", values, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolBetween(String value1, String value2) {
            addCriterion("symbol between", value1, value2, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotBetween(String value1, String value2) {
            addCriterion("symbol not between", value1, value2, "symbol");
            return (Criteria) this;
        }

        public Criteria andMethodSignIsNull() {
            addCriterion("method_sign is null");
            return (Criteria) this;
        }

        public Criteria andMethodSignIsNotNull() {
            addCriterion("method_sign is not null");
            return (Criteria) this;
        }

        public Criteria andMethodSignEqualTo(String value) {
            addCriterion("method_sign =", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignNotEqualTo(String value) {
            addCriterion("method_sign <>", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignGreaterThan(String value) {
            addCriterion("method_sign >", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignGreaterThanOrEqualTo(String value) {
            addCriterion("method_sign >=", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignLessThan(String value) {
            addCriterion("method_sign <", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignLessThanOrEqualTo(String value) {
            addCriterion("method_sign <=", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignLike(String value) {
            addCriterion("method_sign like", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignNotLike(String value) {
            addCriterion("method_sign not like", value, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignIn(List<String> values) {
            addCriterion("method_sign in", values, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignNotIn(List<String> values) {
            addCriterion("method_sign not in", values, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignBetween(String value1, String value2) {
            addCriterion("method_sign between", value1, value2, "methodSign");
            return (Criteria) this;
        }

        public Criteria andMethodSignNotBetween(String value1, String value2) {
            addCriterion("method_sign not between", value1, value2, "methodSign");
            return (Criteria) this;
        }

        public Criteria andResultIsNull() {
            addCriterion("`result` is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("`result` is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(Integer value) {
            addCriterion("`result` =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(Integer value) {
            addCriterion("`result` <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(Integer value) {
            addCriterion("`result` >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("`result` >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(Integer value) {
            addCriterion("`result` <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(Integer value) {
            addCriterion("`result` <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<Integer> values) {
            addCriterion("`result` in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<Integer> values) {
            addCriterion("`result` not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(Integer value1, Integer value2) {
            addCriterion("`result` between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(Integer value1, Integer value2) {
            addCriterion("`result` not between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampIsNull() {
            addCriterion("block_timestamp is null");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampIsNotNull() {
            addCriterion("block_timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampEqualTo(Date value) {
            addCriterion("block_timestamp =", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampNotEqualTo(Date value) {
            addCriterion("block_timestamp <>", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampGreaterThan(Date value) {
            addCriterion("block_timestamp >", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("block_timestamp >=", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampLessThan(Date value) {
            addCriterion("block_timestamp <", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampLessThanOrEqualTo(Date value) {
            addCriterion("block_timestamp <=", value, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampIn(List<Date> values) {
            addCriterion("block_timestamp in", values, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampNotIn(List<Date> values) {
            addCriterion("block_timestamp not in", values, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampBetween(Date value1, Date value2) {
            addCriterion("block_timestamp between", value1, value2, "blockTimestamp");
            return (Criteria) this;
        }

        public Criteria andBlockTimestampNotBetween(Date value1, Date value2) {
            addCriterion("block_timestamp not between", value1, value2, "blockTimestamp");
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

        public Criteria andValueEqualTo(BigDecimal value) {
            addCriterion("`value` =", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotEqualTo(BigDecimal value) {
            addCriterion("`value` <>", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThan(BigDecimal value) {
            addCriterion("`value` >", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`value` >=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThan(BigDecimal value) {
            addCriterion("`value` <", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`value` <=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueIn(List<BigDecimal> values) {
            addCriterion("`value` in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotIn(List<BigDecimal> values) {
            addCriterion("`value` not in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`value` between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`value` not between", value1, value2, "value");
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