package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NodeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NodeExample() {
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

        public Criteria andStatSlashMultiQtyIsNull() {
            addCriterion("stat_slash_multi_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyIsNotNull() {
            addCriterion("stat_slash_multi_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyEqualTo(Integer value) {
            addCriterion("stat_slash_multi_qty =", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyNotEqualTo(Integer value) {
            addCriterion("stat_slash_multi_qty <>", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyGreaterThan(Integer value) {
            addCriterion("stat_slash_multi_qty >", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_slash_multi_qty >=", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyLessThan(Integer value) {
            addCriterion("stat_slash_multi_qty <", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_slash_multi_qty <=", value, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyIn(List<Integer> values) {
            addCriterion("stat_slash_multi_qty in", values, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyNotIn(List<Integer> values) {
            addCriterion("stat_slash_multi_qty not in", values, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_slash_multi_qty between", value1, value2, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashMultiQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_slash_multi_qty not between", value1, value2, "statSlashMultiQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyIsNull() {
            addCriterion("stat_slash_low_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyIsNotNull() {
            addCriterion("stat_slash_low_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyEqualTo(Integer value) {
            addCriterion("stat_slash_low_qty =", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyNotEqualTo(Integer value) {
            addCriterion("stat_slash_low_qty <>", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyGreaterThan(Integer value) {
            addCriterion("stat_slash_low_qty >", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_slash_low_qty >=", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyLessThan(Integer value) {
            addCriterion("stat_slash_low_qty <", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyLessThanOrEqualTo(Integer value) {
            addCriterion("stat_slash_low_qty <=", value, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyIn(List<Integer> values) {
            addCriterion("stat_slash_low_qty in", values, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyNotIn(List<Integer> values) {
            addCriterion("stat_slash_low_qty not in", values, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyBetween(Integer value1, Integer value2) {
            addCriterion("stat_slash_low_qty between", value1, value2, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatSlashLowQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_slash_low_qty not between", value1, value2, "statSlashLowQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyIsNull() {
            addCriterion("stat_block_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyIsNotNull() {
            addCriterion("stat_block_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyEqualTo(Long value) {
            addCriterion("stat_block_qty =", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyNotEqualTo(Long value) {
            addCriterion("stat_block_qty <>", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyGreaterThan(Long value) {
            addCriterion("stat_block_qty >", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyGreaterThanOrEqualTo(Long value) {
            addCriterion("stat_block_qty >=", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyLessThan(Long value) {
            addCriterion("stat_block_qty <", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyLessThanOrEqualTo(Long value) {
            addCriterion("stat_block_qty <=", value, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyIn(List<Long> values) {
            addCriterion("stat_block_qty in", values, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyNotIn(List<Long> values) {
            addCriterion("stat_block_qty not in", values, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyBetween(Long value1, Long value2) {
            addCriterion("stat_block_qty between", value1, value2, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatBlockQtyNotBetween(Long value1, Long value2) {
            addCriterion("stat_block_qty not between", value1, value2, "statBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyIsNull() {
            addCriterion("stat_expect_block_qty is null");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyIsNotNull() {
            addCriterion("stat_expect_block_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyEqualTo(String value) {
            addCriterion("stat_expect_block_qty =", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyNotEqualTo(String value) {
            addCriterion("stat_expect_block_qty <>", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyGreaterThan(String value) {
            addCriterion("stat_expect_block_qty >", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyGreaterThanOrEqualTo(String value) {
            addCriterion("stat_expect_block_qty >=", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyLessThan(String value) {
            addCriterion("stat_expect_block_qty <", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyLessThanOrEqualTo(String value) {
            addCriterion("stat_expect_block_qty <=", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyLike(String value) {
            addCriterion("stat_expect_block_qty like", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyNotLike(String value) {
            addCriterion("stat_expect_block_qty not like", value, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyIn(List<String> values) {
            addCriterion("stat_expect_block_qty in", values, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyNotIn(List<String> values) {
            addCriterion("stat_expect_block_qty not in", values, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyBetween(String value1, String value2) {
            addCriterion("stat_expect_block_qty between", value1, value2, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatExpectBlockQtyNotBetween(String value1, String value2) {
            addCriterion("stat_expect_block_qty not between", value1, value2, "statExpectBlockQty");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueIsNull() {
            addCriterion("stat_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueIsNotNull() {
            addCriterion("stat_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueEqualTo(String value) {
            addCriterion("stat_reward_value =", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueNotEqualTo(String value) {
            addCriterion("stat_reward_value <>", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueGreaterThan(String value) {
            addCriterion("stat_reward_value >", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueGreaterThanOrEqualTo(String value) {
            addCriterion("stat_reward_value >=", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueLessThan(String value) {
            addCriterion("stat_reward_value <", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueLessThanOrEqualTo(String value) {
            addCriterion("stat_reward_value <=", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueLike(String value) {
            addCriterion("stat_reward_value like", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueNotLike(String value) {
            addCriterion("stat_reward_value not like", value, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueIn(List<String> values) {
            addCriterion("stat_reward_value in", values, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueNotIn(List<String> values) {
            addCriterion("stat_reward_value not in", values, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueBetween(String value1, String value2) {
            addCriterion("stat_reward_value between", value1, value2, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatRewardValueNotBetween(String value1, String value2) {
            addCriterion("stat_reward_value not between", value1, value2, "statRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeIsNull() {
            addCriterion("stat_verifier_time is null");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeIsNotNull() {
            addCriterion("stat_verifier_time is not null");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeEqualTo(Integer value) {
            addCriterion("stat_verifier_time =", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotEqualTo(Integer value) {
            addCriterion("stat_verifier_time <>", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeGreaterThan(Integer value) {
            addCriterion("stat_verifier_time >", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("stat_verifier_time >=", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeLessThan(Integer value) {
            addCriterion("stat_verifier_time <", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeLessThanOrEqualTo(Integer value) {
            addCriterion("stat_verifier_time <=", value, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeIn(List<Integer> values) {
            addCriterion("stat_verifier_time in", values, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotIn(List<Integer> values) {
            addCriterion("stat_verifier_time not in", values, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeBetween(Integer value1, Integer value2) {
            addCriterion("stat_verifier_time between", value1, value2, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andStatVerifierTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("stat_verifier_time not between", value1, value2, "statVerifierTime");
            return (Criteria) this;
        }

        public Criteria andIsRecommendIsNull() {
            addCriterion("is_recommend is null");
            return (Criteria) this;
        }

        public Criteria andIsRecommendIsNotNull() {
            addCriterion("is_recommend is not null");
            return (Criteria) this;
        }

        public Criteria andIsRecommendEqualTo(Integer value) {
            addCriterion("is_recommend =", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendNotEqualTo(Integer value) {
            addCriterion("is_recommend <>", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendGreaterThan(Integer value) {
            addCriterion("is_recommend >", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_recommend >=", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendLessThan(Integer value) {
            addCriterion("is_recommend <", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendLessThanOrEqualTo(Integer value) {
            addCriterion("is_recommend <=", value, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendIn(List<Integer> values) {
            addCriterion("is_recommend in", values, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendNotIn(List<Integer> values) {
            addCriterion("is_recommend not in", values, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendBetween(Integer value1, Integer value2) {
            addCriterion("is_recommend between", value1, value2, "isRecommend");
            return (Criteria) this;
        }

        public Criteria andIsRecommendNotBetween(Integer value1, Integer value2) {
            addCriterion("is_recommend not between", value1, value2, "isRecommend");
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

        public Criteria andStatFeeRewardValueIsNull() {
            addCriterion("stat_fee_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueIsNotNull() {
            addCriterion("stat_fee_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueEqualTo(String value) {
            addCriterion("stat_fee_reward_value =", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueNotEqualTo(String value) {
            addCriterion("stat_fee_reward_value <>", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueGreaterThan(String value) {
            addCriterion("stat_fee_reward_value >", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueGreaterThanOrEqualTo(String value) {
            addCriterion("stat_fee_reward_value >=", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueLessThan(String value) {
            addCriterion("stat_fee_reward_value <", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueLessThanOrEqualTo(String value) {
            addCriterion("stat_fee_reward_value <=", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueLike(String value) {
            addCriterion("stat_fee_reward_value like", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueNotLike(String value) {
            addCriterion("stat_fee_reward_value not like", value, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueIn(List<String> values) {
            addCriterion("stat_fee_reward_value in", values, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueNotIn(List<String> values) {
            addCriterion("stat_fee_reward_value not in", values, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueBetween(String value1, String value2) {
            addCriterion("stat_fee_reward_value between", value1, value2, "statFeeRewardValue");
            return (Criteria) this;
        }

        public Criteria andStatFeeRewardValueNotBetween(String value1, String value2) {
            addCriterion("stat_fee_reward_value not between", value1, value2, "statFeeRewardValue");
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