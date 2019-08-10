package com.platon.browser.dao.entity;

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

        public Criteria andDelegateHasIsNull() {
            addCriterion("delegate_has is null");
            return (Criteria) this;
        }

        public Criteria andDelegateHasIsNotNull() {
            addCriterion("delegate_has is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateHasEqualTo(String value) {
            addCriterion("delegate_has =", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasNotEqualTo(String value) {
            addCriterion("delegate_has <>", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasGreaterThan(String value) {
            addCriterion("delegate_has >", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasGreaterThanOrEqualTo(String value) {
            addCriterion("delegate_has >=", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasLessThan(String value) {
            addCriterion("delegate_has <", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasLessThanOrEqualTo(String value) {
            addCriterion("delegate_has <=", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasLike(String value) {
            addCriterion("delegate_has like", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasNotLike(String value) {
            addCriterion("delegate_has not like", value, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasIn(List<String> values) {
            addCriterion("delegate_has in", values, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasNotIn(List<String> values) {
            addCriterion("delegate_has not in", values, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasBetween(String value1, String value2) {
            addCriterion("delegate_has between", value1, value2, "delegateHas");
            return (Criteria) this;
        }

        public Criteria andDelegateHasNotBetween(String value1, String value2) {
            addCriterion("delegate_has not between", value1, value2, "delegateHas");
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

        public Criteria andDelegateLockedEqualTo(String value) {
            addCriterion("delegate_locked =", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotEqualTo(String value) {
            addCriterion("delegate_locked <>", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedGreaterThan(String value) {
            addCriterion("delegate_locked >", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedGreaterThanOrEqualTo(String value) {
            addCriterion("delegate_locked >=", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedLessThan(String value) {
            addCriterion("delegate_locked <", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedLessThanOrEqualTo(String value) {
            addCriterion("delegate_locked <=", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedLike(String value) {
            addCriterion("delegate_locked like", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotLike(String value) {
            addCriterion("delegate_locked not like", value, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedIn(List<String> values) {
            addCriterion("delegate_locked in", values, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotIn(List<String> values) {
            addCriterion("delegate_locked not in", values, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedBetween(String value1, String value2) {
            addCriterion("delegate_locked between", value1, value2, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateLockedNotBetween(String value1, String value2) {
            addCriterion("delegate_locked not between", value1, value2, "delegateLocked");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionIsNull() {
            addCriterion("delegate_reduction is null");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionIsNotNull() {
            addCriterion("delegate_reduction is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionEqualTo(String value) {
            addCriterion("delegate_reduction =", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionNotEqualTo(String value) {
            addCriterion("delegate_reduction <>", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionGreaterThan(String value) {
            addCriterion("delegate_reduction >", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionGreaterThanOrEqualTo(String value) {
            addCriterion("delegate_reduction >=", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionLessThan(String value) {
            addCriterion("delegate_reduction <", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionLessThanOrEqualTo(String value) {
            addCriterion("delegate_reduction <=", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionLike(String value) {
            addCriterion("delegate_reduction like", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionNotLike(String value) {
            addCriterion("delegate_reduction not like", value, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionIn(List<String> values) {
            addCriterion("delegate_reduction in", values, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionNotIn(List<String> values) {
            addCriterion("delegate_reduction not in", values, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionBetween(String value1, String value2) {
            addCriterion("delegate_reduction between", value1, value2, "delegateReduction");
            return (Criteria) this;
        }

        public Criteria andDelegateReductionNotBetween(String value1, String value2) {
            addCriterion("delegate_reduction not between", value1, value2, "delegateReduction");
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