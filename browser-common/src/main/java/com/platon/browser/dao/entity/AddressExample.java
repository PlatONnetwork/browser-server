package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddressExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AddressExample() {
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

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(BigDecimal value) {
            addCriterion("balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(BigDecimal value) {
            addCriterion("balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(BigDecimal value) {
            addCriterion("balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(BigDecimal value) {
            addCriterion("balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<BigDecimal> values) {
            addCriterion("balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<BigDecimal> values) {
            addCriterion("balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance not between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceIsNull() {
            addCriterion("restricting_balance is null");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceIsNotNull() {
            addCriterion("restricting_balance is not null");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceEqualTo(BigDecimal value) {
            addCriterion("restricting_balance =", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceNotEqualTo(BigDecimal value) {
            addCriterion("restricting_balance <>", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceGreaterThan(BigDecimal value) {
            addCriterion("restricting_balance >", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("restricting_balance >=", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceLessThan(BigDecimal value) {
            addCriterion("restricting_balance <", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("restricting_balance <=", value, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceIn(List<BigDecimal> values) {
            addCriterion("restricting_balance in", values, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceNotIn(List<BigDecimal> values) {
            addCriterion("restricting_balance not in", values, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("restricting_balance between", value1, value2, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andRestrictingBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("restricting_balance not between", value1, value2, "restrictingBalance");
            return (Criteria) this;
        }

        public Criteria andStakingValueIsNull() {
            addCriterion("staking_value is null");
            return (Criteria) this;
        }

        public Criteria andStakingValueIsNotNull() {
            addCriterion("staking_value is not null");
            return (Criteria) this;
        }

        public Criteria andStakingValueEqualTo(BigDecimal value) {
            addCriterion("staking_value =", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotEqualTo(BigDecimal value) {
            addCriterion("staking_value <>", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThan(BigDecimal value) {
            addCriterion("staking_value >", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_value >=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThan(BigDecimal value) {
            addCriterion("staking_value <", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("staking_value <=", value, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueIn(List<BigDecimal> values) {
            addCriterion("staking_value in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotIn(List<BigDecimal> values) {
            addCriterion("staking_value not in", values, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_value between", value1, value2, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andStakingValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("staking_value not between", value1, value2, "stakingValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueIsNull() {
            addCriterion("delegate_value is null");
            return (Criteria) this;
        }

        public Criteria andDelegateValueIsNotNull() {
            addCriterion("delegate_value is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateValueEqualTo(BigDecimal value) {
            addCriterion("delegate_value =", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueNotEqualTo(BigDecimal value) {
            addCriterion("delegate_value <>", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueGreaterThan(BigDecimal value) {
            addCriterion("delegate_value >", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_value >=", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueLessThan(BigDecimal value) {
            addCriterion("delegate_value <", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("delegate_value <=", value, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueIn(List<BigDecimal> values) {
            addCriterion("delegate_value in", values, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueNotIn(List<BigDecimal> values) {
            addCriterion("delegate_value not in", values, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_value between", value1, value2, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andDelegateValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("delegate_value not between", value1, value2, "delegateValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueIsNull() {
            addCriterion("redeemed_value is null");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueIsNotNull() {
            addCriterion("redeemed_value is not null");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueEqualTo(BigDecimal value) {
            addCriterion("redeemed_value =", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueNotEqualTo(BigDecimal value) {
            addCriterion("redeemed_value <>", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueGreaterThan(BigDecimal value) {
            addCriterion("redeemed_value >", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("redeemed_value >=", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueLessThan(BigDecimal value) {
            addCriterion("redeemed_value <", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("redeemed_value <=", value, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueIn(List<BigDecimal> values) {
            addCriterion("redeemed_value in", values, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueNotIn(List<BigDecimal> values) {
            addCriterion("redeemed_value not in", values, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("redeemed_value between", value1, value2, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andRedeemedValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("redeemed_value not between", value1, value2, "redeemedValue");
            return (Criteria) this;
        }

        public Criteria andTxQtyIsNull() {
            addCriterion("tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andTxQtyIsNotNull() {
            addCriterion("tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTxQtyEqualTo(Integer value) {
            addCriterion("tx_qty =", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotEqualTo(Integer value) {
            addCriterion("tx_qty <>", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyGreaterThan(Integer value) {
            addCriterion("tx_qty >", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_qty >=", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyLessThan(Integer value) {
            addCriterion("tx_qty <", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("tx_qty <=", value, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyIn(List<Integer> values) {
            addCriterion("tx_qty in", values, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotIn(List<Integer> values) {
            addCriterion("tx_qty not in", values, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyBetween(Integer value1, Integer value2) {
            addCriterion("tx_qty between", value1, value2, "txQty");
            return (Criteria) this;
        }

        public Criteria andTxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_qty not between", value1, value2, "txQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyIsNull() {
            addCriterion("transfer_qty is null");
            return (Criteria) this;
        }

        public Criteria andTransferQtyIsNotNull() {
            addCriterion("transfer_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTransferQtyEqualTo(Integer value) {
            addCriterion("transfer_qty =", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyNotEqualTo(Integer value) {
            addCriterion("transfer_qty <>", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyGreaterThan(Integer value) {
            addCriterion("transfer_qty >", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("transfer_qty >=", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyLessThan(Integer value) {
            addCriterion("transfer_qty <", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyLessThanOrEqualTo(Integer value) {
            addCriterion("transfer_qty <=", value, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyIn(List<Integer> values) {
            addCriterion("transfer_qty in", values, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyNotIn(List<Integer> values) {
            addCriterion("transfer_qty not in", values, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyBetween(Integer value1, Integer value2) {
            addCriterion("transfer_qty between", value1, value2, "transferQty");
            return (Criteria) this;
        }

        public Criteria andTransferQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("transfer_qty not between", value1, value2, "transferQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyIsNull() {
            addCriterion("delegate_qty is null");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyIsNotNull() {
            addCriterion("delegate_qty is not null");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyEqualTo(Integer value) {
            addCriterion("delegate_qty =", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyNotEqualTo(Integer value) {
            addCriterion("delegate_qty <>", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyGreaterThan(Integer value) {
            addCriterion("delegate_qty >", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("delegate_qty >=", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyLessThan(Integer value) {
            addCriterion("delegate_qty <", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyLessThanOrEqualTo(Integer value) {
            addCriterion("delegate_qty <=", value, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyIn(List<Integer> values) {
            addCriterion("delegate_qty in", values, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyNotIn(List<Integer> values) {
            addCriterion("delegate_qty not in", values, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyBetween(Integer value1, Integer value2) {
            addCriterion("delegate_qty between", value1, value2, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andDelegateQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("delegate_qty not between", value1, value2, "delegateQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyIsNull() {
            addCriterion("staking_qty is null");
            return (Criteria) this;
        }

        public Criteria andStakingQtyIsNotNull() {
            addCriterion("staking_qty is not null");
            return (Criteria) this;
        }

        public Criteria andStakingQtyEqualTo(Integer value) {
            addCriterion("staking_qty =", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyNotEqualTo(Integer value) {
            addCriterion("staking_qty <>", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyGreaterThan(Integer value) {
            addCriterion("staking_qty >", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("staking_qty >=", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyLessThan(Integer value) {
            addCriterion("staking_qty <", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyLessThanOrEqualTo(Integer value) {
            addCriterion("staking_qty <=", value, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyIn(List<Integer> values) {
            addCriterion("staking_qty in", values, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyNotIn(List<Integer> values) {
            addCriterion("staking_qty not in", values, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyBetween(Integer value1, Integer value2) {
            addCriterion("staking_qty between", value1, value2, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andStakingQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("staking_qty not between", value1, value2, "stakingQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIsNull() {
            addCriterion("proposal_qty is null");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIsNotNull() {
            addCriterion("proposal_qty is not null");
            return (Criteria) this;
        }

        public Criteria andProposalQtyEqualTo(Integer value) {
            addCriterion("proposal_qty =", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotEqualTo(Integer value) {
            addCriterion("proposal_qty <>", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyGreaterThan(Integer value) {
            addCriterion("proposal_qty >", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("proposal_qty >=", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyLessThan(Integer value) {
            addCriterion("proposal_qty <", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyLessThanOrEqualTo(Integer value) {
            addCriterion("proposal_qty <=", value, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyIn(List<Integer> values) {
            addCriterion("proposal_qty in", values, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotIn(List<Integer> values) {
            addCriterion("proposal_qty not in", values, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyBetween(Integer value1, Integer value2) {
            addCriterion("proposal_qty between", value1, value2, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andProposalQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("proposal_qty not between", value1, value2, "proposalQty");
            return (Criteria) this;
        }

        public Criteria andCandidateCountIsNull() {
            addCriterion("candidate_count is null");
            return (Criteria) this;
        }

        public Criteria andCandidateCountIsNotNull() {
            addCriterion("candidate_count is not null");
            return (Criteria) this;
        }

        public Criteria andCandidateCountEqualTo(Integer value) {
            addCriterion("candidate_count =", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountNotEqualTo(Integer value) {
            addCriterion("candidate_count <>", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountGreaterThan(Integer value) {
            addCriterion("candidate_count >", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("candidate_count >=", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountLessThan(Integer value) {
            addCriterion("candidate_count <", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountLessThanOrEqualTo(Integer value) {
            addCriterion("candidate_count <=", value, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountIn(List<Integer> values) {
            addCriterion("candidate_count in", values, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountNotIn(List<Integer> values) {
            addCriterion("candidate_count not in", values, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountBetween(Integer value1, Integer value2) {
            addCriterion("candidate_count between", value1, value2, "candidateCount");
            return (Criteria) this;
        }

        public Criteria andCandidateCountNotBetween(Integer value1, Integer value2) {
            addCriterion("candidate_count not between", value1, value2, "candidateCount");
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

        public Criteria andContractNameIsNull() {
            addCriterion("contract_name is null");
            return (Criteria) this;
        }

        public Criteria andContractNameIsNotNull() {
            addCriterion("contract_name is not null");
            return (Criteria) this;
        }

        public Criteria andContractNameEqualTo(String value) {
            addCriterion("contract_name =", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotEqualTo(String value) {
            addCriterion("contract_name <>", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameGreaterThan(String value) {
            addCriterion("contract_name >", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameGreaterThanOrEqualTo(String value) {
            addCriterion("contract_name >=", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLessThan(String value) {
            addCriterion("contract_name <", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLessThanOrEqualTo(String value) {
            addCriterion("contract_name <=", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameLike(String value) {
            addCriterion("contract_name like", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotLike(String value) {
            addCriterion("contract_name not like", value, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameIn(List<String> values) {
            addCriterion("contract_name in", values, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotIn(List<String> values) {
            addCriterion("contract_name not in", values, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameBetween(String value1, String value2) {
            addCriterion("contract_name between", value1, value2, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractNameNotBetween(String value1, String value2) {
            addCriterion("contract_name not between", value1, value2, "contractName");
            return (Criteria) this;
        }

        public Criteria andContractCreateIsNull() {
            addCriterion("contract_create is null");
            return (Criteria) this;
        }

        public Criteria andContractCreateIsNotNull() {
            addCriterion("contract_create is not null");
            return (Criteria) this;
        }

        public Criteria andContractCreateEqualTo(String value) {
            addCriterion("contract_create =", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateNotEqualTo(String value) {
            addCriterion("contract_create <>", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateGreaterThan(String value) {
            addCriterion("contract_create >", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateGreaterThanOrEqualTo(String value) {
            addCriterion("contract_create >=", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateLessThan(String value) {
            addCriterion("contract_create <", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateLessThanOrEqualTo(String value) {
            addCriterion("contract_create <=", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateLike(String value) {
            addCriterion("contract_create like", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateNotLike(String value) {
            addCriterion("contract_create not like", value, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateIn(List<String> values) {
            addCriterion("contract_create in", values, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateNotIn(List<String> values) {
            addCriterion("contract_create not in", values, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateBetween(String value1, String value2) {
            addCriterion("contract_create between", value1, value2, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreateNotBetween(String value1, String value2) {
            addCriterion("contract_create not between", value1, value2, "contractCreate");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashIsNull() {
            addCriterion("contract_createHash is null");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashIsNotNull() {
            addCriterion("contract_createHash is not null");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashEqualTo(String value) {
            addCriterion("contract_createHash =", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashNotEqualTo(String value) {
            addCriterion("contract_createHash <>", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashGreaterThan(String value) {
            addCriterion("contract_createHash >", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashGreaterThanOrEqualTo(String value) {
            addCriterion("contract_createHash >=", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashLessThan(String value) {
            addCriterion("contract_createHash <", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashLessThanOrEqualTo(String value) {
            addCriterion("contract_createHash <=", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashLike(String value) {
            addCriterion("contract_createHash like", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashNotLike(String value) {
            addCriterion("contract_createHash not like", value, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashIn(List<String> values) {
            addCriterion("contract_createHash in", values, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashNotIn(List<String> values) {
            addCriterion("contract_createHash not in", values, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashBetween(String value1, String value2) {
            addCriterion("contract_createHash between", value1, value2, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractCreatehashNotBetween(String value1, String value2) {
            addCriterion("contract_createHash not between", value1, value2, "contractCreatehash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashIsNull() {
            addCriterion("contract_destroy_hash is null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashIsNotNull() {
            addCriterion("contract_destroy_hash is not null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashEqualTo(String value) {
            addCriterion("contract_destroy_hash =", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashNotEqualTo(String value) {
            addCriterion("contract_destroy_hash <>", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashGreaterThan(String value) {
            addCriterion("contract_destroy_hash >", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashGreaterThanOrEqualTo(String value) {
            addCriterion("contract_destroy_hash >=", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashLessThan(String value) {
            addCriterion("contract_destroy_hash <", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashLessThanOrEqualTo(String value) {
            addCriterion("contract_destroy_hash <=", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashLike(String value) {
            addCriterion("contract_destroy_hash like", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashNotLike(String value) {
            addCriterion("contract_destroy_hash not like", value, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashIn(List<String> values) {
            addCriterion("contract_destroy_hash in", values, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashNotIn(List<String> values) {
            addCriterion("contract_destroy_hash not in", values, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashBetween(String value1, String value2) {
            addCriterion("contract_destroy_hash between", value1, value2, "contractDestroyHash");
            return (Criteria) this;
        }

        public Criteria andContractDestroyHashNotBetween(String value1, String value2) {
            addCriterion("contract_destroy_hash not between", value1, value2, "contractDestroyHash");
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

        public Criteria andHaveRewardIsNull() {
            addCriterion("have_reward is null");
            return (Criteria) this;
        }

        public Criteria andHaveRewardIsNotNull() {
            addCriterion("have_reward is not null");
            return (Criteria) this;
        }

        public Criteria andHaveRewardEqualTo(BigDecimal value) {
            addCriterion("have_reward =", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardNotEqualTo(BigDecimal value) {
            addCriterion("have_reward <>", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardGreaterThan(BigDecimal value) {
            addCriterion("have_reward >", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("have_reward >=", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardLessThan(BigDecimal value) {
            addCriterion("have_reward <", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("have_reward <=", value, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardIn(List<BigDecimal> values) {
            addCriterion("have_reward in", values, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardNotIn(List<BigDecimal> values) {
            addCriterion("have_reward not in", values, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("have_reward between", value1, value2, "haveReward");
            return (Criteria) this;
        }

        public Criteria andHaveRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("have_reward not between", value1, value2, "haveReward");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIsNull() {
            addCriterion("erc721_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIsNotNull() {
            addCriterion("erc721_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyEqualTo(Integer value) {
            addCriterion("erc721_tx_qty =", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotEqualTo(Integer value) {
            addCriterion("erc721_tx_qty <>", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyGreaterThan(Integer value) {
            addCriterion("erc721_tx_qty >", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("erc721_tx_qty >=", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyLessThan(Integer value) {
            addCriterion("erc721_tx_qty <", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("erc721_tx_qty <=", value, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyIn(List<Integer> values) {
            addCriterion("erc721_tx_qty in", values, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotIn(List<Integer> values) {
            addCriterion("erc721_tx_qty not in", values, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyBetween(Integer value1, Integer value2) {
            addCriterion("erc721_tx_qty between", value1, value2, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc721TxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("erc721_tx_qty not between", value1, value2, "erc721TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIsNull() {
            addCriterion("erc20_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIsNotNull() {
            addCriterion("erc20_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyEqualTo(Integer value) {
            addCriterion("erc20_tx_qty =", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotEqualTo(Integer value) {
            addCriterion("erc20_tx_qty <>", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyGreaterThan(Integer value) {
            addCriterion("erc20_tx_qty >", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("erc20_tx_qty >=", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyLessThan(Integer value) {
            addCriterion("erc20_tx_qty <", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("erc20_tx_qty <=", value, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyIn(List<Integer> values) {
            addCriterion("erc20_tx_qty in", values, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotIn(List<Integer> values) {
            addCriterion("erc20_tx_qty not in", values, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyBetween(Integer value1, Integer value2) {
            addCriterion("erc20_tx_qty between", value1, value2, "erc20TxQty");
            return (Criteria) this;
        }

        public Criteria andErc20TxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("erc20_tx_qty not between", value1, value2, "erc20TxQty");
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