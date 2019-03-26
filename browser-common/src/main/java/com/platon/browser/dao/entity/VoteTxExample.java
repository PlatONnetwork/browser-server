package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoteTxExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VoteTxExample() {
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

        public Criteria andVaildCountIsNull() {
            addCriterion("vaild_count is null");
            return (Criteria) this;
        }

        public Criteria andVaildCountIsNotNull() {
            addCriterion("vaild_count is not null");
            return (Criteria) this;
        }

        public Criteria andVaildCountEqualTo(Long value) {
            addCriterion("vaild_count =", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountNotEqualTo(Long value) {
            addCriterion("vaild_count <>", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountGreaterThan(Long value) {
            addCriterion("vaild_count >", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountGreaterThanOrEqualTo(Long value) {
            addCriterion("vaild_count >=", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountLessThan(Long value) {
            addCriterion("vaild_count <", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountLessThanOrEqualTo(Long value) {
            addCriterion("vaild_count <=", value, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountIn(List<Long> values) {
            addCriterion("vaild_count in", values, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountNotIn(List<Long> values) {
            addCriterion("vaild_count not in", values, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountBetween(Long value1, Long value2) {
            addCriterion("vaild_count between", value1, value2, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andVaildCountNotBetween(Long value1, Long value2) {
            addCriterion("vaild_count not between", value1, value2, "vaildCount");
            return (Criteria) this;
        }

        public Criteria andTotalsIsNull() {
            addCriterion("totals is null");
            return (Criteria) this;
        }

        public Criteria andTotalsIsNotNull() {
            addCriterion("totals is not null");
            return (Criteria) this;
        }

        public Criteria andTotalsEqualTo(Long value) {
            addCriterion("totals =", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsNotEqualTo(Long value) {
            addCriterion("totals <>", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsGreaterThan(Long value) {
            addCriterion("totals >", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsGreaterThanOrEqualTo(Long value) {
            addCriterion("totals >=", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsLessThan(Long value) {
            addCriterion("totals <", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsLessThanOrEqualTo(Long value) {
            addCriterion("totals <=", value, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsIn(List<Long> values) {
            addCriterion("totals in", values, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsNotIn(List<Long> values) {
            addCriterion("totals not in", values, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsBetween(Long value1, Long value2) {
            addCriterion("totals between", value1, value2, "totals");
            return (Criteria) this;
        }

        public Criteria andTotalsNotBetween(Long value1, Long value2) {
            addCriterion("totals not between", value1, value2, "totals");
            return (Criteria) this;
        }

        public Criteria andDeadLineIsNull() {
            addCriterion("dead_line is null");
            return (Criteria) this;
        }

        public Criteria andDeadLineIsNotNull() {
            addCriterion("dead_line is not null");
            return (Criteria) this;
        }

        public Criteria andDeadLineEqualTo(Date value) {
            addCriterion("dead_line =", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineNotEqualTo(Date value) {
            addCriterion("dead_line <>", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineGreaterThan(Date value) {
            addCriterion("dead_line >", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineGreaterThanOrEqualTo(Date value) {
            addCriterion("dead_line >=", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineLessThan(Date value) {
            addCriterion("dead_line <", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineLessThanOrEqualTo(Date value) {
            addCriterion("dead_line <=", value, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineIn(List<Date> values) {
            addCriterion("dead_line in", values, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineNotIn(List<Date> values) {
            addCriterion("dead_line not in", values, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineBetween(Date value1, Date value2) {
            addCriterion("dead_line between", value1, value2, "deadLine");
            return (Criteria) this;
        }

        public Criteria andDeadLineNotBetween(Date value1, Date value2) {
            addCriterion("dead_line not between", value1, value2, "deadLine");
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

        public Criteria andCompleteFlagIsNull() {
            addCriterion("complete_flag is null");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagIsNotNull() {
            addCriterion("complete_flag is not null");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagEqualTo(String value) {
            addCriterion("complete_flag =", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagNotEqualTo(String value) {
            addCriterion("complete_flag <>", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagGreaterThan(String value) {
            addCriterion("complete_flag >", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagGreaterThanOrEqualTo(String value) {
            addCriterion("complete_flag >=", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagLessThan(String value) {
            addCriterion("complete_flag <", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagLessThanOrEqualTo(String value) {
            addCriterion("complete_flag <=", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagLike(String value) {
            addCriterion("complete_flag like", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagNotLike(String value) {
            addCriterion("complete_flag not like", value, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagIn(List<String> values) {
            addCriterion("complete_flag in", values, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagNotIn(List<String> values) {
            addCriterion("complete_flag not in", values, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagBetween(String value1, String value2) {
            addCriterion("complete_flag between", value1, value2, "completeFlag");
            return (Criteria) this;
        }

        public Criteria andCompleteFlagNotBetween(String value1, String value2) {
            addCriterion("complete_flag not between", value1, value2, "completeFlag");
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