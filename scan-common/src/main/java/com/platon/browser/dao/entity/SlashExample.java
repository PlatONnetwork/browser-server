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

        public Criteria andTxHashIsNull() {
            addCriterion("tx_hash is null");
            return (Criteria) this;
        }

        public Criteria andTxHashIsNotNull() {
            addCriterion("tx_hash is not null");
            return (Criteria) this;
        }

        public Criteria andTxHashEqualTo(String value) {
            addCriterion("tx_hash =", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotEqualTo(String value) {
            addCriterion("tx_hash <>", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThan(String value) {
            addCriterion("tx_hash >", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThanOrEqualTo(String value) {
            addCriterion("tx_hash >=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThan(String value) {
            addCriterion("tx_hash <", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThanOrEqualTo(String value) {
            addCriterion("tx_hash <=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLike(String value) {
            addCriterion("tx_hash like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotLike(String value) {
            addCriterion("tx_hash not like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashIn(List<String> values) {
            addCriterion("tx_hash in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotIn(List<String> values) {
            addCriterion("tx_hash not in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashBetween(String value1, String value2) {
            addCriterion("tx_hash between", value1, value2, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotBetween(String value1, String value2) {
            addCriterion("tx_hash not between", value1, value2, "txHash");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("`time` is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("`time` is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("`time` =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("`time` <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("`time` >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`time` >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("`time` <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("`time` <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("`time` in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("`time` not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("`time` between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("`time` not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andSettingEpochIsNull() {
            addCriterion("setting_epoch is null");
            return (Criteria) this;
        }

        public Criteria andSettingEpochIsNotNull() {
            addCriterion("setting_epoch is not null");
            return (Criteria) this;
        }

        public Criteria andSettingEpochEqualTo(Integer value) {
            addCriterion("setting_epoch =", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochNotEqualTo(Integer value) {
            addCriterion("setting_epoch <>", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochGreaterThan(Integer value) {
            addCriterion("setting_epoch >", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochGreaterThanOrEqualTo(Integer value) {
            addCriterion("setting_epoch >=", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochLessThan(Integer value) {
            addCriterion("setting_epoch <", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochLessThanOrEqualTo(Integer value) {
            addCriterion("setting_epoch <=", value, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochIn(List<Integer> values) {
            addCriterion("setting_epoch in", values, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochNotIn(List<Integer> values) {
            addCriterion("setting_epoch not in", values, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochBetween(Integer value1, Integer value2) {
            addCriterion("setting_epoch between", value1, value2, "settingEpoch");
            return (Criteria) this;
        }

        public Criteria andSettingEpochNotBetween(Integer value1, Integer value2) {
            addCriterion("setting_epoch not between", value1, value2, "settingEpoch");
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

        public Criteria andSlashRateIsNull() {
            addCriterion("slash_rate is null");
            return (Criteria) this;
        }

        public Criteria andSlashRateIsNotNull() {
            addCriterion("slash_rate is not null");
            return (Criteria) this;
        }

        public Criteria andSlashRateEqualTo(BigDecimal value) {
            addCriterion("slash_rate =", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotEqualTo(BigDecimal value) {
            addCriterion("slash_rate <>", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateGreaterThan(BigDecimal value) {
            addCriterion("slash_rate >", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_rate >=", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateLessThan(BigDecimal value) {
            addCriterion("slash_rate <", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_rate <=", value, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateIn(List<BigDecimal> values) {
            addCriterion("slash_rate in", values, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotIn(List<BigDecimal> values) {
            addCriterion("slash_rate not in", values, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_rate between", value1, value2, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_rate not between", value1, value2, "slashRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateIsNull() {
            addCriterion("slash_report_rate is null");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateIsNotNull() {
            addCriterion("slash_report_rate is not null");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateEqualTo(BigDecimal value) {
            addCriterion("slash_report_rate =", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateNotEqualTo(BigDecimal value) {
            addCriterion("slash_report_rate <>", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateGreaterThan(BigDecimal value) {
            addCriterion("slash_report_rate >", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_report_rate >=", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateLessThan(BigDecimal value) {
            addCriterion("slash_report_rate <", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("slash_report_rate <=", value, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateIn(List<BigDecimal> values) {
            addCriterion("slash_report_rate in", values, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateNotIn(List<BigDecimal> values) {
            addCriterion("slash_report_rate not in", values, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_report_rate between", value1, value2, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andSlashReportRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("slash_report_rate not between", value1, value2, "slashReportRate");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressIsNull() {
            addCriterion("benefit_address is null");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressIsNotNull() {
            addCriterion("benefit_address is not null");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressEqualTo(String value) {
            addCriterion("benefit_address =", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressNotEqualTo(String value) {
            addCriterion("benefit_address <>", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressGreaterThan(String value) {
            addCriterion("benefit_address >", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressGreaterThanOrEqualTo(String value) {
            addCriterion("benefit_address >=", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressLessThan(String value) {
            addCriterion("benefit_address <", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressLessThanOrEqualTo(String value) {
            addCriterion("benefit_address <=", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressLike(String value) {
            addCriterion("benefit_address like", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressNotLike(String value) {
            addCriterion("benefit_address not like", value, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressIn(List<String> values) {
            addCriterion("benefit_address in", values, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressNotIn(List<String> values) {
            addCriterion("benefit_address not in", values, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressBetween(String value1, String value2) {
            addCriterion("benefit_address between", value1, value2, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andBenefitAddressNotBetween(String value1, String value2) {
            addCriterion("benefit_address not between", value1, value2, "benefitAddress");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountIsNull() {
            addCriterion("code_remain_redeem_amount is null");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountIsNotNull() {
            addCriterion("code_remain_redeem_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountEqualTo(BigDecimal value) {
            addCriterion("code_remain_redeem_amount =", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountNotEqualTo(BigDecimal value) {
            addCriterion("code_remain_redeem_amount <>", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountGreaterThan(BigDecimal value) {
            addCriterion("code_remain_redeem_amount >", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("code_remain_redeem_amount >=", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountLessThan(BigDecimal value) {
            addCriterion("code_remain_redeem_amount <", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("code_remain_redeem_amount <=", value, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountIn(List<BigDecimal> values) {
            addCriterion("code_remain_redeem_amount in", values, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountNotIn(List<BigDecimal> values) {
            addCriterion("code_remain_redeem_amount not in", values, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_remain_redeem_amount between", value1, value2, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRemainRedeemAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_remain_redeem_amount not between", value1, value2, "codeRemainRedeemAmount");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueIsNull() {
            addCriterion("code_reward_value is null");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueIsNotNull() {
            addCriterion("code_reward_value is not null");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueEqualTo(BigDecimal value) {
            addCriterion("code_reward_value =", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueNotEqualTo(BigDecimal value) {
            addCriterion("code_reward_value <>", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueGreaterThan(BigDecimal value) {
            addCriterion("code_reward_value >", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("code_reward_value >=", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueLessThan(BigDecimal value) {
            addCriterion("code_reward_value <", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("code_reward_value <=", value, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueIn(List<BigDecimal> values) {
            addCriterion("code_reward_value in", values, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueNotIn(List<BigDecimal> values) {
            addCriterion("code_reward_value not in", values, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_reward_value between", value1, value2, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeRewardValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_reward_value not between", value1, value2, "codeRewardValue");
            return (Criteria) this;
        }

        public Criteria andCodeStatusIsNull() {
            addCriterion("code_status is null");
            return (Criteria) this;
        }

        public Criteria andCodeStatusIsNotNull() {
            addCriterion("code_status is not null");
            return (Criteria) this;
        }

        public Criteria andCodeStatusEqualTo(Integer value) {
            addCriterion("code_status =", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusNotEqualTo(Integer value) {
            addCriterion("code_status <>", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusGreaterThan(Integer value) {
            addCriterion("code_status >", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_status >=", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusLessThan(Integer value) {
            addCriterion("code_status <", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("code_status <=", value, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusIn(List<Integer> values) {
            addCriterion("code_status in", values, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusNotIn(List<Integer> values) {
            addCriterion("code_status not in", values, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusBetween(Integer value1, Integer value2) {
            addCriterion("code_status between", value1, value2, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("code_status not between", value1, value2, "codeStatus");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochIsNull() {
            addCriterion("code_staking_reduction_epoch is null");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochIsNotNull() {
            addCriterion("code_staking_reduction_epoch is not null");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochEqualTo(Integer value) {
            addCriterion("code_staking_reduction_epoch =", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochNotEqualTo(Integer value) {
            addCriterion("code_staking_reduction_epoch <>", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochGreaterThan(Integer value) {
            addCriterion("code_staking_reduction_epoch >", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochGreaterThanOrEqualTo(Integer value) {
            addCriterion("code_staking_reduction_epoch >=", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochLessThan(Integer value) {
            addCriterion("code_staking_reduction_epoch <", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochLessThanOrEqualTo(Integer value) {
            addCriterion("code_staking_reduction_epoch <=", value, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochIn(List<Integer> values) {
            addCriterion("code_staking_reduction_epoch in", values, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochNotIn(List<Integer> values) {
            addCriterion("code_staking_reduction_epoch not in", values, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochBetween(Integer value1, Integer value2) {
            addCriterion("code_staking_reduction_epoch between", value1, value2, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeStakingReductionEpochNotBetween(Integer value1, Integer value2) {
            addCriterion("code_staking_reduction_epoch not between", value1, value2, "codeStakingReductionEpoch");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueIsNull() {
            addCriterion("code_slash_value is null");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueIsNotNull() {
            addCriterion("code_slash_value is not null");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueEqualTo(BigDecimal value) {
            addCriterion("code_slash_value =", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueNotEqualTo(BigDecimal value) {
            addCriterion("code_slash_value <>", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueGreaterThan(BigDecimal value) {
            addCriterion("code_slash_value >", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("code_slash_value >=", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueLessThan(BigDecimal value) {
            addCriterion("code_slash_value <", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("code_slash_value <=", value, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueIn(List<BigDecimal> values) {
            addCriterion("code_slash_value in", values, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueNotIn(List<BigDecimal> values) {
            addCriterion("code_slash_value not in", values, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_slash_value between", value1, value2, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andCodeSlashValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("code_slash_value not between", value1, value2, "codeSlashValue");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIsNull() {
            addCriterion("un_stake_freeze_duration is null");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIsNotNull() {
            addCriterion("un_stake_freeze_duration is not null");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration =", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration <>", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationGreaterThan(Integer value) {
            addCriterion("un_stake_freeze_duration >", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationGreaterThanOrEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration >=", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationLessThan(Integer value) {
            addCriterion("un_stake_freeze_duration <", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationLessThanOrEqualTo(Integer value) {
            addCriterion("un_stake_freeze_duration <=", value, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationIn(List<Integer> values) {
            addCriterion("un_stake_freeze_duration in", values, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotIn(List<Integer> values) {
            addCriterion("un_stake_freeze_duration not in", values, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationBetween(Integer value1, Integer value2) {
            addCriterion("un_stake_freeze_duration between", value1, value2, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeFreezeDurationNotBetween(Integer value1, Integer value2) {
            addCriterion("un_stake_freeze_duration not between", value1, value2, "unStakeFreezeDuration");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIsNull() {
            addCriterion("un_stake_end_block is null");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIsNotNull() {
            addCriterion("un_stake_end_block is not null");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockEqualTo(Long value) {
            addCriterion("un_stake_end_block =", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotEqualTo(Long value) {
            addCriterion("un_stake_end_block <>", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockGreaterThan(Long value) {
            addCriterion("un_stake_end_block >", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockGreaterThanOrEqualTo(Long value) {
            addCriterion("un_stake_end_block >=", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockLessThan(Long value) {
            addCriterion("un_stake_end_block <", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockLessThanOrEqualTo(Long value) {
            addCriterion("un_stake_end_block <=", value, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockIn(List<Long> values) {
            addCriterion("un_stake_end_block in", values, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotIn(List<Long> values) {
            addCriterion("un_stake_end_block not in", values, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockBetween(Long value1, Long value2) {
            addCriterion("un_stake_end_block between", value1, value2, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andUnStakeEndBlockNotBetween(Long value1, Long value2) {
            addCriterion("un_stake_end_block not between", value1, value2, "unStakeEndBlock");
            return (Criteria) this;
        }

        public Criteria andBlockNumIsNull() {
            addCriterion("block_num is null");
            return (Criteria) this;
        }

        public Criteria andBlockNumIsNotNull() {
            addCriterion("block_num is not null");
            return (Criteria) this;
        }

        public Criteria andBlockNumEqualTo(Long value) {
            addCriterion("block_num =", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumNotEqualTo(Long value) {
            addCriterion("block_num <>", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumGreaterThan(Long value) {
            addCriterion("block_num >", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumGreaterThanOrEqualTo(Long value) {
            addCriterion("block_num >=", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumLessThan(Long value) {
            addCriterion("block_num <", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumLessThanOrEqualTo(Long value) {
            addCriterion("block_num <=", value, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumIn(List<Long> values) {
            addCriterion("block_num in", values, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumNotIn(List<Long> values) {
            addCriterion("block_num not in", values, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumBetween(Long value1, Long value2) {
            addCriterion("block_num between", value1, value2, "blockNum");
            return (Criteria) this;
        }

        public Criteria andBlockNumNotBetween(Long value1, Long value2) {
            addCriterion("block_num not between", value1, value2, "blockNum");
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

        public Criteria andIsHandleIsNull() {
            addCriterion("is_handle is null");
            return (Criteria) this;
        }

        public Criteria andIsHandleIsNotNull() {
            addCriterion("is_handle is not null");
            return (Criteria) this;
        }

        public Criteria andIsHandleEqualTo(Boolean value) {
            addCriterion("is_handle =", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleNotEqualTo(Boolean value) {
            addCriterion("is_handle <>", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleGreaterThan(Boolean value) {
            addCriterion("is_handle >", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_handle >=", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleLessThan(Boolean value) {
            addCriterion("is_handle <", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleLessThanOrEqualTo(Boolean value) {
            addCriterion("is_handle <=", value, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleIn(List<Boolean> values) {
            addCriterion("is_handle in", values, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleNotIn(List<Boolean> values) {
            addCriterion("is_handle not in", values, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleBetween(Boolean value1, Boolean value2) {
            addCriterion("is_handle between", value1, value2, "isHandle");
            return (Criteria) this;
        }

        public Criteria andIsHandleNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_handle not between", value1, value2, "isHandle");
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