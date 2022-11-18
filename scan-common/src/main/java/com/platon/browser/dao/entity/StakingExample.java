package com.platon.browser.dao.entity;

import java.math.BigDecimal;
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

        public Criteria andStakingHesIsNull() {
            addCriterion("staking_hes is null");
            return (Criteria) this;
        }

        public Criteria andStakingHesIsNotNull() {
            addCriterion("staking_hes is not null");
            return (Criteria) this;
        }

        public Criteria andStakingHesEqualTo(BigDecimal value) {
            addCriterion("staking_hes =", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesNotEqualTo(BigDecimal value) {
            addCriterion("staking_hes <>", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesGreaterThan(BigDecimal value) {
            addCriterion("staking_hes >", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_hes >=", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesLessThan(BigDecimal value) {
            addCriterion("staking_hes <", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_hes <=", value, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesIn(List<BigDecimal> values) {
            addCriterion("staking_hes in", values, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesNotIn(List<BigDecimal> values) {
            addCriterion("staking_hes not in", values, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_hes between", value1, value2, "stakingHes");
            return (Criteria) this;
        }

        public Criteria andStakingHesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_hes not between", value1, value2, "stakingHes");
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

        public Criteria andStakingLockedEqualTo(BigDecimal value) {
            addCriterion("staking_locked =", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotEqualTo(BigDecimal value) {
            addCriterion("staking_locked <>", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedGreaterThan(BigDecimal value) {
            addCriterion("staking_locked >", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_locked >=", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedLessThan(BigDecimal value) {
            addCriterion("staking_locked <", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_locked <=", value, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedIn(List<BigDecimal> values) {
            addCriterion("staking_locked in", values, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotIn(List<BigDecimal> values) {
            addCriterion("staking_locked not in", values, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_locked between", value1, value2, "stakingLocked");
            return (Criteria) this;
        }

        public Criteria andStakingLockedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_locked not between", value1, value2, "stakingLocked");
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

        public Criteria andStakingReductionEqualTo(BigDecimal value) {
            addCriterion("staking_reduction =", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotEqualTo(BigDecimal value) {
            addCriterion("staking_reduction <>", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionGreaterThan(BigDecimal value) {
            addCriterion("staking_reduction >", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reduction >=", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionLessThan(BigDecimal value) {
            addCriterion("staking_reduction <", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reduction <=", value, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionIn(List<BigDecimal> values) {
            addCriterion("staking_reduction in", values, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotIn(List<BigDecimal> values) {
            addCriterion("staking_reduction not in", values, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reduction between", value1, value2, "stakingReduction");
            return (Criteria) this;
        }

        public Criteria andStakingReductionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reduction not between", value1, value2, "stakingReduction");
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

        public Criteria andNodeIconIsNull() {
            addCriterion("node_icon is null");
            return (Criteria) this;
        }

        public Criteria andNodeIconIsNotNull() {
            addCriterion("node_icon is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIconEqualTo(String value) {
            addCriterion("node_icon =", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconNotEqualTo(String value) {
            addCriterion("node_icon <>", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconGreaterThan(String value) {
            addCriterion("node_icon >", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconGreaterThanOrEqualTo(String value) {
            addCriterion("node_icon >=", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconLessThan(String value) {
            addCriterion("node_icon <", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconLessThanOrEqualTo(String value) {
            addCriterion("node_icon <=", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconLike(String value) {
            addCriterion("node_icon like", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconNotLike(String value) {
            addCriterion("node_icon not like", value, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconIn(List<String> values) {
            addCriterion("node_icon in", values, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconNotIn(List<String> values) {
            addCriterion("node_icon not in", values, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconBetween(String value1, String value2) {
            addCriterion("node_icon between", value1, value2, "nodeIcon");
            return (Criteria) this;
        }

        public Criteria andNodeIconNotBetween(String value1, String value2) {
            addCriterion("node_icon not between", value1, value2, "nodeIcon");
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

        public Criteria andExternalNameIsNull() {
            addCriterion("external_name is null");
            return (Criteria) this;
        }

        public Criteria andExternalNameIsNotNull() {
            addCriterion("external_name is not null");
            return (Criteria) this;
        }

        public Criteria andExternalNameEqualTo(String value) {
            addCriterion("external_name =", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameNotEqualTo(String value) {
            addCriterion("external_name <>", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameGreaterThan(String value) {
            addCriterion("external_name >", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameGreaterThanOrEqualTo(String value) {
            addCriterion("external_name >=", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameLessThan(String value) {
            addCriterion("external_name <", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameLessThanOrEqualTo(String value) {
            addCriterion("external_name <=", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameLike(String value) {
            addCriterion("external_name like", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameNotLike(String value) {
            addCriterion("external_name not like", value, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameIn(List<String> values) {
            addCriterion("external_name in", values, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameNotIn(List<String> values) {
            addCriterion("external_name not in", values, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameBetween(String value1, String value2) {
            addCriterion("external_name between", value1, value2, "externalName");
            return (Criteria) this;
        }

        public Criteria andExternalNameNotBetween(String value1, String value2) {
            addCriterion("external_name not between", value1, value2, "externalName");
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

        public Criteria andBenefitAddrIsNull() {
            addCriterion("benefit_addr is null");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrIsNotNull() {
            addCriterion("benefit_addr is not null");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrEqualTo(String value) {
            addCriterion("benefit_addr =", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrNotEqualTo(String value) {
            addCriterion("benefit_addr <>", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrGreaterThan(String value) {
            addCriterion("benefit_addr >", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrGreaterThanOrEqualTo(String value) {
            addCriterion("benefit_addr >=", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrLessThan(String value) {
            addCriterion("benefit_addr <", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrLessThanOrEqualTo(String value) {
            addCriterion("benefit_addr <=", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrLike(String value) {
            addCriterion("benefit_addr like", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrNotLike(String value) {
            addCriterion("benefit_addr not like", value, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrIn(List<String> values) {
            addCriterion("benefit_addr in", values, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrNotIn(List<String> values) {
            addCriterion("benefit_addr not in", values, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrBetween(String value1, String value2) {
            addCriterion("benefit_addr between", value1, value2, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andBenefitAddrNotBetween(String value1, String value2) {
            addCriterion("benefit_addr not between", value1, value2, "benefitAddr");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateIsNull() {
            addCriterion("annualized_rate is null");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateIsNotNull() {
            addCriterion("annualized_rate is not null");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateEqualTo(Double value) {
            addCriterion("annualized_rate =", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateNotEqualTo(Double value) {
            addCriterion("annualized_rate <>", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateGreaterThan(Double value) {
            addCriterion("annualized_rate >", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateGreaterThanOrEqualTo(Double value) {
            addCriterion("annualized_rate >=", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateLessThan(Double value) {
            addCriterion("annualized_rate <", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateLessThanOrEqualTo(Double value) {
            addCriterion("annualized_rate <=", value, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateIn(List<Double> values) {
            addCriterion("annualized_rate in", values, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateNotIn(List<Double> values) {
            addCriterion("annualized_rate not in", values, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateBetween(Double value1, Double value2) {
            addCriterion("annualized_rate between", value1, value2, "annualizedRate");
            return (Criteria) this;
        }

        public Criteria andAnnualizedRateNotBetween(Double value1, Double value2) {
            addCriterion("annualized_rate not between", value1, value2, "annualizedRate");
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

        public Criteria andBigVersionIsNull() {
            addCriterion("big_version is null");
            return (Criteria) this;
        }

        public Criteria andBigVersionIsNotNull() {
            addCriterion("big_version is not null");
            return (Criteria) this;
        }

        public Criteria andBigVersionEqualTo(String value) {
            addCriterion("big_version =", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionNotEqualTo(String value) {
            addCriterion("big_version <>", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionGreaterThan(String value) {
            addCriterion("big_version >", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionGreaterThanOrEqualTo(String value) {
            addCriterion("big_version >=", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionLessThan(String value) {
            addCriterion("big_version <", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionLessThanOrEqualTo(String value) {
            addCriterion("big_version <=", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionLike(String value) {
            addCriterion("big_version like", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionNotLike(String value) {
            addCriterion("big_version not like", value, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionIn(List<String> values) {
            addCriterion("big_version in", values, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionNotIn(List<String> values) {
            addCriterion("big_version not in", values, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionBetween(String value1, String value2) {
            addCriterion("big_version between", value1, value2, "bigVersion");
            return (Criteria) this;
        }

        public Criteria andBigVersionNotBetween(String value1, String value2) {
            addCriterion("big_version not between", value1, value2, "bigVersion");
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

        public Criteria andLeaveNumIsNull() {
            addCriterion("leave_num is null");
            return (Criteria) this;
        }

        public Criteria andLeaveNumIsNotNull() {
            addCriterion("leave_num is not null");
            return (Criteria) this;
        }

        public Criteria andLeaveNumEqualTo(Long value) {
            addCriterion("leave_num =", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumNotEqualTo(Long value) {
            addCriterion("leave_num <>", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumGreaterThan(Long value) {
            addCriterion("leave_num >", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumGreaterThanOrEqualTo(Long value) {
            addCriterion("leave_num >=", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumLessThan(Long value) {
            addCriterion("leave_num <", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumLessThanOrEqualTo(Long value) {
            addCriterion("leave_num <=", value, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumIn(List<Long> values) {
            addCriterion("leave_num in", values, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumNotIn(List<Long> values) {
            addCriterion("leave_num not in", values, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumBetween(Long value1, Long value2) {
            addCriterion("leave_num between", value1, value2, "leaveNum");
            return (Criteria) this;
        }

        public Criteria andLeaveNumNotBetween(Long value1, Long value2) {
            addCriterion("leave_num not between", value1, value2, "leaveNum");
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

        public Criteria andIsSettleIsNull() {
            addCriterion("is_settle is null");
            return (Criteria) this;
        }

        public Criteria andIsSettleIsNotNull() {
            addCriterion("is_settle is not null");
            return (Criteria) this;
        }

        public Criteria andIsSettleEqualTo(Integer value) {
            addCriterion("is_settle =", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleNotEqualTo(Integer value) {
            addCriterion("is_settle <>", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleGreaterThan(Integer value) {
            addCriterion("is_settle >", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_settle >=", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleLessThan(Integer value) {
            addCriterion("is_settle <", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleLessThanOrEqualTo(Integer value) {
            addCriterion("is_settle <=", value, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleIn(List<Integer> values) {
            addCriterion("is_settle in", values, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleNotIn(List<Integer> values) {
            addCriterion("is_settle not in", values, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleBetween(Integer value1, Integer value2) {
            addCriterion("is_settle between", value1, value2, "isSettle");
            return (Criteria) this;
        }

        public Criteria andIsSettleNotBetween(Integer value1, Integer value2) {
            addCriterion("is_settle not between", value1, value2, "isSettle");
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

        public Criteria andStatDelegateHesIsNull() {
            addCriterion("stat_delegate_hes is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesIsNotNull() {
            addCriterion("stat_delegate_hes is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_hes =", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesNotEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_hes <>", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesGreaterThan(BigDecimal value) {
            addCriterion("stat_delegate_hes >", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_hes >=", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesLessThan(BigDecimal value) {
            addCriterion("stat_delegate_hes <", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_hes <=", value, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_hes in", values, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesNotIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_hes not in", values, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_hes between", value1, value2, "statDelegateHes");
            return (Criteria) this;
        }

        public Criteria andStatDelegateHesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_hes not between", value1, value2, "statDelegateHes");
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

        public Criteria andStatDelegateLockedEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_locked =", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_locked <>", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedGreaterThan(BigDecimal value) {
            addCriterion("stat_delegate_locked >", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_locked >=", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedLessThan(BigDecimal value) {
            addCriterion("stat_delegate_locked <", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_locked <=", value, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_locked in", values, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_locked not in", values, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_locked between", value1, value2, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateLockedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_locked not between", value1, value2, "statDelegateLocked");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedIsNull() {
            addCriterion("stat_delegate_released is null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedIsNotNull() {
            addCriterion("stat_delegate_released is not null");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_released =", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedNotEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_released <>", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedGreaterThan(BigDecimal value) {
            addCriterion("stat_delegate_released >", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_released >=", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedLessThan(BigDecimal value) {
            addCriterion("stat_delegate_released <", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("stat_delegate_released <=", value, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_released in", values, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedNotIn(List<BigDecimal> values) {
            addCriterion("stat_delegate_released not in", values, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_released between", value1, value2, "statDelegateReleased");
            return (Criteria) this;
        }

        public Criteria andStatDelegateReleasedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("stat_delegate_released not between", value1, value2, "statDelegateReleased");
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

        public Criteria andBlockRewardValueEqualTo(BigDecimal value) {
            addCriterion("block_reward_value =", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotEqualTo(BigDecimal value) {
            addCriterion("block_reward_value <>", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueGreaterThan(BigDecimal value) {
            addCriterion("block_reward_value >", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("block_reward_value >=", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueLessThan(BigDecimal value) {
            addCriterion("block_reward_value <", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("block_reward_value <=", value, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueIn(List<BigDecimal> values) {
            addCriterion("block_reward_value in", values, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotIn(List<BigDecimal> values) {
            addCriterion("block_reward_value not in", values, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("block_reward_value between", value1, value2, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andBlockRewardValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("block_reward_value not between", value1, value2, "blockRewardValue");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardIsNull() {
            addCriterion("predict_staking_reward is null");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardIsNotNull() {
            addCriterion("predict_staking_reward is not null");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardEqualTo(BigDecimal value) {
            addCriterion("predict_staking_reward =", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardNotEqualTo(BigDecimal value) {
            addCriterion("predict_staking_reward <>", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardGreaterThan(BigDecimal value) {
            addCriterion("predict_staking_reward >", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("predict_staking_reward >=", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardLessThan(BigDecimal value) {
            addCriterion("predict_staking_reward <", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("predict_staking_reward <=", value, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardIn(List<BigDecimal> values) {
            addCriterion("predict_staking_reward in", values, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardNotIn(List<BigDecimal> values) {
            addCriterion("predict_staking_reward not in", values, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("predict_staking_reward between", value1, value2, "predictStakingReward");
            return (Criteria) this;
        }

        public Criteria andPredictStakingRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("predict_staking_reward not between", value1, value2, "predictStakingReward");
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

        public Criteria andStakingRewardValueEqualTo(BigDecimal value) {
            addCriterion("staking_reward_value =", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotEqualTo(BigDecimal value) {
            addCriterion("staking_reward_value <>", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueGreaterThan(BigDecimal value) {
            addCriterion("staking_reward_value >", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reward_value >=", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueLessThan(BigDecimal value) {
            addCriterion("staking_reward_value <", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reward_value <=", value, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueIn(List<BigDecimal> values) {
            addCriterion("staking_reward_value in", values, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotIn(List<BigDecimal> values) {
            addCriterion("staking_reward_value not in", values, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reward_value between", value1, value2, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andStakingRewardValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reward_value not between", value1, value2, "stakingRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueIsNull() {
            addCriterion("fee_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueIsNotNull() {
            addCriterion("fee_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueEqualTo(BigDecimal value) {
            addCriterion("fee_reward_value =", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueNotEqualTo(BigDecimal value) {
            addCriterion("fee_reward_value <>", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueGreaterThan(BigDecimal value) {
            addCriterion("fee_reward_value >", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee_reward_value >=", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueLessThan(BigDecimal value) {
            addCriterion("fee_reward_value <", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee_reward_value <=", value, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueIn(List<BigDecimal> values) {
            addCriterion("fee_reward_value in", values, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueNotIn(List<BigDecimal> values) {
            addCriterion("fee_reward_value not in", values, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee_reward_value between", value1, value2, "feeRewardValue");
            return (Criteria) this;
        }

        public Criteria andFeeRewardValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee_reward_value not between", value1, value2, "feeRewardValue");
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

        public Criteria andRewardPerIsNull() {
            addCriterion("reward_per is null");
            return (Criteria) this;
        }

        public Criteria andRewardPerIsNotNull() {
            addCriterion("reward_per is not null");
            return (Criteria) this;
        }

        public Criteria andRewardPerEqualTo(Integer value) {
            addCriterion("reward_per =", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerNotEqualTo(Integer value) {
            addCriterion("reward_per <>", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerGreaterThan(Integer value) {
            addCriterion("reward_per >", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerGreaterThanOrEqualTo(Integer value) {
            addCriterion("reward_per >=", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerLessThan(Integer value) {
            addCriterion("reward_per <", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerLessThanOrEqualTo(Integer value) {
            addCriterion("reward_per <=", value, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerIn(List<Integer> values) {
            addCriterion("reward_per in", values, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerNotIn(List<Integer> values) {
            addCriterion("reward_per not in", values, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerBetween(Integer value1, Integer value2) {
            addCriterion("reward_per between", value1, value2, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andRewardPerNotBetween(Integer value1, Integer value2) {
            addCriterion("reward_per not between", value1, value2, "rewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerIsNull() {
            addCriterion("next_reward_per is null");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerIsNotNull() {
            addCriterion("next_reward_per is not null");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerEqualTo(Integer value) {
            addCriterion("next_reward_per =", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerNotEqualTo(Integer value) {
            addCriterion("next_reward_per <>", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerGreaterThan(Integer value) {
            addCriterion("next_reward_per >", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerGreaterThanOrEqualTo(Integer value) {
            addCriterion("next_reward_per >=", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerLessThan(Integer value) {
            addCriterion("next_reward_per <", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerLessThanOrEqualTo(Integer value) {
            addCriterion("next_reward_per <=", value, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerIn(List<Integer> values) {
            addCriterion("next_reward_per in", values, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerNotIn(List<Integer> values) {
            addCriterion("next_reward_per not in", values, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerBetween(Integer value1, Integer value2) {
            addCriterion("next_reward_per between", value1, value2, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerNotBetween(Integer value1, Integer value2) {
            addCriterion("next_reward_per not between", value1, value2, "nextRewardPer");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochIsNull() {
            addCriterion("next_reward_per_mod_epoch is null");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochIsNotNull() {
            addCriterion("next_reward_per_mod_epoch is not null");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochEqualTo(Integer value) {
            addCriterion("next_reward_per_mod_epoch =", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochNotEqualTo(Integer value) {
            addCriterion("next_reward_per_mod_epoch <>", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochGreaterThan(Integer value) {
            addCriterion("next_reward_per_mod_epoch >", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochGreaterThanOrEqualTo(Integer value) {
            addCriterion("next_reward_per_mod_epoch >=", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochLessThan(Integer value) {
            addCriterion("next_reward_per_mod_epoch <", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochLessThanOrEqualTo(Integer value) {
            addCriterion("next_reward_per_mod_epoch <=", value, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochIn(List<Integer> values) {
            addCriterion("next_reward_per_mod_epoch in", values, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochNotIn(List<Integer> values) {
            addCriterion("next_reward_per_mod_epoch not in", values, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochBetween(Integer value1, Integer value2) {
            addCriterion("next_reward_per_mod_epoch between", value1, value2, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andNextRewardPerModEpochNotBetween(Integer value1, Integer value2) {
            addCriterion("next_reward_per_mod_epoch not between", value1, value2, "nextRewardPerModEpoch");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardIsNull() {
            addCriterion("have_dele_reward is null");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardIsNotNull() {
            addCriterion("have_dele_reward is not null");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardEqualTo(BigDecimal value) {
            addCriterion("have_dele_reward =", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardNotEqualTo(BigDecimal value) {
            addCriterion("have_dele_reward <>", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardGreaterThan(BigDecimal value) {
            addCriterion("have_dele_reward >", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("have_dele_reward >=", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardLessThan(BigDecimal value) {
            addCriterion("have_dele_reward <", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("have_dele_reward <=", value, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardIn(List<BigDecimal> values) {
            addCriterion("have_dele_reward in", values, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardNotIn(List<BigDecimal> values) {
            addCriterion("have_dele_reward not in", values, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("have_dele_reward between", value1, value2, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andHaveDeleRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("have_dele_reward not between", value1, value2, "haveDeleReward");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateIsNull() {
            addCriterion("pre_dele_annualized_rate is null");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateIsNotNull() {
            addCriterion("pre_dele_annualized_rate is not null");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateEqualTo(Double value) {
            addCriterion("pre_dele_annualized_rate =", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateNotEqualTo(Double value) {
            addCriterion("pre_dele_annualized_rate <>", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateGreaterThan(Double value) {
            addCriterion("pre_dele_annualized_rate >", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateGreaterThanOrEqualTo(Double value) {
            addCriterion("pre_dele_annualized_rate >=", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateLessThan(Double value) {
            addCriterion("pre_dele_annualized_rate <", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateLessThanOrEqualTo(Double value) {
            addCriterion("pre_dele_annualized_rate <=", value, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateIn(List<Double> values) {
            addCriterion("pre_dele_annualized_rate in", values, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateNotIn(List<Double> values) {
            addCriterion("pre_dele_annualized_rate not in", values, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateBetween(Double value1, Double value2) {
            addCriterion("pre_dele_annualized_rate between", value1, value2, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andPreDeleAnnualizedRateNotBetween(Double value1, Double value2) {
            addCriterion("pre_dele_annualized_rate not between", value1, value2, "preDeleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateIsNull() {
            addCriterion("dele_annualized_rate is null");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateIsNotNull() {
            addCriterion("dele_annualized_rate is not null");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateEqualTo(Double value) {
            addCriterion("dele_annualized_rate =", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateNotEqualTo(Double value) {
            addCriterion("dele_annualized_rate <>", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateGreaterThan(Double value) {
            addCriterion("dele_annualized_rate >", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateGreaterThanOrEqualTo(Double value) {
            addCriterion("dele_annualized_rate >=", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateLessThan(Double value) {
            addCriterion("dele_annualized_rate <", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateLessThanOrEqualTo(Double value) {
            addCriterion("dele_annualized_rate <=", value, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateIn(List<Double> values) {
            addCriterion("dele_annualized_rate in", values, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateNotIn(List<Double> values) {
            addCriterion("dele_annualized_rate not in", values, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateBetween(Double value1, Double value2) {
            addCriterion("dele_annualized_rate between", value1, value2, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andDeleAnnualizedRateNotBetween(Double value1, Double value2) {
            addCriterion("dele_annualized_rate not between", value1, value2, "deleAnnualizedRate");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardIsNull() {
            addCriterion("total_dele_reward is null");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardIsNotNull() {
            addCriterion("total_dele_reward is not null");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardEqualTo(BigDecimal value) {
            addCriterion("total_dele_reward =", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardNotEqualTo(BigDecimal value) {
            addCriterion("total_dele_reward <>", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardGreaterThan(BigDecimal value) {
            addCriterion("total_dele_reward >", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_dele_reward >=", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardLessThan(BigDecimal value) {
            addCriterion("total_dele_reward <", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_dele_reward <=", value, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardIn(List<BigDecimal> values) {
            addCriterion("total_dele_reward in", values, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardNotIn(List<BigDecimal> values) {
            addCriterion("total_dele_reward not in", values, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_dele_reward between", value1, value2, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andTotalDeleRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_dele_reward not between", value1, value2, "totalDeleReward");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusIsNull() {
            addCriterion("exception_status is null");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusIsNotNull() {
            addCriterion("exception_status is not null");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusEqualTo(Integer value) {
            addCriterion("exception_status =", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusNotEqualTo(Integer value) {
            addCriterion("exception_status <>", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusGreaterThan(Integer value) {
            addCriterion("exception_status >", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("exception_status >=", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusLessThan(Integer value) {
            addCriterion("exception_status <", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusLessThanOrEqualTo(Integer value) {
            addCriterion("exception_status <=", value, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusIn(List<Integer> values) {
            addCriterion("exception_status in", values, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusNotIn(List<Integer> values) {
            addCriterion("exception_status not in", values, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusBetween(Integer value1, Integer value2) {
            addCriterion("exception_status between", value1, value2, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andExceptionStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("exception_status not between", value1, value2, "exceptionStatus");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIsNull() {
            addCriterion("un_stake_freeze_duration is null");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIsNotNull() {
            addCriterion("un_stake_freeze_duration is not null");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration =", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration <>", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationGreaterThan(Integer value) {
            addCriterion("un_stake_freeze_duration >", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationGreaterThanOrEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration >=", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationLessThan(Integer value) {
            addCriterion("un_stake_freeze_duration <", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationLessThanOrEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration <=", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIn(List<Integer> values) {
            addCriterion("un_stake_freeze_duration in", values, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotIn(List<Integer> values) {
            addCriterion("un_stake_freeze_duration not in", values, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationBetween(Integer value1, Integer value2) {
            addCriterion("un_stake_freeze_duration between", value1, value2, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotBetween(Integer value1, Integer value2) {
            addCriterion("un_stake_freeze_duration not between", value1, value2, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIsNull() {
            addCriterion("un_stake_end_block is null");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIsNotNull() {
            addCriterion("un_stake_end_block is not null");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockEqualTo(Long value) {
            addCriterion("un_stake_end_block =", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotEqualTo(Long value) {
            addCriterion("un_stake_end_block <>", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockGreaterThan(Long value) {
            addCriterion("un_stake_end_block >", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockGreaterThanOrEqualTo(Long value) {
            addCriterion("un_stake_end_block >=", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockLessThan(Long value) {
            addCriterion("un_stake_end_block <", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockLessThanOrEqualTo(Long value) {
            addCriterion("un_stake_end_block <=", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIn(List<Long> values) {
            addCriterion("un_stake_end_block in", values, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotIn(List<Long> values) {
            addCriterion("un_stake_end_block not in", values, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockBetween(Long value1, Long value2) {
            addCriterion("un_stake_end_block between", value1, value2, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotBetween(Long value1, Long value2) {
            addCriterion("un_stake_end_block not between", value1, value2, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationIsNull() {
            addCriterion("zero_produce_freeze_duration is null");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationIsNotNull() {
            addCriterion("zero_produce_freeze_duration is not null");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_duration =", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationNotEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_duration <>", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationGreaterThan(Integer value) {
            addCriterion("zero_produce_freeze_duration >", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationGreaterThanOrEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_duration >=", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationLessThan(Integer value) {
            addCriterion("zero_produce_freeze_duration <", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationLessThanOrEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_duration <=", value, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationIn(List<Integer> values) {
            addCriterion("zero_produce_freeze_duration in", values, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationNotIn(List<Integer> values) {
            addCriterion("zero_produce_freeze_duration not in", values, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationBetween(Integer value1, Integer value2) {
            addCriterion("zero_produce_freeze_duration between", value1, value2, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeDurationNotBetween(Integer value1, Integer value2) {
            addCriterion("zero_produce_freeze_duration not between", value1, value2, "zeroProduceFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochIsNull() {
            addCriterion("zero_produce_freeze_epoch is null");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochIsNotNull() {
            addCriterion("zero_produce_freeze_epoch is not null");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_epoch =", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochNotEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_epoch <>", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochGreaterThan(Integer value) {
            addCriterion("zero_produce_freeze_epoch >", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochGreaterThanOrEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_epoch >=", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochLessThan(Integer value) {
            addCriterion("zero_produce_freeze_epoch <", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochLessThanOrEqualTo(Integer value) {
            addCriterion("zero_produce_freeze_epoch <=", value, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochIn(List<Integer> values) {
            addCriterion("zero_produce_freeze_epoch in", values, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochNotIn(List<Integer> values) {
            addCriterion("zero_produce_freeze_epoch not in", values, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochBetween(Integer value1, Integer value2) {
            addCriterion("zero_produce_freeze_epoch between", value1, value2, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andZeroProduceFreezeEpochNotBetween(Integer value1, Integer value2) {
            addCriterion("zero_produce_freeze_epoch not between", value1, value2, "zeroProduceFreezeEpoch");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountIsNull() {
            addCriterion("low_rate_slash_count is null");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountIsNotNull() {
            addCriterion("low_rate_slash_count is not null");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountEqualTo(Integer value) {
            addCriterion("low_rate_slash_count =", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountNotEqualTo(Integer value) {
            addCriterion("low_rate_slash_count <>", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountGreaterThan(Integer value) {
            addCriterion("low_rate_slash_count >", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("low_rate_slash_count >=", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountLessThan(Integer value) {
            addCriterion("low_rate_slash_count <", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountLessThanOrEqualTo(Integer value) {
            addCriterion("low_rate_slash_count <=", value, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountIn(List<Integer> values) {
            addCriterion("low_rate_slash_count in", values, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountNotIn(List<Integer> values) {
            addCriterion("low_rate_slash_count not in", values, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountBetween(Integer value1, Integer value2) {
            addCriterion("low_rate_slash_count between", value1, value2, "lowRateSlashCount");
            return (Criteria) this;
        }

        public Criteria andLowRateSlashCountNotBetween(Integer value1, Integer value2) {
            addCriterion("low_rate_slash_count not between", value1, value2, "lowRateSlashCount");
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