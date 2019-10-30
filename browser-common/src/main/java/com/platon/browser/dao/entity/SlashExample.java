package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlashExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SlashExample() {
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

        public Criteria andSlashRateIsNull() {
            addCriterion("slash_rate is null");
            return (Criteria) this;
        }

        public Criteria andSlashRateIsNotNull() {
            addCriterion("slash_rate is not null");
            return (Criteria) this;
        }

        public Criteria andSlashRateEqualTo(String value) {
            addCriterion("slash_rate =", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotEqualTo(String value) {
            addCriterion("slash_rate <>", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateGreaterThan(String value) {
            addCriterion("slash_rate >", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateGreaterThanOrEqualTo(String value) {
            addCriterion("slash_rate >=", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateLessThan(String value) {
            addCriterion("slash_rate <", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateLessThanOrEqualTo(String value) {
            addCriterion("slash_rate <=", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateLike(String value) {
            addCriterion("slash_rate like", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotLike(String value) {
            addCriterion("slash_rate not like", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateIn(List<String> values) {
            addCriterion("slash_rate in", values, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotIn(List<String> values) {
            addCriterion("slash_rate not in", values, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateBetween(String value1, String value2) {
            addCriterion("slash_rate between", value1, value2, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotBetween(String value1, String value2) {
            addCriterion("slash_rate not between", value1, value2, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashValueIsNull() {
            addCriterion("slash_value is null");
            return (Criteria) this;
        }

        public Criteria andSlashValueIsNotNull() {
            addCriterion("slash_value is not null");
            return (Criteria) this;
        }

        public Criteria andSlashValueEqualTo(BigDecimal value) {
            addCriterion("slash_value =", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueNotEqualTo(BigDecimal value) {
            addCriterion("slash_value <>", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueGreaterThan(BigDecimal value) {
            addCriterion("slash_value >", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_value >=", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueLessThan(BigDecimal value) {
            addCriterion("slash_value <", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_value <=", value, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueIn(List<BigDecimal> values) {
            addCriterion("slash_value in", values, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueNotIn(List<BigDecimal> values) {
            addCriterion("slash_value not in", values, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_value between", value1, value2, "slashValue");
            return (Criteria) this;
        }

        public Criteria andSlashValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_value not between", value1, value2, "slashValue");
            return (Criteria) this;
        }

        public Criteria andRewardIsNull() {
            addCriterion("reward is null");
            return (Criteria) this;
        }

        public Criteria andRewardIsNotNull() {
            addCriterion("reward is not null");
            return (Criteria) this;
        }

        public Criteria andRewardEqualTo(BigDecimal value) {
            addCriterion("reward =", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotEqualTo(BigDecimal value) {
            addCriterion("reward <>", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThan(BigDecimal value) {
            addCriterion("reward >", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reward >=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThan(BigDecimal value) {
            addCriterion("reward <", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reward <=", value, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardIn(List<BigDecimal> values) {
            addCriterion("reward in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotIn(List<BigDecimal> values) {
            addCriterion("reward not in", values, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward between", value1, value2, "reward");
            return (Criteria) this;
        }

        public Criteria andRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward not between", value1, value2, "reward");
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

        public Criteria andIsQuitIsNull() {
            addCriterion("is_quit is null");
            return (Criteria) this;
        }

        public Criteria andIsQuitIsNotNull() {
            addCriterion("is_quit is not null");
            return (Criteria) this;
        }

        public Criteria andIsQuitEqualTo(Integer value) {
            addCriterion("is_quit =", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitNotEqualTo(Integer value) {
            addCriterion("is_quit <>", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitGreaterThan(Integer value) {
            addCriterion("is_quit >", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_quit >=", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitLessThan(Integer value) {
            addCriterion("is_quit <", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitLessThanOrEqualTo(Integer value) {
            addCriterion("is_quit <=", value, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitIn(List<Integer> values) {
            addCriterion("is_quit in", values, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitNotIn(List<Integer> values) {
            addCriterion("is_quit not in", values, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitBetween(Integer value1, Integer value2) {
            addCriterion("is_quit between", value1, value2, "isQuit");
            return (Criteria) this;
        }

        public Criteria andIsQuitNotBetween(Integer value1, Integer value2) {
            addCriterion("is_quit not between", value1, value2, "isQuit");
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