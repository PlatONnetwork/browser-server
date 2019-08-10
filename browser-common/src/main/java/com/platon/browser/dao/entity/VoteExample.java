package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoteExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VoteExample() {
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

        public Criteria andVerifierNameIsNull() {
            addCriterion("verifier_name is null");
            return (Criteria) this;
        }

        public Criteria andVerifierNameIsNotNull() {
            addCriterion("verifier_name is not null");
            return (Criteria) this;
        }

        public Criteria andVerifierNameEqualTo(String value) {
            addCriterion("verifier_name =", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotEqualTo(String value) {
            addCriterion("verifier_name <>", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameGreaterThan(String value) {
            addCriterion("verifier_name >", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameGreaterThanOrEqualTo(String value) {
            addCriterion("verifier_name >=", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLessThan(String value) {
            addCriterion("verifier_name <", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLessThanOrEqualTo(String value) {
            addCriterion("verifier_name <=", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameLike(String value) {
            addCriterion("verifier_name like", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotLike(String value) {
            addCriterion("verifier_name not like", value, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameIn(List<String> values) {
            addCriterion("verifier_name in", values, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotIn(List<String> values) {
            addCriterion("verifier_name not in", values, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameBetween(String value1, String value2) {
            addCriterion("verifier_name between", value1, value2, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierNameNotBetween(String value1, String value2) {
            addCriterion("verifier_name not between", value1, value2, "verifierName");
            return (Criteria) this;
        }

        public Criteria andVerifierIsNull() {
            addCriterion("verifier is null");
            return (Criteria) this;
        }

        public Criteria andVerifierIsNotNull() {
            addCriterion("verifier is not null");
            return (Criteria) this;
        }

        public Criteria andVerifierEqualTo(String value) {
            addCriterion("verifier =", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotEqualTo(String value) {
            addCriterion("verifier <>", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierGreaterThan(String value) {
            addCriterion("verifier >", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierGreaterThanOrEqualTo(String value) {
            addCriterion("verifier >=", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLessThan(String value) {
            addCriterion("verifier <", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLessThanOrEqualTo(String value) {
            addCriterion("verifier <=", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierLike(String value) {
            addCriterion("verifier like", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotLike(String value) {
            addCriterion("verifier not like", value, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierIn(List<String> values) {
            addCriterion("verifier in", values, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotIn(List<String> values) {
            addCriterion("verifier not in", values, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierBetween(String value1, String value2) {
            addCriterion("verifier between", value1, value2, "verifier");
            return (Criteria) this;
        }

        public Criteria andVerifierNotBetween(String value1, String value2) {
            addCriterion("verifier not between", value1, value2, "verifier");
            return (Criteria) this;
        }

        public Criteria andOptionIsNull() {
            addCriterion("`option` is null");
            return (Criteria) this;
        }

        public Criteria andOptionIsNotNull() {
            addCriterion("`option` is not null");
            return (Criteria) this;
        }

        public Criteria andOptionEqualTo(String value) {
            addCriterion("`option` =", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionNotEqualTo(String value) {
            addCriterion("`option` <>", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionGreaterThan(String value) {
            addCriterion("`option` >", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionGreaterThanOrEqualTo(String value) {
            addCriterion("`option` >=", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionLessThan(String value) {
            addCriterion("`option` <", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionLessThanOrEqualTo(String value) {
            addCriterion("`option` <=", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionLike(String value) {
            addCriterion("`option` like", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionNotLike(String value) {
            addCriterion("`option` not like", value, "option");
            return (Criteria) this;
        }

        public Criteria andOptionIn(List<String> values) {
            addCriterion("`option` in", values, "option");
            return (Criteria) this;
        }

        public Criteria andOptionNotIn(List<String> values) {
            addCriterion("`option` not in", values, "option");
            return (Criteria) this;
        }

        public Criteria andOptionBetween(String value1, String value2) {
            addCriterion("`option` between", value1, value2, "option");
            return (Criteria) this;
        }

        public Criteria andOptionNotBetween(String value1, String value2) {
            addCriterion("`option` not between", value1, value2, "option");
            return (Criteria) this;
        }

        public Criteria andProposalHashIsNull() {
            addCriterion("proposal_hash is null");
            return (Criteria) this;
        }

        public Criteria andProposalHashIsNotNull() {
            addCriterion("proposal_hash is not null");
            return (Criteria) this;
        }

        public Criteria andProposalHashEqualTo(String value) {
            addCriterion("proposal_hash =", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashNotEqualTo(String value) {
            addCriterion("proposal_hash <>", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashGreaterThan(String value) {
            addCriterion("proposal_hash >", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashGreaterThanOrEqualTo(String value) {
            addCriterion("proposal_hash >=", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashLessThan(String value) {
            addCriterion("proposal_hash <", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashLessThanOrEqualTo(String value) {
            addCriterion("proposal_hash <=", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashLike(String value) {
            addCriterion("proposal_hash like", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashNotLike(String value) {
            addCriterion("proposal_hash not like", value, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashIn(List<String> values) {
            addCriterion("proposal_hash in", values, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashNotIn(List<String> values) {
            addCriterion("proposal_hash not in", values, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashBetween(String value1, String value2) {
            addCriterion("proposal_hash between", value1, value2, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andProposalHashNotBetween(String value1, String value2) {
            addCriterion("proposal_hash not between", value1, value2, "proposalHash");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("`timestamp` is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("`timestamp` is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Date value) {
            addCriterion("`timestamp` =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Date value) {
            addCriterion("`timestamp` <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Date value) {
            addCriterion("`timestamp` >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("`timestamp` >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Date value) {
            addCriterion("`timestamp` <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Date value) {
            addCriterion("`timestamp` <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Date> values) {
            addCriterion("`timestamp` in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Date> values) {
            addCriterion("`timestamp` not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Date value1, Date value2) {
            addCriterion("`timestamp` between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Date value1, Date value2) {
            addCriterion("`timestamp` not between", value1, value2, "timestamp");
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