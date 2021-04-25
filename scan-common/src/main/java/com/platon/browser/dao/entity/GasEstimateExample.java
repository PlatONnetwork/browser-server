package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class GasEstimateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GasEstimateExample() {
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

        public Criteria andAddrIsNull() {
            addCriterion("addr is null");
            return (Criteria) this;
        }

        public Criteria andAddrIsNotNull() {
            addCriterion("addr is not null");
            return (Criteria) this;
        }

        public Criteria andAddrEqualTo(String value) {
            addCriterion("addr =", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotEqualTo(String value) {
            addCriterion("addr <>", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrGreaterThan(String value) {
            addCriterion("addr >", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrGreaterThanOrEqualTo(String value) {
            addCriterion("addr >=", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLessThan(String value) {
            addCriterion("addr <", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLessThanOrEqualTo(String value) {
            addCriterion("addr <=", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrLike(String value) {
            addCriterion("addr like", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotLike(String value) {
            addCriterion("addr not like", value, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrIn(List<String> values) {
            addCriterion("addr in", values, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotIn(List<String> values) {
            addCriterion("addr not in", values, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrBetween(String value1, String value2) {
            addCriterion("addr between", value1, value2, "addr");
            return (Criteria) this;
        }

        public Criteria andAddrNotBetween(String value1, String value2) {
            addCriterion("addr not between", value1, value2, "addr");
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

        public Criteria andSbnIsNull() {
            addCriterion("sbn is null");
            return (Criteria) this;
        }

        public Criteria andSbnIsNotNull() {
            addCriterion("sbn is not null");
            return (Criteria) this;
        }

        public Criteria andSbnEqualTo(Long value) {
            addCriterion("sbn =", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnNotEqualTo(Long value) {
            addCriterion("sbn <>", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnGreaterThan(Long value) {
            addCriterion("sbn >", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnGreaterThanOrEqualTo(Long value) {
            addCriterion("sbn >=", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnLessThan(Long value) {
            addCriterion("sbn <", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnLessThanOrEqualTo(Long value) {
            addCriterion("sbn <=", value, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnIn(List<Long> values) {
            addCriterion("sbn in", values, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnNotIn(List<Long> values) {
            addCriterion("sbn not in", values, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnBetween(Long value1, Long value2) {
            addCriterion("sbn between", value1, value2, "sbn");
            return (Criteria) this;
        }

        public Criteria andSbnNotBetween(Long value1, Long value2) {
            addCriterion("sbn not between", value1, value2, "sbn");
            return (Criteria) this;
        }

        public Criteria andEpochIsNull() {
            addCriterion("epoch is null");
            return (Criteria) this;
        }

        public Criteria andEpochIsNotNull() {
            addCriterion("epoch is not null");
            return (Criteria) this;
        }

        public Criteria andEpochEqualTo(Long value) {
            addCriterion("epoch =", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochNotEqualTo(Long value) {
            addCriterion("epoch <>", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochGreaterThan(Long value) {
            addCriterion("epoch >", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochGreaterThanOrEqualTo(Long value) {
            addCriterion("epoch >=", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochLessThan(Long value) {
            addCriterion("epoch <", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochLessThanOrEqualTo(Long value) {
            addCriterion("epoch <=", value, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochIn(List<Long> values) {
            addCriterion("epoch in", values, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochNotIn(List<Long> values) {
            addCriterion("epoch not in", values, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochBetween(Long value1, Long value2) {
            addCriterion("epoch between", value1, value2, "epoch");
            return (Criteria) this;
        }

        public Criteria andEpochNotBetween(Long value1, Long value2) {
            addCriterion("epoch not between", value1, value2, "epoch");
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