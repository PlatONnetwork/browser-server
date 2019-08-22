package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StakingExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StakingExample() {
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

        public Criteria andStakingBlockNumIsNull() {
            addCriterion("staking_block_num is null");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumIsNotNull() {
            addCriterion("staking_block_num is not null");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumEqualTo(Long value) {
            addCriterion("staking_block_num =", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumNotEqualTo(Long value) {
            addCriterion("staking_block_num <>", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumGreaterThan(Long value) {
            addCriterion("staking_block_num >", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumGreaterThanOrEqualTo(Long value) {
            addCriterion("staking_block_num >=", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumLessThan(Long value) {
            addCriterion("staking_block_num <", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumLessThanOrEqualTo(Long value) {
            addCriterion("staking_block_num <=", value, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumIn(List<Long> values) {
            addCriterion("staking_block_num in", values, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumNotIn(List<Long> values) {
            addCriterion("staking_block_num not in", values, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumBetween(Long value1, Long value2) {
            addCriterion("staking_block_num between", value1, value2, "stakingBlockNum");
            return (Criteria) this;
        }

        public Criteria andStakingBlockNumNotBetween(Long value1, Long value2) {
            addCriterion("staking_block_num not between", value1, value2, "stakingBlockNum");
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

        public Criteria andStakingTxIndexIsNull() {
            addCriterion("staking_tx_index is null");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexIsNotNull() {
            addCriterion("staking_tx_index is not null");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexEqualTo(Integer value) {
            addCriterion("staking_tx_index =", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexNotEqualTo(Integer value) {
            addCriterion("staking_tx_index <>", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexGreaterThan(Integer value) {
            addCriterion("staking_tx_index >", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("staking_tx_index >=", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexLessThan(Integer value) {
            addCriterion("staking_tx_index <", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexLessThanOrEqualTo(Integer value) {
            addCriterion("staking_tx_index <=", value, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexIn(List<Integer> values) {
            addCriterion("staking_tx_index in", values, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexNotIn(List<Integer> values) {
            addCriterion("staking_tx_index not in", values, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexBetween(Integer value1, Integer value2) {
            addCriterion("staking_tx_index between", value1, value2, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingTxIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("staking_tx_index not between", value1, value2, "stakingTxIndex");
            return (Criteria) this;
        }

        public Criteria andStakingAddrIsNull() {
            addCriterion("staking_addr is null");
            return (Criteria) this;
        }

        public Criteria andStakingAddrIsNotNull() {
            addCriterion("staking_addr is not null");
            return (Criteria) this;
        }

        public Criteria andStakingAddrEqualTo(String value) {
            addCriterion("staking_addr =", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrNotEqualTo(String value) {
            addCriterion("staking_addr <>", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrGreaterThan(String value) {
            addCriterion("staking_addr >", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrGreaterThanOrEqualTo(String value) {
            addCriterion("staking_addr >=", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrLessThan(String value) {
            addCriterion("staking_addr <", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrLessThanOrEqualTo(String value) {
            addCriterion("staking_addr <=", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrLike(String value) {
            addCriterion("staking_addr like", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrNotLike(String value) {
            addCriterion("staking_addr not like", value, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrIn(List<String> values) {
            addCriterion("staking_addr in", values, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrNotIn(List<String> values) {
            addCriterion("staking_addr not in", values, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrBetween(String value1, String value2) {
            addCriterion("staking_addr between", value1, value2, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingAddrNotBetween(String value1, String value2) {
            addCriterion("staking_addr not between", value1, value2, "stakingAddr");
            return (Criteria) this;
        }

        public Criteria andStakingHasIsNull() {
            addCriterion("staking_has is null");
            return (Criteria) this;
        }

        public Criteria andStakingHasIsNotNull() {
            addCriterion("staking_has is not null");
            return (Criteria) this;
        }

        public Criteria andStakingHasEqualTo(String value) {
            addCriterion("staking_has =", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasNotEqualTo(String value) {
            addCriterion("staking_has <>", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasGreaterThan(String value) {
            addCriterion("staking_has >", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasGreaterThanOrEqualTo(String value) {
            addCriterion("staking_has >=", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasLessThan(String value) {
            addCriterion("staking_has <", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasLessThanOrEqualTo(String value) {
            addCriterion("staking_has <=", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasLike(String value) {
            addCriterion("staking_has like", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasNotLike(String value) {
            addCriterion("staking_has not like", value, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasIn(List<String> values) {
            addCriterion("staking_has in", values, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasNotIn(List<String> values) {
            addCriterion("staking_has not in", values, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasBetween(String value1, String value2) {
            addCriterion("staking_has between", value1, value2, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingHasNotBetween(String value1, String value2) {
            addCriterion("staking_has not between", value1, value2, "stakingHas");
            return (Criteria) this;
        }

        public Criteria andStakingLockedIsNull() {
            addCriterion("staking_locked is null");
            return (Criteria) this;
        }

        public Criteria andStakingLockedIsNotNull() {
            addCriterion("staking_locked is not null");
            return (Criteria) this;
        }

        public Criteria andStakingLockedEqualTo(String value) {
            addCriterion("staking_locked =", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotEqualTo(String value) {
            addCriterion("staking_locked <>", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedGreaterThan(String value) {
            addCriterion("staking_locked >", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedGreaterThanOrEqualTo(String value) {
            addCriterion("staking_locked >=", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedLessThan(String value) {
            addCriterion("staking_locked <", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedLessThanOrEqualTo(String value) {
            addCriterion("staking_locked <=", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedLike(String value) {
            addCriterion("staking_locked like", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotLike(String value) {
            addCriterion("staking_locked not like", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedIn(List<String> values) {
            addCriterion("staking_locked in", values, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotIn(List<String> values) {
            addCriterion("staking_locked not in", values, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedBetween(String value1, String value2) {
            addCriterion("staking_locked between", value1, value2, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotBetween(String value1, String value2) {
            addCriterion("staking_locked not between", value1, value2, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochIsNull() {
            addCriterion("staking_reduction_epoch is null");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochIsNotNull() {
            addCriterion("staking_reduction_epoch is not null");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochEqualTo(Integer value) {
            addCriterion("staking_reduction_epoch =", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochNotEqualTo(Integer value) {
            addCriterion("staking_reduction_epoch <>", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochGreaterThan(Integer value) {
            addCriterion("staking_reduction_epoch >", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochGreaterThanOrEqualTo(Integer value) {
            addCriterion("staking_reduction_epoch >=", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochLessThan(Integer value) {
            addCriterion("staking_reduction_epoch <", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochLessThanOrEqualTo(Integer value) {
            addCriterion("staking_reduction_epoch <=", value, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochIn(List<Integer> values) {
            addCriterion("staking_reduction_epoch in", values, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochNotIn(List<Integer> values) {
            addCriterion("staking_reduction_epoch not in", values, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochBetween(Integer value1, Integer value2) {
            addCriterion("staking_reduction_epoch between", value1, value2, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEpochNotBetween(Integer value1, Integer value2) {
            addCriterion("staking_reduction_epoch not between", value1, value2, "stakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andStakingReductionIsNull() {
            addCriterion("staking_reduction is null");
            return (Criteria) this;
        }

        public Criteria andStakingReductionIsNotNull() {
            addCriterion("staking_reduction is not null");
            return (Criteria) this;
        }

        public Criteria andStakingReductionEqualTo(String value) {
            addCriterion("staking_reduction =", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotEqualTo(String value) {
            addCriterion("staking_reduction <>", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionGreaterThan(String value) {
            addCriterion("staking_reduction >", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionGreaterThanOrEqualTo(String value) {
            addCriterion("staking_reduction >=", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionLessThan(String value) {
            addCriterion("staking_reduction <", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionLessThanOrEqualTo(String value) {
            addCriterion("staking_reduction <=", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionLike(String value) {
            addCriterion("staking_reduction like", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotLike(String value) {
            addCriterion("staking_reduction not like", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionIn(List<String> values) {
            addCriterion("staking_reduction in", values, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotIn(List<String> values) {
            addCriterion("staking_reduction not in", values, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionBetween(String value1, String value2) {
            addCriterion("staking_reduction between", value1, value2, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotBetween(String value1, String value2) {
            addCriterion("staking_reduction not between", value1, value2, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasIsNull() {
            addCriterion("stat_delegate_has is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasIsNotNull() {
            addCriterion("stat_delegate_has is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasEqualTo(String value) {
            addCriterion("stat_delegate_has =", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasNotEqualTo(String value) {
            addCriterion("stat_delegate_has <>", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasGreaterThan(String value) {
            addCriterion("stat_delegate_has >", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasGreaterThanOrEqualTo(String value) {
            addCriterion("stat_delegate_has >=", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasLessThan(String value) {
            addCriterion("stat_delegate_has <", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasLessThanOrEqualTo(String value) {
            addCriterion("stat_delegate_has <=", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasLike(String value) {
            addCriterion("stat_delegate_has like", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasNotLike(String value) {
            addCriterion("stat_delegate_has not like", value, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasIn(List<String> values) {
            addCriterion("stat_delegate_has in", values, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasNotIn(List<String> values) {
            addCriterion("stat_delegate_has not in", values, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasBetween(String value1, String value2) {
            addCriterion("stat_delegate_has between", value1, value2, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHasNotBetween(String value1, String value2) {
            addCriterion("stat_delegate_has not between", value1, value2, "statDelegateHas");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedIsNull() {
            addCriterion("stat_delegate_locked is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedIsNotNull() {
            addCriterion("stat_delegate_locked is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedEqualTo(String value) {
            addCriterion("stat_delegate_locked =", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotEqualTo(String value) {
            addCriterion("stat_delegate_locked <>", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedGreaterThan(String value) {
            addCriterion("stat_delegate_locked >", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedGreaterThanOrEqualTo(String value) {
            addCriterion("stat_delegate_locked >=", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedLessThan(String value) {
            addCriterion("stat_delegate_locked <", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedLessThanOrEqualTo(String value) {
            addCriterion("stat_delegate_locked <=", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedLike(String value) {
            addCriterion("stat_delegate_locked like", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotLike(String value) {
            addCriterion("stat_delegate_locked not like", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedIn(List<String> values) {
            addCriterion("stat_delegate_locked in", values, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotIn(List<String> values) {
            addCriterion("stat_delegate_locked not in", values, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedBetween(String value1, String value2) {
            addCriterion("stat_delegate_locked between", value1, value2, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotBetween(String value1, String value2) {
            addCriterion("stat_delegate_locked not between", value1, value2, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionIsNull() {
            addCriterion("stat_delegate_reduction is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionIsNotNull() {
            addCriterion("stat_delegate_reduction is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionEqualTo(String value) {
            addCriterion("stat_delegate_reduction =", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionNotEqualTo(String value) {
            addCriterion("stat_delegate_reduction <>", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionGreaterThan(String value) {
            addCriterion("stat_delegate_reduction >", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionGreaterThanOrEqualTo(String value) {
            addCriterion("stat_delegate_reduction >=", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionLessThan(String value) {
            addCriterion("stat_delegate_reduction <", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionLessThanOrEqualTo(String value) {
            addCriterion("stat_delegate_reduction <=", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionLike(String value) {
            addCriterion("stat_delegate_reduction like", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionNotLike(String value) {
            addCriterion("stat_delegate_reduction not like", value, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionIn(List<String> values) {
            addCriterion("stat_delegate_reduction in", values, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionNotIn(List<String> values) {
            addCriterion("stat_delegate_reduction not in", values, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionBetween(String value1, String value2) {
            addCriterion("stat_delegate_reduction between", value1, value2, "statDelegateReduction");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReductionNotBetween(String value1, String value2) {
            addCriterion("stat_delegate_reduction not between", value1, value2, "statDelegateReduction");
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

        public Criteria andStatVerifierTimeIsNull() {
            addCriterion("stat_verifier_time is null");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeIsNotNull() {
            addCriterion("stat_verifier_time is not null");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeEqualTo(Integer value) {
            addCriterion("stat_verifier_time =", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotEqualTo(Integer value) {
            addCriterion("stat_verifier_time <>", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeGreaterThan(Integer value) {
            addCriterion("stat_verifier_time >", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_verifier_time >=", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeLessThan(Integer value) {
            addCriterion("stat_verifier_time <", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeLessThanOrEqualTo(Integer value) {
            addCriterion("stat_verifier_time <=", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeIn(List<Integer> values) {
            addCriterion("stat_verifier_time in", values, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotIn(List<Integer> values) {
            addCriterion("stat_verifier_time not in", values, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeBetween(Integer value1, Integer value2) {
            addCriterion("stat_verifier_time between", value1, value2, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_verifier_time not between", value1, value2, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStakingNameIsNull() {
            addCriterion("staking_name is null");
            return (Criteria) this;
        }

        public Criteria andStakingNameIsNotNull() {
            addCriterion("staking_name is not null");
            return (Criteria) this;
        }

        public Criteria andStakingNameEqualTo(String value) {
            addCriterion("staking_name =", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameNotEqualTo(String value) {
            addCriterion("staking_name <>", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameGreaterThan(String value) {
            addCriterion("staking_name >", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameGreaterThanOrEqualTo(String value) {
            addCriterion("staking_name >=", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameLessThan(String value) {
            addCriterion("staking_name <", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameLessThanOrEqualTo(String value) {
            addCriterion("staking_name <=", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameLike(String value) {
            addCriterion("staking_name like", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameNotLike(String value) {
            addCriterion("staking_name not like", value, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameIn(List<String> values) {
            addCriterion("staking_name in", values, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameNotIn(List<String> values) {
            addCriterion("staking_name not in", values, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameBetween(String value1, String value2) {
            addCriterion("staking_name between", value1, value2, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingNameNotBetween(String value1, String value2) {
            addCriterion("staking_name not between", value1, value2, "stakingName");
            return (Criteria) this;
        }

        public Criteria andStakingIconIsNull() {
            addCriterion("staking_icon is null");
            return (Criteria) this;
        }

        public Criteria andStakingIconIsNotNull() {
            addCriterion("staking_icon is not null");
            return (Criteria) this;
        }

        public Criteria andStakingIconEqualTo(String value) {
            addCriterion("staking_icon =", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconNotEqualTo(String value) {
            addCriterion("staking_icon <>", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconGreaterThan(String value) {
            addCriterion("staking_icon >", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconGreaterThanOrEqualTo(String value) {
            addCriterion("staking_icon >=", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconLessThan(String value) {
            addCriterion("staking_icon <", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconLessThanOrEqualTo(String value) {
            addCriterion("staking_icon <=", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconLike(String value) {
            addCriterion("staking_icon like", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconNotLike(String value) {
            addCriterion("staking_icon not like", value, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconIn(List<String> values) {
            addCriterion("staking_icon in", values, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconNotIn(List<String> values) {
            addCriterion("staking_icon not in", values, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconBetween(String value1, String value2) {
            addCriterion("staking_icon between", value1, value2, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andStakingIconNotBetween(String value1, String value2) {
            addCriterion("staking_icon not between", value1, value2, "stakingIcon");
            return (Criteria) this;
        }

        public Criteria andExternalIdIsNull() {
            addCriterion("external_id is null");
            return (Criteria) this;
        }

        public Criteria andExternalIdIsNotNull() {
            addCriterion("external_id is not null");
            return (Criteria) this;
        }

        public Criteria andExternalIdEqualTo(String value) {
            addCriterion("external_id =", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdNotEqualTo(String value) {
            addCriterion("external_id <>", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdGreaterThan(String value) {
            addCriterion("external_id >", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdGreaterThanOrEqualTo(String value) {
            addCriterion("external_id >=", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdLessThan(String value) {
            addCriterion("external_id <", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdLessThanOrEqualTo(String value) {
            addCriterion("external_id <=", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdLike(String value) {
            addCriterion("external_id like", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdNotLike(String value) {
            addCriterion("external_id not like", value, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdIn(List<String> values) {
            addCriterion("external_id in", values, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdNotIn(List<String> values) {
            addCriterion("external_id not in", values, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdBetween(String value1, String value2) {
            addCriterion("external_id between", value1, value2, "externalId");
            return (Criteria) this;
        }

        public Criteria andExternalIdNotBetween(String value1, String value2) {
            addCriterion("external_id not between", value1, value2, "externalId");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrIsNull() {
            addCriterion("denefit_addr is null");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrIsNotNull() {
            addCriterion("denefit_addr is not null");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrEqualTo(String value) {
            addCriterion("denefit_addr =", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrNotEqualTo(String value) {
            addCriterion("denefit_addr <>", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrGreaterThan(String value) {
            addCriterion("denefit_addr >", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrGreaterThanOrEqualTo(String value) {
            addCriterion("denefit_addr >=", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrLessThan(String value) {
            addCriterion("denefit_addr <", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrLessThanOrEqualTo(String value) {
            addCriterion("denefit_addr <=", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrLike(String value) {
            addCriterion("denefit_addr like", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrNotLike(String value) {
            addCriterion("denefit_addr not like", value, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrIn(List<String> values) {
            addCriterion("denefit_addr in", values, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrNotIn(List<String> values) {
            addCriterion("denefit_addr not in", values, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrBetween(String value1, String value2) {
            addCriterion("denefit_addr between", value1, value2, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andDenefitAddrNotBetween(String value1, String value2) {
            addCriterion("denefit_addr not between", value1, value2, "denefitAddr");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeIsNull() {
            addCriterion("expected_income is null");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeIsNotNull() {
            addCriterion("expected_income is not null");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeEqualTo(String value) {
            addCriterion("expected_income =", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeNotEqualTo(String value) {
            addCriterion("expected_income <>", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeGreaterThan(String value) {
            addCriterion("expected_income >", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeGreaterThanOrEqualTo(String value) {
            addCriterion("expected_income >=", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeLessThan(String value) {
            addCriterion("expected_income <", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeLessThanOrEqualTo(String value) {
            addCriterion("expected_income <=", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeLike(String value) {
            addCriterion("expected_income like", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeNotLike(String value) {
            addCriterion("expected_income not like", value, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeIn(List<String> values) {
            addCriterion("expected_income in", values, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeNotIn(List<String> values) {
            addCriterion("expected_income not in", values, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeBetween(String value1, String value2) {
            addCriterion("expected_income between", value1, value2, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andExpectedIncomeNotBetween(String value1, String value2) {
            addCriterion("expected_income not between", value1, value2, "expectedIncome");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueIsNull() {
            addCriterion("block_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueIsNotNull() {
            addCriterion("block_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueEqualTo(String value) {
            addCriterion("block_reward_value =", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotEqualTo(String value) {
            addCriterion("block_reward_value <>", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueGreaterThan(String value) {
            addCriterion("block_reward_value >", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueGreaterThanOrEqualTo(String value) {
            addCriterion("block_reward_value >=", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueLessThan(String value) {
            addCriterion("block_reward_value <", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueLessThanOrEqualTo(String value) {
            addCriterion("block_reward_value <=", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueLike(String value) {
            addCriterion("block_reward_value like", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotLike(String value) {
            addCriterion("block_reward_value not like", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueIn(List<String> values) {
            addCriterion("block_reward_value in", values, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotIn(List<String> values) {
            addCriterion("block_reward_value not in", values, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueBetween(String value1, String value2) {
            addCriterion("block_reward_value between", value1, value2, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotBetween(String value1, String value2) {
            addCriterion("block_reward_value not between", value1, value2, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyIsNull() {
            addCriterion("pre_cons_block_qty is null");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyIsNotNull() {
            addCriterion("pre_cons_block_qty is not null");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyEqualTo(Long value) {
            addCriterion("pre_cons_block_qty =", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyNotEqualTo(Long value) {
            addCriterion("pre_cons_block_qty <>", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyGreaterThan(Long value) {
            addCriterion("pre_cons_block_qty >", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyGreaterThanOrEqualTo(Long value) {
            addCriterion("pre_cons_block_qty >=", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyLessThan(Long value) {
            addCriterion("pre_cons_block_qty <", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyLessThanOrEqualTo(Long value) {
            addCriterion("pre_cons_block_qty <=", value, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyIn(List<Long> values) {
            addCriterion("pre_cons_block_qty in", values, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyNotIn(List<Long> values) {
            addCriterion("pre_cons_block_qty not in", values, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyBetween(Long value1, Long value2) {
            addCriterion("pre_cons_block_qty between", value1, value2, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andPreConsBlockQtyNotBetween(Long value1, Long value2) {
            addCriterion("pre_cons_block_qty not between", value1, value2, "preConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyIsNull() {
            addCriterion("cur_cons_block_qty is null");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyIsNotNull() {
            addCriterion("cur_cons_block_qty is not null");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyEqualTo(Long value) {
            addCriterion("cur_cons_block_qty =", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyNotEqualTo(Long value) {
            addCriterion("cur_cons_block_qty <>", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyGreaterThan(Long value) {
            addCriterion("cur_cons_block_qty >", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyGreaterThanOrEqualTo(Long value) {
            addCriterion("cur_cons_block_qty >=", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyLessThan(Long value) {
            addCriterion("cur_cons_block_qty <", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyLessThanOrEqualTo(Long value) {
            addCriterion("cur_cons_block_qty <=", value, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyIn(List<Long> values) {
            addCriterion("cur_cons_block_qty in", values, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyNotIn(List<Long> values) {
            addCriterion("cur_cons_block_qty not in", values, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyBetween(Long value1, Long value2) {
            addCriterion("cur_cons_block_qty between", value1, value2, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andCurConsBlockQtyNotBetween(Long value1, Long value2) {
            addCriterion("cur_cons_block_qty not between", value1, value2, "curConsBlockQty");
            return (Criteria) this;
        }

        public Criteria andProgramVersionIsNull() {
            addCriterion("program_version is null");
            return (Criteria) this;
        }

        public Criteria andProgramVersionIsNotNull() {
            addCriterion("program_version is not null");
            return (Criteria) this;
        }

        public Criteria andProgramVersionEqualTo(String value) {
            addCriterion("program_version =", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionNotEqualTo(String value) {
            addCriterion("program_version <>", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionGreaterThan(String value) {
            addCriterion("program_version >", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionGreaterThanOrEqualTo(String value) {
            addCriterion("program_version >=", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionLessThan(String value) {
            addCriterion("program_version <", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionLessThanOrEqualTo(String value) {
            addCriterion("program_version <=", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionLike(String value) {
            addCriterion("program_version like", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionNotLike(String value) {
            addCriterion("program_version not like", value, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionIn(List<String> values) {
            addCriterion("program_version in", values, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionNotIn(List<String> values) {
            addCriterion("program_version not in", values, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionBetween(String value1, String value2) {
            addCriterion("program_version between", value1, value2, "programVersion");
            return (Criteria) this;
        }

        public Criteria andProgramVersionNotBetween(String value1, String value2) {
            addCriterion("program_version not between", value1, value2, "programVersion");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueIsNull() {
            addCriterion("staking_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueIsNotNull() {
            addCriterion("staking_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueEqualTo(String value) {
            addCriterion("staking_reward_value =", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotEqualTo(String value) {
            addCriterion("staking_reward_value <>", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueGreaterThan(String value) {
            addCriterion("staking_reward_value >", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueGreaterThanOrEqualTo(String value) {
            addCriterion("staking_reward_value >=", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueLessThan(String value) {
            addCriterion("staking_reward_value <", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueLessThanOrEqualTo(String value) {
            addCriterion("staking_reward_value <=", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueLike(String value) {
            addCriterion("staking_reward_value like", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotLike(String value) {
            addCriterion("staking_reward_value not like", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueIn(List<String> values) {
            addCriterion("staking_reward_value in", values, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotIn(List<String> values) {
            addCriterion("staking_reward_value not in", values, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueBetween(String value1, String value2) {
            addCriterion("staking_reward_value between", value1, value2, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotBetween(String value1, String value2) {
            addCriterion("staking_reward_value not between", value1, value2, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andWebSiteIsNull() {
            addCriterion("web_site is null");
            return (Criteria) this;
        }

        public Criteria andWebSiteIsNotNull() {
            addCriterion("web_site is not null");
            return (Criteria) this;
        }

        public Criteria andWebSiteEqualTo(String value) {
            addCriterion("web_site =", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteNotEqualTo(String value) {
            addCriterion("web_site <>", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteGreaterThan(String value) {
            addCriterion("web_site >", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteGreaterThanOrEqualTo(String value) {
            addCriterion("web_site >=", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteLessThan(String value) {
            addCriterion("web_site <", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteLessThanOrEqualTo(String value) {
            addCriterion("web_site <=", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteLike(String value) {
            addCriterion("web_site like", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteNotLike(String value) {
            addCriterion("web_site not like", value, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteIn(List<String> values) {
            addCriterion("web_site in", values, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteNotIn(List<String> values) {
            addCriterion("web_site not in", values, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteBetween(String value1, String value2) {
            addCriterion("web_site between", value1, value2, "webSite");
            return (Criteria) this;
        }

        public Criteria andWebSiteNotBetween(String value1, String value2) {
            addCriterion("web_site not between", value1, value2, "webSite");
            return (Criteria) this;
        }

        public Criteria andDetailsIsNull() {
            addCriterion("details is null");
            return (Criteria) this;
        }

        public Criteria andDetailsIsNotNull() {
            addCriterion("details is not null");
            return (Criteria) this;
        }

        public Criteria andDetailsEqualTo(String value) {
            addCriterion("details =", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsNotEqualTo(String value) {
            addCriterion("details <>", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsGreaterThan(String value) {
            addCriterion("details >", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsGreaterThanOrEqualTo(String value) {
            addCriterion("details >=", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsLessThan(String value) {
            addCriterion("details <", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsLessThanOrEqualTo(String value) {
            addCriterion("details <=", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsLike(String value) {
            addCriterion("details like", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsNotLike(String value) {
            addCriterion("details not like", value, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsIn(List<String> values) {
            addCriterion("details in", values, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsNotIn(List<String> values) {
            addCriterion("details not in", values, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsBetween(String value1, String value2) {
            addCriterion("details between", value1, value2, "details");
            return (Criteria) this;
        }

        public Criteria andDetailsNotBetween(String value1, String value2) {
            addCriterion("details not between", value1, value2, "details");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIsNull() {
            addCriterion("join_time is null");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIsNotNull() {
            addCriterion("join_time is not null");
            return (Criteria) this;
        }

        public Criteria andJoinTimeEqualTo(Date value) {
            addCriterion("join_time =", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotEqualTo(Date value) {
            addCriterion("join_time <>", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeGreaterThan(Date value) {
            addCriterion("join_time >", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("join_time >=", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeLessThan(Date value) {
            addCriterion("join_time <", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeLessThanOrEqualTo(Date value) {
            addCriterion("join_time <=", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIn(List<Date> values) {
            addCriterion("join_time in", values, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotIn(List<Date> values) {
            addCriterion("join_time not in", values, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeBetween(Date value1, Date value2) {
            addCriterion("join_time between", value1, value2, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotBetween(Date value1, Date value2) {
            addCriterion("join_time not between", value1, value2, "joinTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeIsNull() {
            addCriterion("leave_time is null");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeIsNotNull() {
            addCriterion("leave_time is not null");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeEqualTo(Date value) {
            addCriterion("leave_time =", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeNotEqualTo(Date value) {
            addCriterion("leave_time <>", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeGreaterThan(Date value) {
            addCriterion("leave_time >", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("leave_time >=", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeLessThan(Date value) {
            addCriterion("leave_time <", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeLessThanOrEqualTo(Date value) {
            addCriterion("leave_time <=", value, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeIn(List<Date> values) {
            addCriterion("leave_time in", values, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeNotIn(List<Date> values) {
            addCriterion("leave_time not in", values, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeBetween(Date value1, Date value2) {
            addCriterion("leave_time between", value1, value2, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andLeaveTimeNotBetween(Date value1, Date value2) {
            addCriterion("leave_time not between", value1, value2, "leaveTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andIsConsensusIsNull() {
            addCriterion("is_consensus is null");
            return (Criteria) this;
        }

        public Criteria andIsConsensusIsNotNull() {
            addCriterion("is_consensus is not null");
            return (Criteria) this;
        }

        public Criteria andIsConsensusEqualTo(Integer value) {
            addCriterion("is_consensus =", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusNotEqualTo(Integer value) {
            addCriterion("is_consensus <>", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusGreaterThan(Integer value) {
            addCriterion("is_consensus >", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_consensus >=", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusLessThan(Integer value) {
            addCriterion("is_consensus <", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusLessThanOrEqualTo(Integer value) {
            addCriterion("is_consensus <=", value, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusIn(List<Integer> values) {
            addCriterion("is_consensus in", values, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusNotIn(List<Integer> values) {
            addCriterion("is_consensus not in", values, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusBetween(Integer value1, Integer value2) {
            addCriterion("is_consensus between", value1, value2, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsConsensusNotBetween(Integer value1, Integer value2) {
            addCriterion("is_consensus not between", value1, value2, "isConsensus");
            return (Criteria) this;
        }

        public Criteria andIsSettingIsNull() {
            addCriterion("is_setting is null");
            return (Criteria) this;
        }

        public Criteria andIsSettingIsNotNull() {
            addCriterion("is_setting is not null");
            return (Criteria) this;
        }

        public Criteria andIsSettingEqualTo(Integer value) {
            addCriterion("is_setting =", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingNotEqualTo(Integer value) {
            addCriterion("is_setting <>", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingGreaterThan(Integer value) {
            addCriterion("is_setting >", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_setting >=", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingLessThan(Integer value) {
            addCriterion("is_setting <", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingLessThanOrEqualTo(Integer value) {
            addCriterion("is_setting <=", value, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingIn(List<Integer> values) {
            addCriterion("is_setting in", values, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingNotIn(List<Integer> values) {
            addCriterion("is_setting not in", values, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingBetween(Integer value1, Integer value2) {
            addCriterion("is_setting between", value1, value2, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsSettingNotBetween(Integer value1, Integer value2) {
            addCriterion("is_setting not between", value1, value2, "isSetting");
            return (Criteria) this;
        }

        public Criteria andIsInitIsNull() {
            addCriterion("is_init is null");
            return (Criteria) this;
        }

        public Criteria andIsInitIsNotNull() {
            addCriterion("is_init is not null");
            return (Criteria) this;
        }

        public Criteria andIsInitEqualTo(Integer value) {
            addCriterion("is_init =", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitNotEqualTo(Integer value) {
            addCriterion("is_init <>", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitGreaterThan(Integer value) {
            addCriterion("is_init >", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_init >=", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitLessThan(Integer value) {
            addCriterion("is_init <", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitLessThanOrEqualTo(Integer value) {
            addCriterion("is_init <=", value, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitIn(List<Integer> values) {
            addCriterion("is_init in", values, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitNotIn(List<Integer> values) {
            addCriterion("is_init not in", values, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitBetween(Integer value1, Integer value2) {
            addCriterion("is_init between", value1, value2, "isInit");
            return (Criteria) this;
        }

        public Criteria andIsInitNotBetween(Integer value1, Integer value2) {
            addCriterion("is_init not between", value1, value2, "isInit");
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