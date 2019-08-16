package com.platon.browser.dao.entity;

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

        public Criteria andCurrentNumberIsNull() {
            addCriterion("current_number is null");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberIsNotNull() {
            addCriterion("current_number is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberEqualTo(Long value) {
            addCriterion("current_number =", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberNotEqualTo(Long value) {
            addCriterion("current_number <>", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberGreaterThan(Long value) {
            addCriterion("current_number >", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("current_number >=", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberLessThan(Long value) {
            addCriterion("current_number <", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberLessThanOrEqualTo(Long value) {
            addCriterion("current_number <=", value, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberIn(List<Long> values) {
            addCriterion("current_number in", values, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberNotIn(List<Long> values) {
            addCriterion("current_number not in", values, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberBetween(Long value1, Long value2) {
            addCriterion("current_number between", value1, value2, "currentNumber");
            return (Criteria) this;
        }

        public Criteria andCurrentNumberNotBetween(Long value1, Long value2) {
            addCriterion("current_number not between", value1, value2, "currentNumber");
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

        public Criteria andCurrentTpsIsNull() {
            addCriterion("current_tps is null");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsIsNotNull() {
            addCriterion("current_tps is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsEqualTo(Integer value) {
            addCriterion("current_tps =", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsNotEqualTo(Integer value) {
            addCriterion("current_tps <>", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsGreaterThan(Integer value) {
            addCriterion("current_tps >", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsGreaterThanOrEqualTo(Integer value) {
            addCriterion("current_tps >=", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsLessThan(Integer value) {
            addCriterion("current_tps <", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsLessThanOrEqualTo(Integer value) {
            addCriterion("current_tps <=", value, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsIn(List<Integer> values) {
            addCriterion("current_tps in", values, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsNotIn(List<Integer> values) {
            addCriterion("current_tps not in", values, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsBetween(Integer value1, Integer value2) {
            addCriterion("current_tps between", value1, value2, "currentTps");
            return (Criteria) this;
        }

        public Criteria andCurrentTpsNotBetween(Integer value1, Integer value2) {
            addCriterion("current_tps not between", value1, value2, "currentTps");
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

        public Criteria andIssueValueEqualTo(String value) {
            addCriterion("issue_value =", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotEqualTo(String value) {
            addCriterion("issue_value <>", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueGreaterThan(String value) {
            addCriterion("issue_value >", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueGreaterThanOrEqualTo(String value) {
            addCriterion("issue_value >=", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueLessThan(String value) {
            addCriterion("issue_value <", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueLessThanOrEqualTo(String value) {
            addCriterion("issue_value <=", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueLike(String value) {
            addCriterion("issue_value like", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotLike(String value) {
            addCriterion("issue_value not like", value, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueIn(List<String> values) {
            addCriterion("issue_value in", values, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotIn(List<String> values) {
            addCriterion("issue_value not in", values, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueBetween(String value1, String value2) {
            addCriterion("issue_value between", value1, value2, "issueValue");
            return (Criteria) this;
        }

        public Criteria andIssueValueNotBetween(String value1, String value2) {
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

        public Criteria andTurnValueEqualTo(String value) {
            addCriterion("turn_value =", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotEqualTo(String value) {
            addCriterion("turn_value <>", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueGreaterThan(String value) {
            addCriterion("turn_value >", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueGreaterThanOrEqualTo(String value) {
            addCriterion("turn_value >=", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueLessThan(String value) {
            addCriterion("turn_value <", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueLessThanOrEqualTo(String value) {
            addCriterion("turn_value <=", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueLike(String value) {
            addCriterion("turn_value like", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotLike(String value) {
            addCriterion("turn_value not like", value, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueIn(List<String> values) {
            addCriterion("turn_value in", values, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotIn(List<String> values) {
            addCriterion("turn_value not in", values, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueBetween(String value1, String value2) {
            addCriterion("turn_value between", value1, value2, "turnValue");
            return (Criteria) this;
        }

        public Criteria andTurnValueNotBetween(String value1, String value2) {
            addCriterion("turn_value not between", value1, value2, "turnValue");
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

        public Criteria andStakingDelegationValueEqualTo(String value) {
            addCriterion("staking_delegation_value =", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotEqualTo(String value) {
            addCriterion("staking_delegation_value <>", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueGreaterThan(String value) {
            addCriterion("staking_delegation_value >", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueGreaterThanOrEqualTo(String value) {
            addCriterion("staking_delegation_value >=", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueLessThan(String value) {
            addCriterion("staking_delegation_value <", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueLessThanOrEqualTo(String value) {
            addCriterion("staking_delegation_value <=", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueLike(String value) {
            addCriterion("staking_delegation_value like", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotLike(String value) {
            addCriterion("staking_delegation_value not like", value, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueIn(List<String> values) {
            addCriterion("staking_delegation_value in", values, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotIn(List<String> values) {
            addCriterion("staking_delegation_value not in", values, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueBetween(String value1, String value2) {
            addCriterion("staking_delegation_value between", value1, value2, "stakingDelegationValue");
            return (Criteria) this;
        }

        public Criteria andStakingDelegationValueNotBetween(String value1, String value2) {
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

        public Criteria andStakingValueEqualTo(String value) {
            addCriterion("staking_value =", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotEqualTo(String value) {
            addCriterion("staking_value <>", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThan(String value) {
            addCriterion("staking_value >", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThanOrEqualTo(String value) {
            addCriterion("staking_value >=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThan(String value) {
            addCriterion("staking_value <", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThanOrEqualTo(String value) {
            addCriterion("staking_value <=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLike(String value) {
            addCriterion("staking_value like", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotLike(String value) {
            addCriterion("staking_value not like", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueIn(List<String> values) {
            addCriterion("staking_value in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotIn(List<String> values) {
            addCriterion("staking_value not in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueBetween(String value1, String value2) {
            addCriterion("staking_value between", value1, value2, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotBetween(String value1, String value2) {
            addCriterion("staking_value not between", value1, value2, "stakingValue");
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

        public Criteria andStakingRewardIsNull() {
            addCriterion("staking_reward is null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardIsNotNull() {
            addCriterion("staking_reward is not null");
            return (Criteria) this;
        }

        public Criteria andStakingRewardEqualTo(String value) {
            addCriterion("staking_reward =", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotEqualTo(String value) {
            addCriterion("staking_reward <>", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardGreaterThan(String value) {
            addCriterion("staking_reward >", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardGreaterThanOrEqualTo(String value) {
            addCriterion("staking_reward >=", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardLessThan(String value) {
            addCriterion("staking_reward <", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardLessThanOrEqualTo(String value) {
            addCriterion("staking_reward <=", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardLike(String value) {
            addCriterion("staking_reward like", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotLike(String value) {
            addCriterion("staking_reward not like", value, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardIn(List<String> values) {
            addCriterion("staking_reward in", values, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotIn(List<String> values) {
            addCriterion("staking_reward not in", values, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardBetween(String value1, String value2) {
            addCriterion("staking_reward between", value1, value2, "stakingReward");
            return (Criteria) this;
        }

        public Criteria andStakingRewardNotBetween(String value1, String value2) {
            addCriterion("staking_reward not between", value1, value2, "stakingReward");
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

        public Criteria andNextSettingIsNull() {
            addCriterion("next_setting is null");
            return (Criteria) this;
        }

        public Criteria andNextSettingIsNotNull() {
            addCriterion("next_setting is not null");
            return (Criteria) this;
        }

        public Criteria andNextSettingEqualTo(Long value) {
            addCriterion("next_setting =", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingNotEqualTo(Long value) {
            addCriterion("next_setting <>", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingGreaterThan(Long value) {
            addCriterion("next_setting >", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingGreaterThanOrEqualTo(Long value) {
            addCriterion("next_setting >=", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingLessThan(Long value) {
            addCriterion("next_setting <", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingLessThanOrEqualTo(Long value) {
            addCriterion("next_setting <=", value, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingIn(List<Long> values) {
            addCriterion("next_setting in", values, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingNotIn(List<Long> values) {
            addCriterion("next_setting not in", values, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingBetween(Long value1, Long value2) {
            addCriterion("next_setting between", value1, value2, "nextSetting");
            return (Criteria) this;
        }

        public Criteria andNextSettingNotBetween(Long value1, Long value2) {
            addCriterion("next_setting not between", value1, value2, "nextSetting");
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