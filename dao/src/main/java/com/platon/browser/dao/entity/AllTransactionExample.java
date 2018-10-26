package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllTransactionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AllTransactionExample() {
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

        public Criteria andBlockTimeIsNull() {
            addCriterion("block_time is null");
            return (Criteria) this;
        }

        public Criteria andBlockTimeIsNotNull() {
            addCriterion("block_time is not null");
            return (Criteria) this;
        }

        public Criteria andBlockTimeEqualTo(Date value) {
            addCriterion("block_time =", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotEqualTo(Date value) {
            addCriterion("block_time <>", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeGreaterThan(Date value) {
            addCriterion("block_time >", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("block_time >=", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeLessThan(Date value) {
            addCriterion("block_time <", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeLessThanOrEqualTo(Date value) {
            addCriterion("block_time <=", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeIn(List<Date> values) {
            addCriterion("block_time in", values, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotIn(List<Date> values) {
            addCriterion("block_time not in", values, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeBetween(Date value1, Date value2) {
            addCriterion("block_time between", value1, value2, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotBetween(Date value1, Date value2) {
            addCriterion("block_time not between", value1, value2, "blockTime");
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

        public Criteria andEnergonUsedIsNull() {
            addCriterion("energon_used is null");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedIsNotNull() {
            addCriterion("energon_used is not null");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedEqualTo(Integer value) {
            addCriterion("energon_used =", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotEqualTo(Integer value) {
            addCriterion("energon_used <>", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedGreaterThan(Integer value) {
            addCriterion("energon_used >", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedGreaterThanOrEqualTo(Integer value) {
            addCriterion("energon_used >=", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedLessThan(Integer value) {
            addCriterion("energon_used <", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedLessThanOrEqualTo(Integer value) {
            addCriterion("energon_used <=", value, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedIn(List<Integer> values) {
            addCriterion("energon_used in", values, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotIn(List<Integer> values) {
            addCriterion("energon_used not in", values, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedBetween(Integer value1, Integer value2) {
            addCriterion("energon_used between", value1, value2, "energonUsed");
            return (Criteria) this;
        }

        public Criteria andEnergonUsedNotBetween(Integer value1, Integer value2) {
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

        public Criteria andEnergonLimitEqualTo(Integer value) {
            addCriterion("energon_limit =", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotEqualTo(Integer value) {
            addCriterion("energon_limit <>", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitGreaterThan(Integer value) {
            addCriterion("energon_limit >", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("energon_limit >=", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitLessThan(Integer value) {
            addCriterion("energon_limit <", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitLessThanOrEqualTo(Integer value) {
            addCriterion("energon_limit <=", value, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitIn(List<Integer> values) {
            addCriterion("energon_limit in", values, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotIn(List<Integer> values) {
            addCriterion("energon_limit not in", values, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitBetween(Integer value1, Integer value2) {
            addCriterion("energon_limit between", value1, value2, "energonLimit");
            return (Criteria) this;
        }

        public Criteria andEnergonLimitNotBetween(Integer value1, Integer value2) {
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

        public Criteria andTransactionIndexIsNull() {
            addCriterion("transaction_index is null");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexIsNotNull() {
            addCriterion("transaction_index is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexEqualTo(Long value) {
            addCriterion("transaction_index =", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotEqualTo(Long value) {
            addCriterion("transaction_index <>", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexGreaterThan(Long value) {
            addCriterion("transaction_index >", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexGreaterThanOrEqualTo(Long value) {
            addCriterion("transaction_index >=", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexLessThan(Long value) {
            addCriterion("transaction_index <", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexLessThanOrEqualTo(Long value) {
            addCriterion("transaction_index <=", value, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexIn(List<Long> values) {
            addCriterion("transaction_index in", values, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotIn(List<Long> values) {
            addCriterion("transaction_index not in", values, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexBetween(Long value1, Long value2) {
            addCriterion("transaction_index between", value1, value2, "transactionIndex");
            return (Criteria) this;
        }

        public Criteria andTransactionIndexNotBetween(Long value1, Long value2) {
            addCriterion("transaction_index not between", value1, value2, "transactionIndex");
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

        public Criteria andTxReceiptStatusIsNull() {
            addCriterion("tx_receipt_status is null");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusIsNotNull() {
            addCriterion("tx_receipt_status is not null");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusEqualTo(Long value) {
            addCriterion("tx_receipt_status =", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotEqualTo(Long value) {
            addCriterion("tx_receipt_status <>", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusGreaterThan(Long value) {
            addCriterion("tx_receipt_status >", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusGreaterThanOrEqualTo(Long value) {
            addCriterion("tx_receipt_status >=", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusLessThan(Long value) {
            addCriterion("tx_receipt_status <", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusLessThanOrEqualTo(Long value) {
            addCriterion("tx_receipt_status <=", value, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusIn(List<Long> values) {
            addCriterion("tx_receipt_status in", values, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotIn(List<Long> values) {
            addCriterion("tx_receipt_status not in", values, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusBetween(Long value1, Long value2) {
            addCriterion("tx_receipt_status between", value1, value2, "txReceiptStatus");
            return (Criteria) this;
        }

        public Criteria andTxReceiptStatusNotBetween(Long value1, Long value2) {
            addCriterion("tx_receipt_status not between", value1, value2, "txReceiptStatus");
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

        public Criteria andServerTimeIsNull() {
            addCriterion("server_time is null");
            return (Criteria) this;
        }

        public Criteria andServerTimeIsNotNull() {
            addCriterion("server_time is not null");
            return (Criteria) this;
        }

        public Criteria andServerTimeEqualTo(Date value) {
            addCriterion("server_time =", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeNotEqualTo(Date value) {
            addCriterion("server_time <>", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeGreaterThan(Date value) {
            addCriterion("server_time >", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("server_time >=", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeLessThan(Date value) {
            addCriterion("server_time <", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeLessThanOrEqualTo(Date value) {
            addCriterion("server_time <=", value, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeIn(List<Date> values) {
            addCriterion("server_time in", values, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeNotIn(List<Date> values) {
            addCriterion("server_time not in", values, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeBetween(Date value1, Date value2) {
            addCriterion("server_time between", value1, value2, "serverTime");
            return (Criteria) this;
        }

        public Criteria andServerTimeNotBetween(Date value1, Date value2) {
            addCriterion("server_time not between", value1, value2, "serverTime");
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