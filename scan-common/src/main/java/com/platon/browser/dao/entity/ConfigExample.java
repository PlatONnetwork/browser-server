package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConfigExample() {
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

        public Criteria andModuleIsNull() {
            addCriterion("`module` is null");
            return (Criteria) this;
        }

        public Criteria andModuleIsNotNull() {
            addCriterion("`module` is not null");
            return (Criteria) this;
        }

        public Criteria andModuleEqualTo(String value) {
            addCriterion("`module` =", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleNotEqualTo(String value) {
            addCriterion("`module` <>", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleGreaterThan(String value) {
            addCriterion("`module` >", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleGreaterThanOrEqualTo(String value) {
            addCriterion("`module` >=", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleLessThan(String value) {
            addCriterion("`module` <", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleLessThanOrEqualTo(String value) {
            addCriterion("`module` <=", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleLike(String value) {
            addCriterion("`module` like", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleNotLike(String value) {
            addCriterion("`module` not like", value, "module");
            return (Criteria) this;
        }

        public Criteria andModuleIn(List<String> values) {
            addCriterion("`module` in", values, "module");
            return (Criteria) this;
        }

        public Criteria andModuleNotIn(List<String> values) {
            addCriterion("`module` not in", values, "module");
            return (Criteria) this;
        }

        public Criteria andModuleBetween(String value1, String value2) {
            addCriterion("`module` between", value1, value2, "module");
            return (Criteria) this;
        }

        public Criteria andModuleNotBetween(String value1, String value2) {
            addCriterion("`module` not between", value1, value2, "module");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andInitValueIsNull() {
            addCriterion("init_value is null");
            return (Criteria) this;
        }

        public Criteria andInitValueIsNotNull() {
            addCriterion("init_value is not null");
            return (Criteria) this;
        }

        public Criteria andInitValueEqualTo(String value) {
            addCriterion("init_value =", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueNotEqualTo(String value) {
            addCriterion("init_value <>", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueGreaterThan(String value) {
            addCriterion("init_value >", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueGreaterThanOrEqualTo(String value) {
            addCriterion("init_value >=", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueLessThan(String value) {
            addCriterion("init_value <", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueLessThanOrEqualTo(String value) {
            addCriterion("init_value <=", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueLike(String value) {
            addCriterion("init_value like", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueNotLike(String value) {
            addCriterion("init_value not like", value, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueIn(List<String> values) {
            addCriterion("init_value in", values, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueNotIn(List<String> values) {
            addCriterion("init_value not in", values, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueBetween(String value1, String value2) {
            addCriterion("init_value between", value1, value2, "initValue");
            return (Criteria) this;
        }

        public Criteria andInitValueNotBetween(String value1, String value2) {
            addCriterion("init_value not between", value1, value2, "initValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueIsNull() {
            addCriterion("stale_value is null");
            return (Criteria) this;
        }

        public Criteria andStaleValueIsNotNull() {
            addCriterion("stale_value is not null");
            return (Criteria) this;
        }

        public Criteria andStaleValueEqualTo(String value) {
            addCriterion("stale_value =", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueNotEqualTo(String value) {
            addCriterion("stale_value <>", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueGreaterThan(String value) {
            addCriterion("stale_value >", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueGreaterThanOrEqualTo(String value) {
            addCriterion("stale_value >=", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueLessThan(String value) {
            addCriterion("stale_value <", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueLessThanOrEqualTo(String value) {
            addCriterion("stale_value <=", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueLike(String value) {
            addCriterion("stale_value like", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueNotLike(String value) {
            addCriterion("stale_value not like", value, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueIn(List<String> values) {
            addCriterion("stale_value in", values, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueNotIn(List<String> values) {
            addCriterion("stale_value not in", values, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueBetween(String value1, String value2) {
            addCriterion("stale_value between", value1, value2, "staleValue");
            return (Criteria) this;
        }

        public Criteria andStaleValueNotBetween(String value1, String value2) {
            addCriterion("stale_value not between", value1, value2, "staleValue");
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

        public Criteria andRangeDescIsNull() {
            addCriterion("range_desc is null");
            return (Criteria) this;
        }

        public Criteria andRangeDescIsNotNull() {
            addCriterion("range_desc is not null");
            return (Criteria) this;
        }

        public Criteria andRangeDescEqualTo(String value) {
            addCriterion("range_desc =", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescNotEqualTo(String value) {
            addCriterion("range_desc <>", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescGreaterThan(String value) {
            addCriterion("range_desc >", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescGreaterThanOrEqualTo(String value) {
            addCriterion("range_desc >=", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescLessThan(String value) {
            addCriterion("range_desc <", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescLessThanOrEqualTo(String value) {
            addCriterion("range_desc <=", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescLike(String value) {
            addCriterion("range_desc like", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescNotLike(String value) {
            addCriterion("range_desc not like", value, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescIn(List<String> values) {
            addCriterion("range_desc in", values, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescNotIn(List<String> values) {
            addCriterion("range_desc not in", values, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescBetween(String value1, String value2) {
            addCriterion("range_desc between", value1, value2, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andRangeDescNotBetween(String value1, String value2) {
            addCriterion("range_desc not between", value1, value2, "rangeDesc");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIsNull() {
            addCriterion("active_block is null");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIsNotNull() {
            addCriterion("active_block is not null");
            return (Criteria) this;
        }

        public Criteria andActiveBlockEqualTo(Long value) {
            addCriterion("active_block =", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotEqualTo(Long value) {
            addCriterion("active_block <>", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockGreaterThan(Long value) {
            addCriterion("active_block >", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockGreaterThanOrEqualTo(Long value) {
            addCriterion("active_block >=", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockLessThan(Long value) {
            addCriterion("active_block <", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockLessThanOrEqualTo(Long value) {
            addCriterion("active_block <=", value, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockIn(List<Long> values) {
            addCriterion("active_block in", values, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotIn(List<Long> values) {
            addCriterion("active_block not in", values, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockBetween(Long value1, Long value2) {
            addCriterion("active_block between", value1, value2, "activeBlock");
            return (Criteria) this;
        }

        public Criteria andActiveBlockNotBetween(Long value1, Long value2) {
            addCriterion("active_block not between", value1, value2, "activeBlock");
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