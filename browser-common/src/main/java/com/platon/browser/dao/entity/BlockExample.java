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

        public Criteria andStatTxQtyIsNull() {
            addCriterion("stat_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyIsNotNull() {
            addCriterion("stat_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyEqualTo(Integer value) {
            addCriterion("stat_tx_qty =", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyNotEqualTo(Integer value) {
            addCriterion("stat_tx_qty <>", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyGreaterThan(Integer value) {
            addCriterion("stat_tx_qty >", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_tx_qty >=", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyLessThan(Integer value) {
            addCriterion("stat_tx_qty <", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_tx_qty <=", value, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyIn(List<Integer> values) {
            addCriterion("stat_tx_qty in", values, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyNotIn(List<Integer> values) {
            addCriterion("stat_tx_qty not in", values, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_tx_qty between", value1, value2, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_tx_qty not between", value1, value2, "statTxQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyIsNull() {
            addCriterion("stat_transfer_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyIsNotNull() {
            addCriterion("stat_transfer_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyEqualTo(Integer value) {
            addCriterion("stat_transfer_qty =", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyNotEqualTo(Integer value) {
            addCriterion("stat_transfer_qty <>", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyGreaterThan(Integer value) {
            addCriterion("stat_transfer_qty >", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_transfer_qty >=", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyLessThan(Integer value) {
            addCriterion("stat_transfer_qty <", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_transfer_qty <=", value, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyIn(List<Integer> values) {
            addCriterion("stat_transfer_qty in", values, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyNotIn(List<Integer> values) {
            addCriterion("stat_transfer_qty not in", values, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_transfer_qty between", value1, value2, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatTransferQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_transfer_qty not between", value1, value2, "statTransferQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyIsNull() {
            addCriterion("stat_staking_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyIsNotNull() {
            addCriterion("stat_staking_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyEqualTo(Integer value) {
            addCriterion("stat_staking_qty =", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyNotEqualTo(Integer value) {
            addCriterion("stat_staking_qty <>", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyGreaterThan(Integer value) {
            addCriterion("stat_staking_qty >", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_staking_qty >=", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyLessThan(Integer value) {
            addCriterion("stat_staking_qty <", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_staking_qty <=", value, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyIn(List<Integer> values) {
            addCriterion("stat_staking_qty in", values, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyNotIn(List<Integer> values) {
            addCriterion("stat_staking_qty not in", values, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_staking_qty between", value1, value2, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatStakingQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_staking_qty not between", value1, value2, "statStakingQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyIsNull() {
            addCriterion("stat_proposal_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyIsNotNull() {
            addCriterion("stat_proposal_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyEqualTo(Integer value) {
            addCriterion("stat_proposal_qty =", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyNotEqualTo(Integer value) {
            addCriterion("stat_proposal_qty <>", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyGreaterThan(Integer value) {
            addCriterion("stat_proposal_qty >", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_proposal_qty >=", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyLessThan(Integer value) {
            addCriterion("stat_proposal_qty <", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_proposal_qty <=", value, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyIn(List<Integer> values) {
            addCriterion("stat_proposal_qty in", values, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyNotIn(List<Integer> values) {
            addCriterion("stat_proposal_qty not in", values, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_proposal_qty between", value1, value2, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatProposalQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_proposal_qty not between", value1, value2, "statProposalQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyIsNull() {
            addCriterion("stat_delegate_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyIsNotNull() {
            addCriterion("stat_delegate_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyEqualTo(Integer value) {
            addCriterion("stat_delegate_qty =", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyNotEqualTo(Integer value) {
            addCriterion("stat_delegate_qty <>", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyGreaterThan(Integer value) {
            addCriterion("stat_delegate_qty >", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_delegate_qty >=", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyLessThan(Integer value) {
            addCriterion("stat_delegate_qty <", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_delegate_qty <=", value, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyIn(List<Integer> values) {
            addCriterion("stat_delegate_qty in", values, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyNotIn(List<Integer> values) {
            addCriterion("stat_delegate_qty not in", values, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_delegate_qty between", value1, value2, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatDelegateQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_delegate_qty not between", value1, value2, "statDelegateQty");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitIsNull() {
            addCriterion("stat_tx_gas_limit is null");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitIsNotNull() {
            addCriterion("stat_tx_gas_limit is not null");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitEqualTo(String value) {
            addCriterion("stat_tx_gas_limit =", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitNotEqualTo(String value) {
            addCriterion("stat_tx_gas_limit <>", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitGreaterThan(String value) {
            addCriterion("stat_tx_gas_limit >", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitGreaterThanOrEqualTo(String value) {
            addCriterion("stat_tx_gas_limit >=", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitLessThan(String value) {
            addCriterion("stat_tx_gas_limit <", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitLessThanOrEqualTo(String value) {
            addCriterion("stat_tx_gas_limit <=", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitLike(String value) {
            addCriterion("stat_tx_gas_limit like", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitNotLike(String value) {
            addCriterion("stat_tx_gas_limit not like", value, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitIn(List<String> values) {
            addCriterion("stat_tx_gas_limit in", values, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitNotIn(List<String> values) {
            addCriterion("stat_tx_gas_limit not in", values, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitBetween(String value1, String value2) {
            addCriterion("stat_tx_gas_limit between", value1, value2, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxGasLimitNotBetween(String value1, String value2) {
            addCriterion("stat_tx_gas_limit not between", value1, value2, "statTxGasLimit");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeIsNull() {
            addCriterion("stat_tx_fee is null");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeIsNotNull() {
            addCriterion("stat_tx_fee is not null");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeEqualTo(String value) {
            addCriterion("stat_tx_fee =", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeNotEqualTo(String value) {
            addCriterion("stat_tx_fee <>", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeGreaterThan(String value) {
            addCriterion("stat_tx_fee >", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeGreaterThanOrEqualTo(String value) {
            addCriterion("stat_tx_fee >=", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeLessThan(String value) {
            addCriterion("stat_tx_fee <", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeLessThanOrEqualTo(String value) {
            addCriterion("stat_tx_fee <=", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeLike(String value) {
            addCriterion("stat_tx_fee like", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeNotLike(String value) {
            addCriterion("stat_tx_fee not like", value, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeIn(List<String> values) {
            addCriterion("stat_tx_fee in", values, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeNotIn(List<String> values) {
            addCriterion("stat_tx_fee not in", values, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeBetween(String value1, String value2) {
            addCriterion("stat_tx_fee between", value1, value2, "statTxFee");
            return (Criteria) this;
        }

        public Criteria andStatTxFeeNotBetween(String value1, String value2) {
            addCriterion("stat_tx_fee not between", value1, value2, "statTxFee");
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