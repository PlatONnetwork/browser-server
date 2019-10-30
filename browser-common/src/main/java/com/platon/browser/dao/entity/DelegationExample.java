package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DelegationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DelegationExample() {
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

        public Criteria andDelegateAddrIsNull() {
            addCriterion("delegate_addr is null");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrIsNotNull() {
            addCriterion("delegate_addr is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrEqualTo(String value) {
            addCriterion("delegate_addr =", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrNotEqualTo(String value) {
            addCriterion("delegate_addr <>", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrGreaterThan(String value) {
            addCriterion("delegate_addr >", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrGreaterThanOrEqualTo(String value) {
            addCriterion("delegate_addr >=", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrLessThan(String value) {
            addCriterion("delegate_addr <", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrLessThanOrEqualTo(String value) {
            addCriterion("delegate_addr <=", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrLike(String value) {
            addCriterion("delegate_addr like", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrNotLike(String value) {
            addCriterion("delegate_addr not like", value, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrIn(List<String> values) {
            addCriterion("delegate_addr in", values, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrNotIn(List<String> values) {
            addCriterion("delegate_addr not in", values, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrBetween(String value1, String value2) {
            addCriterion("delegate_addr between", value1, value2, "delegateAddr");
            return (Criteria) this;
        }

        public Criteria andDelegateAddrNotBetween(String value1, String value2) {
            addCriterion("delegate_addr not between", value1, value2, "delegateAddr");
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

        public Criteria andDelegateHesIsNull() {
            addCriterion("delegate_hes is null");
            return (Criteria) this;
        }

        public Criteria andDelegateHesIsNotNull() {
            addCriterion("delegate_hes is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateHesEqualTo(BigDecimal value) {
            addCriterion("delegate_hes =", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesNotEqualTo(BigDecimal value) {
            addCriterion("delegate_hes <>", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesGreaterThan(BigDecimal value) {
            addCriterion("delegate_hes >", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_hes >=", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesLessThan(BigDecimal value) {
            addCriterion("delegate_hes <", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_hes <=", value, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesIn(List<BigDecimal> values) {
            addCriterion("delegate_hes in", values, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesNotIn(List<BigDecimal> values) {
            addCriterion("delegate_hes not in", values, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_hes between", value1, value2, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateHesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_hes not between", value1, value2, "delegateHes");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedIsNull() {
            addCriterion("delegate_locked is null");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedIsNotNull() {
            addCriterion("delegate_locked is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedEqualTo(BigDecimal value) {
            addCriterion("delegate_locked =", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotEqualTo(BigDecimal value) {
            addCriterion("delegate_locked <>", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedGreaterThan(BigDecimal value) {
            addCriterion("delegate_locked >", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_locked >=", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedLessThan(BigDecimal value) {
            addCriterion("delegate_locked <", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_locked <=", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedIn(List<BigDecimal> values) {
            addCriterion("delegate_locked in", values, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotIn(List<BigDecimal> values) {
            addCriterion("delegate_locked not in", values, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_locked between", value1, value2, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_locked not between", value1, value2, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedIsNull() {
            addCriterion("delegate_released is null");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedIsNotNull() {
            addCriterion("delegate_released is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedEqualTo(BigDecimal value) {
            addCriterion("delegate_released =", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedNotEqualTo(BigDecimal value) {
            addCriterion("delegate_released <>", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedGreaterThan(BigDecimal value) {
            addCriterion("delegate_released >", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_released >=", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedLessThan(BigDecimal value) {
            addCriterion("delegate_released <", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_released <=", value, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedIn(List<BigDecimal> values) {
            addCriterion("delegate_released in", values, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedNotIn(List<BigDecimal> values) {
            addCriterion("delegate_released not in", values, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_released between", value1, value2, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andDelegateReleasedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_released not between", value1, value2, "delegateReleased");
            return (Criteria) this;
        }

        public Criteria andIsHistoryIsNull() {
            addCriterion("is_history is null");
            return (Criteria) this;
        }

        public Criteria andIsHistoryIsNotNull() {
            addCriterion("is_history is not null");
            return (Criteria) this;
        }

        public Criteria andIsHistoryEqualTo(Integer value) {
            addCriterion("is_history =", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryNotEqualTo(Integer value) {
            addCriterion("is_history <>", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryGreaterThan(Integer value) {
            addCriterion("is_history >", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_history >=", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryLessThan(Integer value) {
            addCriterion("is_history <", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryLessThanOrEqualTo(Integer value) {
            addCriterion("is_history <=", value, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryIn(List<Integer> values) {
            addCriterion("is_history in", values, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryNotIn(List<Integer> values) {
            addCriterion("is_history not in", values, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryBetween(Integer value1, Integer value2) {
            addCriterion("is_history between", value1, value2, "isHistory");
            return (Criteria) this;
        }

        public Criteria andIsHistoryNotBetween(Integer value1, Integer value2) {
            addCriterion("is_history not between", value1, value2, "isHistory");
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

        public Criteria andCurDelegationBlockNumIsNull() {
            addCriterion("cur_delegation_block_num is null");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumIsNotNull() {
            addCriterion("cur_delegation_block_num is not null");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumEqualTo(Long value) {
            addCriterion("cur_delegation_block_num =", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumNotEqualTo(Long value) {
            addCriterion("cur_delegation_block_num <>", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumGreaterThan(Long value) {
            addCriterion("cur_delegation_block_num >", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumGreaterThanOrEqualTo(Long value) {
            addCriterion("cur_delegation_block_num >=", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumLessThan(Long value) {
            addCriterion("cur_delegation_block_num <", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumLessThanOrEqualTo(Long value) {
            addCriterion("cur_delegation_block_num <=", value, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumIn(List<Long> values) {
            addCriterion("cur_delegation_block_num in", values, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumNotIn(List<Long> values) {
            addCriterion("cur_delegation_block_num not in", values, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumBetween(Long value1, Long value2) {
            addCriterion("cur_delegation_block_num between", value1, value2, "curDelegationBlockNum");
            return (Criteria) this;
        }

        public Criteria andCurDelegationBlockNumNotBetween(Long value1, Long value2) {
            addCriterion("cur_delegation_block_num not between", value1, value2, "curDelegationBlockNum");
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