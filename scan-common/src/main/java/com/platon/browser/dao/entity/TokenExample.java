package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TokenExample() {
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

        public Criteria andTypeEqualTo(String value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("`type` like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("`type` not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("`type` not between", value1, value2, "type");
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

        public Criteria andSymbolIsNull() {
            addCriterion("symbol is null");
            return (Criteria) this;
        }

        public Criteria andSymbolIsNotNull() {
            addCriterion("symbol is not null");
            return (Criteria) this;
        }

        public Criteria andSymbolEqualTo(String value) {
            addCriterion("symbol =", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotEqualTo(String value) {
            addCriterion("symbol <>", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolGreaterThan(String value) {
            addCriterion("symbol >", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolGreaterThanOrEqualTo(String value) {
            addCriterion("symbol >=", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLessThan(String value) {
            addCriterion("symbol <", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLessThanOrEqualTo(String value) {
            addCriterion("symbol <=", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolLike(String value) {
            addCriterion("symbol like", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotLike(String value) {
            addCriterion("symbol not like", value, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolIn(List<String> values) {
            addCriterion("symbol in", values, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotIn(List<String> values) {
            addCriterion("symbol not in", values, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolBetween(String value1, String value2) {
            addCriterion("symbol between", value1, value2, "symbol");
            return (Criteria) this;
        }

        public Criteria andSymbolNotBetween(String value1, String value2) {
            addCriterion("symbol not between", value1, value2, "symbol");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyIsNull() {
            addCriterion("total_supply is null");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyIsNotNull() {
            addCriterion("total_supply is not null");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyEqualTo(String value) {
            addCriterion("total_supply =", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyNotEqualTo(String value) {
            addCriterion("total_supply <>", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyGreaterThan(String value) {
            addCriterion("total_supply >", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyGreaterThanOrEqualTo(String value) {
            addCriterion("total_supply >=", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyLessThan(String value) {
            addCriterion("total_supply <", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyLessThanOrEqualTo(String value) {
            addCriterion("total_supply <=", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyLike(String value) {
            addCriterion("total_supply like", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyNotLike(String value) {
            addCriterion("total_supply not like", value, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyIn(List<String> values) {
            addCriterion("total_supply in", values, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyNotIn(List<String> values) {
            addCriterion("total_supply not in", values, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyBetween(String value1, String value2) {
            addCriterion("total_supply between", value1, value2, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andTotalSupplyNotBetween(String value1, String value2) {
            addCriterion("total_supply not between", value1, value2, "totalSupply");
            return (Criteria) this;
        }

        public Criteria andDecimalIsNull() {
            addCriterion("`decimal` is null");
            return (Criteria) this;
        }

        public Criteria andDecimalIsNotNull() {
            addCriterion("`decimal` is not null");
            return (Criteria) this;
        }

        public Criteria andDecimalEqualTo(Integer value) {
            addCriterion("`decimal` =", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotEqualTo(Integer value) {
            addCriterion("`decimal` <>", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalGreaterThan(Integer value) {
            addCriterion("`decimal` >", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalGreaterThanOrEqualTo(Integer value) {
            addCriterion("`decimal` >=", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalLessThan(Integer value) {
            addCriterion("`decimal` <", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalLessThanOrEqualTo(Integer value) {
            addCriterion("`decimal` <=", value, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalIn(List<Integer> values) {
            addCriterion("`decimal` in", values, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotIn(List<Integer> values) {
            addCriterion("`decimal` not in", values, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalBetween(Integer value1, Integer value2) {
            addCriterion("`decimal` between", value1, value2, "decimal");
            return (Criteria) this;
        }

        public Criteria andDecimalNotBetween(Integer value1, Integer value2) {
            addCriterion("`decimal` not between", value1, value2, "decimal");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165IsNull() {
            addCriterion("is_support_erc165 is null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165IsNotNull() {
            addCriterion("is_support_erc165 is not null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165EqualTo(Boolean value) {
            addCriterion("is_support_erc165 =", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165NotEqualTo(Boolean value) {
            addCriterion("is_support_erc165 <>", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165GreaterThan(Boolean value) {
            addCriterion("is_support_erc165 >", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165GreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc165 >=", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165LessThan(Boolean value) {
            addCriterion("is_support_erc165 <", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165LessThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc165 <=", value, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165In(List<Boolean> values) {
            addCriterion("is_support_erc165 in", values, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165NotIn(List<Boolean> values) {
            addCriterion("is_support_erc165 not in", values, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165Between(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc165 between", value1, value2, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc165NotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc165 not between", value1, value2, "isSupportErc165");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20IsNull() {
            addCriterion("is_support_erc20 is null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20IsNotNull() {
            addCriterion("is_support_erc20 is not null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20EqualTo(Boolean value) {
            addCriterion("is_support_erc20 =", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20NotEqualTo(Boolean value) {
            addCriterion("is_support_erc20 <>", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20GreaterThan(Boolean value) {
            addCriterion("is_support_erc20 >", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20GreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc20 >=", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20LessThan(Boolean value) {
            addCriterion("is_support_erc20 <", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20LessThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc20 <=", value, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20In(List<Boolean> values) {
            addCriterion("is_support_erc20 in", values, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20NotIn(List<Boolean> values) {
            addCriterion("is_support_erc20 not in", values, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20Between(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc20 between", value1, value2, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc20NotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc20 not between", value1, value2, "isSupportErc20");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721IsNull() {
            addCriterion("is_support_erc721 is null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721IsNotNull() {
            addCriterion("is_support_erc721 is not null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EqualTo(Boolean value) {
            addCriterion("is_support_erc721 =", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721NotEqualTo(Boolean value) {
            addCriterion("is_support_erc721 <>", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721GreaterThan(Boolean value) {
            addCriterion("is_support_erc721 >", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721GreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721 >=", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721LessThan(Boolean value) {
            addCriterion("is_support_erc721 <", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721LessThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721 <=", value, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721In(List<Boolean> values) {
            addCriterion("is_support_erc721 in", values, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721NotIn(List<Boolean> values) {
            addCriterion("is_support_erc721 not in", values, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721Between(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721 between", value1, value2, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721NotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721 not between", value1, value2, "isSupportErc721");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationIsNull() {
            addCriterion("is_support_erc721_enumeration is null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationIsNotNull() {
            addCriterion("is_support_erc721_enumeration is not null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationEqualTo(Boolean value) {
            addCriterion("is_support_erc721_enumeration =", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationNotEqualTo(Boolean value) {
            addCriterion("is_support_erc721_enumeration <>", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationGreaterThan(Boolean value) {
            addCriterion("is_support_erc721_enumeration >", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721_enumeration >=", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationLessThan(Boolean value) {
            addCriterion("is_support_erc721_enumeration <", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationLessThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721_enumeration <=", value, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationIn(List<Boolean> values) {
            addCriterion("is_support_erc721_enumeration in", values, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationNotIn(List<Boolean> values) {
            addCriterion("is_support_erc721_enumeration not in", values, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721_enumeration between", value1, value2, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721EnumerationNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721_enumeration not between", value1, value2, "isSupportErc721Enumeration");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataIsNull() {
            addCriterion("is_support_erc721_metadata is null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataIsNotNull() {
            addCriterion("is_support_erc721_metadata is not null");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataEqualTo(Boolean value) {
            addCriterion("is_support_erc721_metadata =", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataNotEqualTo(Boolean value) {
            addCriterion("is_support_erc721_metadata <>", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataGreaterThan(Boolean value) {
            addCriterion("is_support_erc721_metadata >", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721_metadata >=", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataLessThan(Boolean value) {
            addCriterion("is_support_erc721_metadata <", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataLessThanOrEqualTo(Boolean value) {
            addCriterion("is_support_erc721_metadata <=", value, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataIn(List<Boolean> values) {
            addCriterion("is_support_erc721_metadata in", values, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataNotIn(List<Boolean> values) {
            addCriterion("is_support_erc721_metadata not in", values, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721_metadata between", value1, value2, "isSupportErc721Metadata");
            return (Criteria) this;
        }

        public Criteria andIsSupportErc721MetadataNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_support_erc721_metadata not between", value1, value2, "isSupportErc721Metadata");
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

        public Criteria andTokenTxQtyIsNull() {
            addCriterion("token_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyIsNotNull() {
            addCriterion("token_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyEqualTo(Integer value) {
            addCriterion("token_tx_qty =", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyNotEqualTo(Integer value) {
            addCriterion("token_tx_qty <>", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyGreaterThan(Integer value) {
            addCriterion("token_tx_qty >", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("token_tx_qty >=", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyLessThan(Integer value) {
            addCriterion("token_tx_qty <", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("token_tx_qty <=", value, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyIn(List<Integer> values) {
            addCriterion("token_tx_qty in", values, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyNotIn(List<Integer> values) {
            addCriterion("token_tx_qty not in", values, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyBetween(Integer value1, Integer value2) {
            addCriterion("token_tx_qty between", value1, value2, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenTxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("token_tx_qty not between", value1, value2, "tokenTxQty");
            return (Criteria) this;
        }

        public Criteria andHolderIsNull() {
            addCriterion("holder is null");
            return (Criteria) this;
        }

        public Criteria andHolderIsNotNull() {
            addCriterion("holder is not null");
            return (Criteria) this;
        }

        public Criteria andHolderEqualTo(Integer value) {
            addCriterion("holder =", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotEqualTo(Integer value) {
            addCriterion("holder <>", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderGreaterThan(Integer value) {
            addCriterion("holder >", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderGreaterThanOrEqualTo(Integer value) {
            addCriterion("holder >=", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderLessThan(Integer value) {
            addCriterion("holder <", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderLessThanOrEqualTo(Integer value) {
            addCriterion("holder <=", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderIn(List<Integer> values) {
            addCriterion("holder in", values, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotIn(List<Integer> values) {
            addCriterion("holder not in", values, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderBetween(Integer value1, Integer value2) {
            addCriterion("holder between", value1, value2, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotBetween(Integer value1, Integer value2) {
            addCriterion("holder not between", value1, value2, "holder");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockIsNull() {
            addCriterion("contract_destroy_block is null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockIsNotNull() {
            addCriterion("contract_destroy_block is not null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockEqualTo(Long value) {
            addCriterion("contract_destroy_block =", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockNotEqualTo(Long value) {
            addCriterion("contract_destroy_block <>", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockGreaterThan(Long value) {
            addCriterion("contract_destroy_block >", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockGreaterThanOrEqualTo(Long value) {
            addCriterion("contract_destroy_block >=", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockLessThan(Long value) {
            addCriterion("contract_destroy_block <", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockLessThanOrEqualTo(Long value) {
            addCriterion("contract_destroy_block <=", value, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockIn(List<Long> values) {
            addCriterion("contract_destroy_block in", values, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockNotIn(List<Long> values) {
            addCriterion("contract_destroy_block not in", values, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockBetween(Long value1, Long value2) {
            addCriterion("contract_destroy_block between", value1, value2, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyBlockNotBetween(Long value1, Long value2) {
            addCriterion("contract_destroy_block not between", value1, value2, "contractDestroyBlock");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateIsNull() {
            addCriterion("contract_destroy_update is null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateIsNotNull() {
            addCriterion("contract_destroy_update is not null");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateEqualTo(Boolean value) {
            addCriterion("contract_destroy_update =", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateNotEqualTo(Boolean value) {
            addCriterion("contract_destroy_update <>", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateGreaterThan(Boolean value) {
            addCriterion("contract_destroy_update >", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateGreaterThanOrEqualTo(Boolean value) {
            addCriterion("contract_destroy_update >=", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateLessThan(Boolean value) {
            addCriterion("contract_destroy_update <", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateLessThanOrEqualTo(Boolean value) {
            addCriterion("contract_destroy_update <=", value, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateIn(List<Boolean> values) {
            addCriterion("contract_destroy_update in", values, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateNotIn(List<Boolean> values) {
            addCriterion("contract_destroy_update not in", values, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateBetween(Boolean value1, Boolean value2) {
            addCriterion("contract_destroy_update between", value1, value2, "contractDestroyUpdate");
            return (Criteria) this;
        }

        public Criteria andContractDestroyUpdateNotBetween(Boolean value1, Boolean value2) {
            addCriterion("contract_destroy_update not between", value1, value2, "contractDestroyUpdate");
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