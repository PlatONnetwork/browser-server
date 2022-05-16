package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkStatExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NetworkStatExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCurNumberIsNull() {
            addCriterion("cur_number is null");
            return (Criteria) this;
        }

        public Criteria andCurNumberIsNotNull() {
            addCriterion("cur_number is not null");
            return (Criteria) this;
        }

        public Criteria andCurNumberEqualTo(Long value) {
            addCriterion("cur_number =", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberNotEqualTo(Long value) {
            addCriterion("cur_number <>", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberGreaterThan(Long value) {
            addCriterion("cur_number >", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("cur_number >=", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberLessThan(Long value) {
            addCriterion("cur_number <", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberLessThanOrEqualTo(Long value) {
            addCriterion("cur_number <=", value, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberIn(List<Long> values) {
            addCriterion("cur_number in", values, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberNotIn(List<Long> values) {
            addCriterion("cur_number not in", values, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberBetween(Long value1, Long value2) {
            addCriterion("cur_number between", value1, value2, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurNumberNotBetween(Long value1, Long value2) {
            addCriterion("cur_number not between", value1, value2, "curNumber");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashIsNull() {
            addCriterion("cur_block_hash is null");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashIsNotNull() {
            addCriterion("cur_block_hash is not null");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashEqualTo(String value) {
            addCriterion("cur_block_hash =", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashNotEqualTo(String value) {
            addCriterion("cur_block_hash <>", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashGreaterThan(String value) {
            addCriterion("cur_block_hash >", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashGreaterThanOrEqualTo(String value) {
            addCriterion("cur_block_hash >=", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashLessThan(String value) {
            addCriterion("cur_block_hash <", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashLessThanOrEqualTo(String value) {
            addCriterion("cur_block_hash <=", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashLike(String value) {
            addCriterion("cur_block_hash like", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashNotLike(String value) {
            addCriterion("cur_block_hash not like", value, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashIn(List<String> values) {
            addCriterion("cur_block_hash in", values, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashNotIn(List<String> values) {
            addCriterion("cur_block_hash not in", values, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashBetween(String value1, String value2) {
            addCriterion("cur_block_hash between", value1, value2, "curBlockHash");
            return (Criteria) this;
        }

        public Criteria andCurBlockHashNotBetween(String value1, String value2) {
            addCriterion("cur_block_hash not between", value1, value2, "curBlockHash");
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

        public Criteria andCurTpsIsNull() {
            addCriterion("cur_tps is null");
            return (Criteria) this;
        }

        public Criteria andCurTpsIsNotNull() {
            addCriterion("cur_tps is not null");
            return (Criteria) this;
        }

        public Criteria andCurTpsEqualTo(Integer value) {
            addCriterion("cur_tps =", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsNotEqualTo(Integer value) {
            addCriterion("cur_tps <>", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsGreaterThan(Integer value) {
            addCriterion("cur_tps >", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsGreaterThanOrEqualTo(Integer value) {
            addCriterion("cur_tps >=", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsLessThan(Integer value) {
            addCriterion("cur_tps <", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsLessThanOrEqualTo(Integer value) {
            addCriterion("cur_tps <=", value, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsIn(List<Integer> values) {
            addCriterion("cur_tps in", values, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsNotIn(List<Integer> values) {
            addCriterion("cur_tps not in", values, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsBetween(Integer value1, Integer value2) {
            addCriterion("cur_tps between", value1, value2, "curTps");
            return (Criteria) this;
        }

        public Criteria andCurTpsNotBetween(Integer value1, Integer value2) {
            addCriterion("cur_tps not between", value1, value2, "curTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsIsNull() {
            addCriterion("max_tps is null");
            return (Criteria) this;
        }

        public Criteria andMaxTpsIsNotNull() {
            addCriterion("max_tps is not null");
            return (Criteria) this;
        }

        public Criteria andMaxTpsEqualTo(Integer value) {
            addCriterion("max_tps =", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsNotEqualTo(Integer value) {
            addCriterion("max_tps <>", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsGreaterThan(Integer value) {
            addCriterion("max_tps >", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_tps >=", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsLessThan(Integer value) {
            addCriterion("max_tps <", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsLessThanOrEqualTo(Integer value) {
            addCriterion("max_tps <=", value, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsIn(List<Integer> values) {
            addCriterion("max_tps in", values, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsNotIn(List<Integer> values) {
            addCriterion("max_tps not in", values, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsBetween(Integer value1, Integer value2) {
            addCriterion("max_tps between", value1, value2, "maxTps");
            return (Criteria) this;
        }

        public Criteria andMaxTpsNotBetween(Integer value1, Integer value2) {
            addCriterion("max_tps not between", value1, value2, "maxTps");
            return (Criteria) this;
        }

        public Criteria andIssueValueIsNull() {
            addCriterion("issue_value is null");
            return (Criteria) this;
        }

        public Criteria andIssueValueIsNotNull() {
            addCriterion("issue_value is not null");
            return (Criteria) this;
        }

        public Criteria andIssueValueEqualTo(BigDecimal value) {
            addCriterion("issue_value =", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotEqualTo(BigDecimal value) {
            addCriterion("issue_value <>", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueGreaterThan(BigDecimal value) {
            addCriterion("issue_value >", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("issue_value >=", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueLessThan(BigDecimal value) {
            addCriterion("issue_value <", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("issue_value <=", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueIn(List<BigDecimal> values) {
            addCriterion("issue_value in", values, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotIn(List<BigDecimal> values) {
            addCriterion("issue_value not in", values, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("issue_value between", value1, value2, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("issue_value not between", value1, value2, "issueValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueIsNull() {
            addCriterion("turn_value is null");
            return (Criteria) this;
        }

        public Criteria andTurnValueIsNotNull() {
            addCriterion("turn_value is not null");
            return (Criteria) this;
        }

        public Criteria andTurnValueEqualTo(BigDecimal value) {
            addCriterion("turn_value =", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotEqualTo(BigDecimal value) {
            addCriterion("turn_value <>", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueGreaterThan(BigDecimal value) {
            addCriterion("turn_value >", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("turn_value >=", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueLessThan(BigDecimal value) {
            addCriterion("turn_value <", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("turn_value <=", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueIn(List<BigDecimal> values) {
            addCriterion("turn_value in", values, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotIn(List<BigDecimal> values) {
            addCriterion("turn_value not in", values, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("turn_value between", value1, value2, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("turn_value not between", value1, value2, "turnValue");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingIsNull() {
            addCriterion("available_staking is null");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingIsNotNull() {
            addCriterion("available_staking is not null");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingEqualTo(BigDecimal value) {
            addCriterion("available_staking =", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingNotEqualTo(BigDecimal value) {
            addCriterion("available_staking <>", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingGreaterThan(BigDecimal value) {
            addCriterion("available_staking >", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("available_staking >=", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingLessThan(BigDecimal value) {
            addCriterion("available_staking <", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingLessThanOrEqualTo(BigDecimal value) {
            addCriterion("available_staking <=", value, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingIn(List<BigDecimal> values) {
            addCriterion("available_staking in", values, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingNotIn(List<BigDecimal> values) {
            addCriterion("available_staking not in", values, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("available_staking between", value1, value2, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andAvailableStakingNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("available_staking not between", value1, value2, "availableStaking");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueIsNull() {
            addCriterion("staking_delegation_value is null");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueIsNotNull() {
            addCriterion("staking_delegation_value is not null");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueEqualTo(BigDecimal value) {
            addCriterion("staking_delegation_value =", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotEqualTo(BigDecimal value) {
            addCriterion("staking_delegation_value <>", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueGreaterThan(BigDecimal value) {
            addCriterion("staking_delegation_value >", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_delegation_value >=", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueLessThan(BigDecimal value) {
            addCriterion("staking_delegation_value <", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_delegation_value <=", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueIn(List<BigDecimal> values) {
            addCriterion("staking_delegation_value in", values, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotIn(List<BigDecimal> values) {
            addCriterion("staking_delegation_value not in", values, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_delegation_value between", value1, value2, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_delegation_value not between", value1, value2, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueIsNull() {
            addCriterion("staking_value is null");
            return (Criteria) this;
        }

        public Criteria andStakingValueIsNotNull() {
            addCriterion("staking_value is not null");
            return (Criteria) this;
        }

        public Criteria andStakingValueEqualTo(BigDecimal value) {
            addCriterion("staking_value =", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotEqualTo(BigDecimal value) {
            addCriterion("staking_value <>", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThan(BigDecimal value) {
            addCriterion("staking_value >", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_value >=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThan(BigDecimal value) {
            addCriterion("staking_value <", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_value <=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueIn(List<BigDecimal> values) {
            addCriterion("staking_value in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotIn(List<BigDecimal> values) {
            addCriterion("staking_value not in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_value between", value1, value2, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_value not between", value1, value2, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyIsNull() {
            addCriterion("doing_proposal_qty is null");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyIsNotNull() {
            addCriterion("doing_proposal_qty is not null");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyEqualTo(Integer value) {
            addCriterion("doing_proposal_qty =", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyNotEqualTo(Integer value) {
            addCriterion("doing_proposal_qty <>", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyGreaterThan(Integer value) {
            addCriterion("doing_proposal_qty >", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("doing_proposal_qty >=", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyLessThan(Integer value) {
            addCriterion("doing_proposal_qty <", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyLessThanOrEqualTo(Integer value) {
            addCriterion("doing_proposal_qty <=", value, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyIn(List<Integer> values) {
            addCriterion("doing_proposal_qty in", values, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyNotIn(List<Integer> values) {
            addCriterion("doing_proposal_qty not in", values, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyBetween(Integer value1, Integer value2) {
            addCriterion("doing_proposal_qty between", value1, value2, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andDoingProposalQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("doing_proposal_qty not between", value1, value2, "doingProposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIsNull() {
            addCriterion("proposal_qty is null");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIsNotNull() {
            addCriterion("proposal_qty is not null");
            return (Criteria) this;
        }

        public Criteria andProposalQtyEqualTo(Integer value) {
            addCriterion("proposal_qty =", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotEqualTo(Integer value) {
            addCriterion("proposal_qty <>", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyGreaterThan(Integer value) {
            addCriterion("proposal_qty >", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("proposal_qty >=", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyLessThan(Integer value) {
            addCriterion("proposal_qty <", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyLessThanOrEqualTo(Integer value) {
            addCriterion("proposal_qty <=", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIn(List<Integer> values) {
            addCriterion("proposal_qty in", values, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotIn(List<Integer> values) {
            addCriterion("proposal_qty not in", values, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyBetween(Integer value1, Integer value2) {
            addCriterion("proposal_qty between", value1, value2, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("proposal_qty not between", value1, value2, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyIsNull() {
            addCriterion("address_qty is null");
            return (Criteria) this;
        }

        public Criteria andAddressQtyIsNotNull() {
            addCriterion("address_qty is not null");
            return (Criteria) this;
        }

        public Criteria andAddressQtyEqualTo(Integer value) {
            addCriterion("address_qty =", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyNotEqualTo(Integer value) {
            addCriterion("address_qty <>", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyGreaterThan(Integer value) {
            addCriterion("address_qty >", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("address_qty >=", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyLessThan(Integer value) {
            addCriterion("address_qty <", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyLessThanOrEqualTo(Integer value) {
            addCriterion("address_qty <=", value, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyIn(List<Integer> values) {
            addCriterion("address_qty in", values, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyNotIn(List<Integer> values) {
            addCriterion("address_qty not in", values, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyBetween(Integer value1, Integer value2) {
            addCriterion("address_qty between", value1, value2, "addressQty");
            return (Criteria) this;
        }

        public Criteria andAddressQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("address_qty not between", value1, value2, "addressQty");
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

        public Criteria andBlockRewardEqualTo(BigDecimal value) {
            addCriterion("block_reward =", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotEqualTo(BigDecimal value) {
            addCriterion("block_reward <>", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardGreaterThan(BigDecimal value) {
            addCriterion("block_reward >", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("block_reward >=", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardLessThan(BigDecimal value) {
            addCriterion("block_reward <", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("block_reward <=", value, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardIn(List<BigDecimal> values) {
            addCriterion("block_reward in", values, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotIn(List<BigDecimal> values) {
            addCriterion("block_reward not in", values, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("block_reward between", value1, value2, "blockReward");
            return (Criteria) this;
        }

        public Criteria andBlockRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("block_reward not between", value1, value2, "blockReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardIsNull() {
            addCriterion("staking_reward is null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardIsNotNull() {
            addCriterion("staking_reward is not null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardEqualTo(BigDecimal value) {
            addCriterion("staking_reward =", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotEqualTo(BigDecimal value) {
            addCriterion("staking_reward <>", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardGreaterThan(BigDecimal value) {
            addCriterion("staking_reward >", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reward >=", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardLessThan(BigDecimal value) {
            addCriterion("staking_reward <", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_reward <=", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardIn(List<BigDecimal> values) {
            addCriterion("staking_reward in", values, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotIn(List<BigDecimal> values) {
            addCriterion("staking_reward not in", values, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reward between", value1, value2, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_reward not between", value1, value2, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardIsNull() {
            addCriterion("settle_staking_reward is null");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardIsNotNull() {
            addCriterion("settle_staking_reward is not null");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardEqualTo(BigDecimal value) {
            addCriterion("settle_staking_reward =", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardNotEqualTo(BigDecimal value) {
            addCriterion("settle_staking_reward <>", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardGreaterThan(BigDecimal value) {
            addCriterion("settle_staking_reward >", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("settle_staking_reward >=", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardLessThan(BigDecimal value) {
            addCriterion("settle_staking_reward <", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("settle_staking_reward <=", value, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardIn(List<BigDecimal> values) {
            addCriterion("settle_staking_reward in", values, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardNotIn(List<BigDecimal> values) {
            addCriterion("settle_staking_reward not in", values, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("settle_staking_reward between", value1, value2, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andSettleStakingRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("settle_staking_reward not between", value1, value2, "settleStakingReward");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginIsNull() {
            addCriterion("add_issue_begin is null");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginIsNotNull() {
            addCriterion("add_issue_begin is not null");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginEqualTo(Long value) {
            addCriterion("add_issue_begin =", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginNotEqualTo(Long value) {
            addCriterion("add_issue_begin <>", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginGreaterThan(Long value) {
            addCriterion("add_issue_begin >", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginGreaterThanOrEqualTo(Long value) {
            addCriterion("add_issue_begin >=", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginLessThan(Long value) {
            addCriterion("add_issue_begin <", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginLessThanOrEqualTo(Long value) {
            addCriterion("add_issue_begin <=", value, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginIn(List<Long> values) {
            addCriterion("add_issue_begin in", values, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginNotIn(List<Long> values) {
            addCriterion("add_issue_begin not in", values, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginBetween(Long value1, Long value2) {
            addCriterion("add_issue_begin between", value1, value2, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueBeginNotBetween(Long value1, Long value2) {
            addCriterion("add_issue_begin not between", value1, value2, "addIssueBegin");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndIsNull() {
            addCriterion("add_issue_end is null");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndIsNotNull() {
            addCriterion("add_issue_end is not null");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndEqualTo(Long value) {
            addCriterion("add_issue_end =", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndNotEqualTo(Long value) {
            addCriterion("add_issue_end <>", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndGreaterThan(Long value) {
            addCriterion("add_issue_end >", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndGreaterThanOrEqualTo(Long value) {
            addCriterion("add_issue_end >=", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndLessThan(Long value) {
            addCriterion("add_issue_end <", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndLessThanOrEqualTo(Long value) {
            addCriterion("add_issue_end <=", value, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndIn(List<Long> values) {
            addCriterion("add_issue_end in", values, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndNotIn(List<Long> values) {
            addCriterion("add_issue_end not in", values, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndBetween(Long value1, Long value2) {
            addCriterion("add_issue_end between", value1, value2, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andAddIssueEndNotBetween(Long value1, Long value2) {
            addCriterion("add_issue_end not between", value1, value2, "addIssueEnd");
            return (Criteria) this;
        }

        public Criteria andNextSettleIsNull() {
            addCriterion("next_settle is null");
            return (Criteria) this;
        }

        public Criteria andNextSettleIsNotNull() {
            addCriterion("next_settle is not null");
            return (Criteria) this;
        }

        public Criteria andNextSettleEqualTo(Long value) {
            addCriterion("next_settle =", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleNotEqualTo(Long value) {
            addCriterion("next_settle <>", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleGreaterThan(Long value) {
            addCriterion("next_settle >", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleGreaterThanOrEqualTo(Long value) {
            addCriterion("next_settle >=", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleLessThan(Long value) {
            addCriterion("next_settle <", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleLessThanOrEqualTo(Long value) {
            addCriterion("next_settle <=", value, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleIn(List<Long> values) {
            addCriterion("next_settle in", values, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleNotIn(List<Long> values) {
            addCriterion("next_settle not in", values, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleBetween(Long value1, Long value2) {
            addCriterion("next_settle between", value1, value2, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNextSettleNotBetween(Long value1, Long value2) {
            addCriterion("next_settle not between", value1, value2, "nextSettle");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqIsNull() {
            addCriterion("node_opt_seq is null");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqIsNotNull() {
            addCriterion("node_opt_seq is not null");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqEqualTo(Long value) {
            addCriterion("node_opt_seq =", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqNotEqualTo(Long value) {
            addCriterion("node_opt_seq <>", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqGreaterThan(Long value) {
            addCriterion("node_opt_seq >", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqGreaterThanOrEqualTo(Long value) {
            addCriterion("node_opt_seq >=", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqLessThan(Long value) {
            addCriterion("node_opt_seq <", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqLessThanOrEqualTo(Long value) {
            addCriterion("node_opt_seq <=", value, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqIn(List<Long> values) {
            addCriterion("node_opt_seq in", values, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqNotIn(List<Long> values) {
            addCriterion("node_opt_seq not in", values, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqBetween(Long value1, Long value2) {
            addCriterion("node_opt_seq between", value1, value2, "nodeOptSeq");
            return (Criteria) this;
        }

        public Criteria andNodeOptSeqNotBetween(Long value1, Long value2) {
            addCriterion("node_opt_seq not between", value1, value2, "nodeOptSeq");
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

        public Criteria andAvgPackTimeIsNull() {
            addCriterion("avg_pack_time is null");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeIsNotNull() {
            addCriterion("avg_pack_time is not null");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeEqualTo(Long value) {
            addCriterion("avg_pack_time =", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeNotEqualTo(Long value) {
            addCriterion("avg_pack_time <>", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeGreaterThan(Long value) {
            addCriterion("avg_pack_time >", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("avg_pack_time >=", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeLessThan(Long value) {
            addCriterion("avg_pack_time <", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeLessThanOrEqualTo(Long value) {
            addCriterion("avg_pack_time <=", value, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeIn(List<Long> values) {
            addCriterion("avg_pack_time in", values, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeNotIn(List<Long> values) {
            addCriterion("avg_pack_time not in", values, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeBetween(Long value1, Long value2) {
            addCriterion("avg_pack_time between", value1, value2, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andAvgPackTimeNotBetween(Long value1, Long value2) {
            addCriterion("avg_pack_time not between", value1, value2, "avgPackTime");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIsNull() {
            addCriterion("erc721_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIsNotNull() {
            addCriterion("erc721_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyEqualTo(Integer value) {
            addCriterion("erc721_tx_qty =", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotEqualTo(Integer value) {
            addCriterion("erc721_tx_qty <>", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyGreaterThan(Integer value) {
            addCriterion("erc721_tx_qty >", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("erc721_tx_qty >=", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyLessThan(Integer value) {
            addCriterion("erc721_tx_qty <", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("erc721_tx_qty <=", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIn(List<Integer> values) {
            addCriterion("erc721_tx_qty in", values, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotIn(List<Integer> values) {
            addCriterion("erc721_tx_qty not in", values, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyBetween(Integer value1, Integer value2) {
            addCriterion("erc721_tx_qty between", value1, value2, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("erc721_tx_qty not between", value1, value2, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIsNull() {
            addCriterion("erc20_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIsNotNull() {
            addCriterion("erc20_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyEqualTo(Integer value) {
            addCriterion("erc20_tx_qty =", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotEqualTo(Integer value) {
            addCriterion("erc20_tx_qty <>", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyGreaterThan(Integer value) {
            addCriterion("erc20_tx_qty >", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("erc20_tx_qty >=", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyLessThan(Integer value) {
            addCriterion("erc20_tx_qty <", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("erc20_tx_qty <=", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIn(List<Integer> values) {
            addCriterion("erc20_tx_qty in", values, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotIn(List<Integer> values) {
            addCriterion("erc20_tx_qty not in", values, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyBetween(Integer value1, Integer value2) {
            addCriterion("erc20_tx_qty between", value1, value2, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("erc20_tx_qty not between", value1, value2, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andYearNumIsNull() {
            addCriterion("year_num is null");
            return (Criteria) this;
        }

        public Criteria andYearNumIsNotNull() {
            addCriterion("year_num is not null");
            return (Criteria) this;
        }

        public Criteria andYearNumEqualTo(Integer value) {
            addCriterion("year_num =", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumNotEqualTo(Integer value) {
            addCriterion("year_num <>", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumGreaterThan(Integer value) {
            addCriterion("year_num >", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("year_num >=", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumLessThan(Integer value) {
            addCriterion("year_num <", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumLessThanOrEqualTo(Integer value) {
            addCriterion("year_num <=", value, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumIn(List<Integer> values) {
            addCriterion("year_num in", values, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumNotIn(List<Integer> values) {
            addCriterion("year_num not in", values, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumBetween(Integer value1, Integer value2) {
            addCriterion("year_num between", value1, value2, "yearNum");
            return (Criteria) this;
        }

        public Criteria andYearNumNotBetween(Integer value1, Integer value2) {
            addCriterion("year_num not between", value1, value2, "yearNum");
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