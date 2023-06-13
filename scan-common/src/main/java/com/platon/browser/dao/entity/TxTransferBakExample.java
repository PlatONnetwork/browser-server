package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TxTransferBakExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TxTransferBakExample() {
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

        public Criteria andSeqIsNull() {
            addCriterion("seq is null");
            return (Criteria) this;
        }

        public Criteria andSeqIsNotNull() {
            addCriterion("seq is not null");
            return (Criteria) this;
        }

        public Criteria andSeqEqualTo(Long value) {
            addCriterion("seq =", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotEqualTo(Long value) {
            addCriterion("seq <>", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThan(Long value) {
            addCriterion("seq >", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThanOrEqualTo(Long value) {
            addCriterion("seq >=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThan(Long value) {
            addCriterion("seq <", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThanOrEqualTo(Long value) {
            addCriterion("seq <=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqIn(List<Long> values) {
            addCriterion("seq in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotIn(List<Long> values) {
            addCriterion("seq not in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqBetween(Long value1, Long value2) {
            addCriterion("seq between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotBetween(Long value1, Long value2) {
            addCriterion("seq not between", value1, value2, "seq");
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

        public Criteria andFromTypeIsNull() {
            addCriterion("from_type is null");
            return (Criteria) this;
        }

        public Criteria andFromTypeIsNotNull() {
            addCriterion("from_type is not null");
            return (Criteria) this;
        }

        public Criteria andFromTypeEqualTo(Integer value) {
            addCriterion("from_type =", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotEqualTo(Integer value) {
            addCriterion("from_type <>", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThan(Integer value) {
            addCriterion("from_type >", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("from_type >=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThan(Integer value) {
            addCriterion("from_type <", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThanOrEqualTo(Integer value) {
            addCriterion("from_type <=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeIn(List<Integer> values) {
            addCriterion("from_type in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotIn(List<Integer> values) {
            addCriterion("from_type not in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeBetween(Integer value1, Integer value2) {
            addCriterion("from_type between", value1, value2, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("from_type not between", value1, value2, "fromType");
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

        public Criteria andToTypeIsNull() {
            addCriterion("to_type is null");
            return (Criteria) this;
        }

        public Criteria andToTypeIsNotNull() {
            addCriterion("to_type is not null");
            return (Criteria) this;
        }

        public Criteria andToTypeEqualTo(Integer value) {
            addCriterion("to_type =", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeNotEqualTo(Integer value) {
            addCriterion("to_type <>", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeGreaterThan(Integer value) {
            addCriterion("to_type >", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("to_type >=", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeLessThan(Integer value) {
            addCriterion("to_type <", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeLessThanOrEqualTo(Integer value) {
            addCriterion("to_type <=", value, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeIn(List<Integer> values) {
            addCriterion("to_type in", values, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeNotIn(List<Integer> values) {
            addCriterion("to_type not in", values, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeBetween(Integer value1, Integer value2) {
            addCriterion("to_type between", value1, value2, "toType");
            return (Criteria) this;
        }

        public Criteria andToTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("to_type not between", value1, value2, "toType");
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

        public Criteria andBnIsNull() {
            addCriterion("bn is null");
            return (Criteria) this;
        }

        public Criteria andBnIsNotNull() {
            addCriterion("bn is not null");
            return (Criteria) this;
        }

        public Criteria andBnEqualTo(Long value) {
            addCriterion("bn =", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnNotEqualTo(Long value) {
            addCriterion("bn <>", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnGreaterThan(Long value) {
            addCriterion("bn >", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnGreaterThanOrEqualTo(Long value) {
            addCriterion("bn >=", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnLessThan(Long value) {
            addCriterion("bn <", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnLessThanOrEqualTo(Long value) {
            addCriterion("bn <=", value, "bn");
            return (Criteria) this;
        }

        public Criteria andBnIn(List<Long> values) {
            addCriterion("bn in", values, "bn");
            return (Criteria) this;
        }

        public Criteria andBnNotIn(List<Long> values) {
            addCriterion("bn not in", values, "bn");
            return (Criteria) this;
        }

        public Criteria andBnBetween(Long value1, Long value2) {
            addCriterion("bn between", value1, value2, "bn");
            return (Criteria) this;
        }

        public Criteria andBnNotBetween(Long value1, Long value2) {
            addCriterion("bn not between", value1, value2, "bn");
            return (Criteria) this;
        }

        public Criteria andBTimeIsNull() {
            addCriterion("b_time is null");
            return (Criteria) this;
        }

        public Criteria andBTimeIsNotNull() {
            addCriterion("b_time is not null");
            return (Criteria) this;
        }

        public Criteria andBTimeEqualTo(Date value) {
            addCriterion("b_time =", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeNotEqualTo(Date value) {
            addCriterion("b_time <>", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeGreaterThan(Date value) {
            addCriterion("b_time >", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("b_time >=", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeLessThan(Date value) {
            addCriterion("b_time <", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeLessThanOrEqualTo(Date value) {
            addCriterion("b_time <=", value, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeIn(List<Date> values) {
            addCriterion("b_time in", values, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeNotIn(List<Date> values) {
            addCriterion("b_time not in", values, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeBetween(Date value1, Date value2) {
            addCriterion("b_time between", value1, value2, "bTime");
            return (Criteria) this;
        }

        public Criteria andBTimeNotBetween(Date value1, Date value2) {
            addCriterion("b_time not between", value1, value2, "bTime");
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