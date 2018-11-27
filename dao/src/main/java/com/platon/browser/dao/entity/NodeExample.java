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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
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

        public Criteria andIntroIsNull() {
            addCriterion("intro is null");
            return (Criteria) this;
        }

        public Criteria andIntroIsNotNull() {
            addCriterion("intro is not null");
            return (Criteria) this;
        }

        public Criteria andIntroEqualTo(String value) {
            addCriterion("intro =", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroNotEqualTo(String value) {
            addCriterion("intro <>", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroGreaterThan(String value) {
            addCriterion("intro >", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroGreaterThanOrEqualTo(String value) {
            addCriterion("intro >=", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroLessThan(String value) {
            addCriterion("intro <", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroLessThanOrEqualTo(String value) {
            addCriterion("intro <=", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroLike(String value) {
            addCriterion("intro like", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroNotLike(String value) {
            addCriterion("intro not like", value, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroIn(List<String> values) {
            addCriterion("intro in", values, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroNotIn(List<String> values) {
            addCriterion("intro not in", values, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroBetween(String value1, String value2) {
            addCriterion("intro between", value1, value2, "intro");
            return (Criteria) this;
        }

        public Criteria andIntroNotBetween(String value1, String value2) {
            addCriterion("intro not between", value1, value2, "intro");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIsNull() {
            addCriterion("public_key is null");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIsNotNull() {
            addCriterion("public_key is not null");
            return (Criteria) this;
        }

        public Criteria andPublicKeyEqualTo(String value) {
            addCriterion("public_key =", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotEqualTo(String value) {
            addCriterion("public_key <>", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyGreaterThan(String value) {
            addCriterion("public_key >", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyGreaterThanOrEqualTo(String value) {
            addCriterion("public_key >=", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLessThan(String value) {
            addCriterion("public_key <", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLessThanOrEqualTo(String value) {
            addCriterion("public_key <=", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLike(String value) {
            addCriterion("public_key like", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotLike(String value) {
            addCriterion("public_key not like", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIn(List<String> values) {
            addCriterion("public_key in", values, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotIn(List<String> values) {
            addCriterion("public_key not in", values, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyBetween(String value1, String value2) {
            addCriterion("public_key between", value1, value2, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotBetween(String value1, String value2) {
            addCriterion("public_key not between", value1, value2, "publicKey");
            return (Criteria) this;
        }

        public Criteria andWalletIsNull() {
            addCriterion("wallet is null");
            return (Criteria) this;
        }

        public Criteria andWalletIsNotNull() {
            addCriterion("wallet is not null");
            return (Criteria) this;
        }

        public Criteria andWalletEqualTo(String value) {
            addCriterion("wallet =", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletNotEqualTo(String value) {
            addCriterion("wallet <>", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletGreaterThan(String value) {
            addCriterion("wallet >", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletGreaterThanOrEqualTo(String value) {
            addCriterion("wallet >=", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletLessThan(String value) {
            addCriterion("wallet <", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletLessThanOrEqualTo(String value) {
            addCriterion("wallet <=", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletLike(String value) {
            addCriterion("wallet like", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletNotLike(String value) {
            addCriterion("wallet not like", value, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletIn(List<String> values) {
            addCriterion("wallet in", values, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletNotIn(List<String> values) {
            addCriterion("wallet not in", values, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletBetween(String value1, String value2) {
            addCriterion("wallet between", value1, value2, "wallet");
            return (Criteria) this;
        }

        public Criteria andWalletNotBetween(String value1, String value2) {
            addCriterion("wallet not between", value1, value2, "wallet");
            return (Criteria) this;
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

        public Criteria andNodeStatusIsNull() {
            addCriterion("node_status is null");
            return (Criteria) this;
        }

        public Criteria andNodeStatusIsNotNull() {
            addCriterion("node_status is not null");
            return (Criteria) this;
        }

        public Criteria andNodeStatusEqualTo(Integer value) {
            addCriterion("node_status =", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusNotEqualTo(Integer value) {
            addCriterion("node_status <>", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusGreaterThan(Integer value) {
            addCriterion("node_status >", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("node_status >=", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusLessThan(Integer value) {
            addCriterion("node_status <", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("node_status <=", value, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusIn(List<Integer> values) {
            addCriterion("node_status in", values, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusNotIn(List<Integer> values) {
            addCriterion("node_status not in", values, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusBetween(Integer value1, Integer value2) {
            addCriterion("node_status between", value1, value2, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andNodeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("node_status not between", value1, value2, "nodeStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusIsNull() {
            addCriterion("`election status` is null");
            return (Criteria) this;
        }

        public Criteria andElectionStatusIsNotNull() {
            addCriterion("`election status` is not null");
            return (Criteria) this;
        }

        public Criteria andElectionStatusEqualTo(Integer value) {
            addCriterion("`election status` =", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusNotEqualTo(Integer value) {
            addCriterion("`election status` <>", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusGreaterThan(Integer value) {
            addCriterion("`election status` >", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`election status` >=", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusLessThan(Integer value) {
            addCriterion("`election status` <", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`election status` <=", value, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusIn(List<Integer> values) {
            addCriterion("`election status` in", values, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusNotIn(List<Integer> values) {
            addCriterion("`election status` not in", values, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusBetween(Integer value1, Integer value2) {
            addCriterion("`election status` between", value1, value2, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andElectionStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`election status` not between", value1, value2, "electionStatus");
            return (Criteria) this;
        }

        public Criteria andDepositIsNull() {
            addCriterion("deposit is null");
            return (Criteria) this;
        }

        public Criteria andDepositIsNotNull() {
            addCriterion("deposit is not null");
            return (Criteria) this;
        }

        public Criteria andDepositEqualTo(String value) {
            addCriterion("deposit =", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositNotEqualTo(String value) {
            addCriterion("deposit <>", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositGreaterThan(String value) {
            addCriterion("deposit >", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositGreaterThanOrEqualTo(String value) {
            addCriterion("deposit >=", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositLessThan(String value) {
            addCriterion("deposit <", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositLessThanOrEqualTo(String value) {
            addCriterion("deposit <=", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositLike(String value) {
            addCriterion("deposit like", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositNotLike(String value) {
            addCriterion("deposit not like", value, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositIn(List<String> values) {
            addCriterion("deposit in", values, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositNotIn(List<String> values) {
            addCriterion("deposit not in", values, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositBetween(String value1, String value2) {
            addCriterion("deposit between", value1, value2, "deposit");
            return (Criteria) this;
        }

        public Criteria andDepositNotBetween(String value1, String value2) {
            addCriterion("deposit not between", value1, value2, "deposit");
            return (Criteria) this;
        }

        public Criteria andRankingIsNull() {
            addCriterion("ranking is null");
            return (Criteria) this;
        }

        public Criteria andRankingIsNotNull() {
            addCriterion("ranking is not null");
            return (Criteria) this;
        }

        public Criteria andRankingEqualTo(Integer value) {
            addCriterion("ranking =", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingNotEqualTo(Integer value) {
            addCriterion("ranking <>", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingGreaterThan(Integer value) {
            addCriterion("ranking >", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingGreaterThanOrEqualTo(Integer value) {
            addCriterion("ranking >=", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingLessThan(Integer value) {
            addCriterion("ranking <", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingLessThanOrEqualTo(Integer value) {
            addCriterion("ranking <=", value, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingIn(List<Integer> values) {
            addCriterion("ranking in", values, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingNotIn(List<Integer> values) {
            addCriterion("ranking not in", values, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingBetween(Integer value1, Integer value2) {
            addCriterion("ranking between", value1, value2, "ranking");
            return (Criteria) this;
        }

        public Criteria andRankingNotBetween(Integer value1, Integer value2) {
            addCriterion("ranking not between", value1, value2, "ranking");
            return (Criteria) this;
        }

        public Criteria andOrgNameIsNull() {
            addCriterion("org_name is null");
            return (Criteria) this;
        }

        public Criteria andOrgNameIsNotNull() {
            addCriterion("org_name is not null");
            return (Criteria) this;
        }

        public Criteria andOrgNameEqualTo(String value) {
            addCriterion("org_name =", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameNotEqualTo(String value) {
            addCriterion("org_name <>", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameGreaterThan(String value) {
            addCriterion("org_name >", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameGreaterThanOrEqualTo(String value) {
            addCriterion("org_name >=", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameLessThan(String value) {
            addCriterion("org_name <", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameLessThanOrEqualTo(String value) {
            addCriterion("org_name <=", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameLike(String value) {
            addCriterion("org_name like", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameNotLike(String value) {
            addCriterion("org_name not like", value, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameIn(List<String> values) {
            addCriterion("org_name in", values, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameNotIn(List<String> values) {
            addCriterion("org_name not in", values, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameBetween(String value1, String value2) {
            addCriterion("org_name between", value1, value2, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgNameNotBetween(String value1, String value2) {
            addCriterion("org_name not between", value1, value2, "orgName");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteIsNull() {
            addCriterion("org_website is null");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteIsNotNull() {
            addCriterion("org_website is not null");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteEqualTo(String value) {
            addCriterion("org_website =", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteNotEqualTo(String value) {
            addCriterion("org_website <>", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteGreaterThan(String value) {
            addCriterion("org_website >", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteGreaterThanOrEqualTo(String value) {
            addCriterion("org_website >=", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteLessThan(String value) {
            addCriterion("org_website <", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteLessThanOrEqualTo(String value) {
            addCriterion("org_website <=", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteLike(String value) {
            addCriterion("org_website like", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteNotLike(String value) {
            addCriterion("org_website not like", value, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteIn(List<String> values) {
            addCriterion("org_website in", values, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteNotIn(List<String> values) {
            addCriterion("org_website not in", values, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteBetween(String value1, String value2) {
            addCriterion("org_website between", value1, value2, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andOrgWebsiteNotBetween(String value1, String value2) {
            addCriterion("org_website not between", value1, value2, "orgWebsite");
            return (Criteria) this;
        }

        public Criteria andChainIdIsNull() {
            addCriterion("chain_id is null");
            return (Criteria) this;
        }

        public Criteria andChainIdIsNotNull() {
            addCriterion("chain_id is not null");
            return (Criteria) this;
        }

        public Criteria andChainIdEqualTo(String value) {
            addCriterion("chain_id =", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotEqualTo(String value) {
            addCriterion("chain_id <>", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdGreaterThan(String value) {
            addCriterion("chain_id >", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdGreaterThanOrEqualTo(String value) {
            addCriterion("chain_id >=", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLessThan(String value) {
            addCriterion("chain_id <", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLessThanOrEqualTo(String value) {
            addCriterion("chain_id <=", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdLike(String value) {
            addCriterion("chain_id like", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotLike(String value) {
            addCriterion("chain_id not like", value, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdIn(List<String> values) {
            addCriterion("chain_id in", values, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotIn(List<String> values) {
            addCriterion("chain_id not in", values, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdBetween(String value1, String value2) {
            addCriterion("chain_id between", value1, value2, "chainId");
            return (Criteria) this;
        }

        public Criteria andChainIdNotBetween(String value1, String value2) {
            addCriterion("chain_id not between", value1, value2, "chainId");
            return (Criteria) this;
        }

        public Criteria andBlockCountIsNull() {
            addCriterion("block_count is null");
            return (Criteria) this;
        }

        public Criteria andBlockCountIsNotNull() {
            addCriterion("block_count is not null");
            return (Criteria) this;
        }

        public Criteria andBlockCountEqualTo(Integer value) {
            addCriterion("block_count =", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountNotEqualTo(Integer value) {
            addCriterion("block_count <>", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountGreaterThan(Integer value) {
            addCriterion("block_count >", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_count >=", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountLessThan(Integer value) {
            addCriterion("block_count <", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountLessThanOrEqualTo(Integer value) {
            addCriterion("block_count <=", value, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountIn(List<Integer> values) {
            addCriterion("block_count in", values, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountNotIn(List<Integer> values) {
            addCriterion("block_count not in", values, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountBetween(Integer value1, Integer value2) {
            addCriterion("block_count between", value1, value2, "blockCount");
            return (Criteria) this;
        }

        public Criteria andBlockCountNotBetween(Integer value1, Integer value2) {
            addCriterion("block_count not between", value1, value2, "blockCount");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIsNull() {
            addCriterion("join_time is null");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIsNotNull() {
            addCriterion("join_time is not null");
            return (Criteria) this;
        }

        public Criteria andJoinTimeEqualTo(Date value) {
            addCriterion("join_time =", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotEqualTo(Date value) {
            addCriterion("join_time <>", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeGreaterThan(Date value) {
            addCriterion("join_time >", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("join_time >=", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeLessThan(Date value) {
            addCriterion("join_time <", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeLessThanOrEqualTo(Date value) {
            addCriterion("join_time <=", value, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeIn(List<Date> values) {
            addCriterion("join_time in", values, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotIn(List<Date> values) {
            addCriterion("join_time not in", values, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeBetween(Date value1, Date value2) {
            addCriterion("join_time between", value1, value2, "joinTime");
            return (Criteria) this;
        }

        public Criteria andJoinTimeNotBetween(Date value1, Date value2) {
            addCriterion("join_time not between", value1, value2, "joinTime");
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

        public Criteria andRewardRatioIsNull() {
            addCriterion("reward_ratio is null");
            return (Criteria) this;
        }

        public Criteria andRewardRatioIsNotNull() {
            addCriterion("reward_ratio is not null");
            return (Criteria) this;
        }

        public Criteria andRewardRatioEqualTo(Double value) {
            addCriterion("reward_ratio =", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioNotEqualTo(Double value) {
            addCriterion("reward_ratio <>", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioGreaterThan(Double value) {
            addCriterion("reward_ratio >", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioGreaterThanOrEqualTo(Double value) {
            addCriterion("reward_ratio >=", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioLessThan(Double value) {
            addCriterion("reward_ratio <", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioLessThanOrEqualTo(Double value) {
            addCriterion("reward_ratio <=", value, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioIn(List<Double> values) {
            addCriterion("reward_ratio in", values, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioNotIn(List<Double> values) {
            addCriterion("reward_ratio not in", values, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioBetween(Double value1, Double value2) {
            addCriterion("reward_ratio between", value1, value2, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andRewardRatioNotBetween(Double value1, Double value2) {
            addCriterion("reward_ratio not between", value1, value2, "rewardRatio");
            return (Criteria) this;
        }

        public Criteria andVerifyCountIsNull() {
            addCriterion("verify_count is null");
            return (Criteria) this;
        }

        public Criteria andVerifyCountIsNotNull() {
            addCriterion("verify_count is not null");
            return (Criteria) this;
        }

        public Criteria andVerifyCountEqualTo(Long value) {
            addCriterion("verify_count =", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountNotEqualTo(Long value) {
            addCriterion("verify_count <>", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountGreaterThan(Long value) {
            addCriterion("verify_count >", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountGreaterThanOrEqualTo(Long value) {
            addCriterion("verify_count >=", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountLessThan(Long value) {
            addCriterion("verify_count <", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountLessThanOrEqualTo(Long value) {
            addCriterion("verify_count <=", value, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountIn(List<Long> values) {
            addCriterion("verify_count in", values, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountNotIn(List<Long> values) {
            addCriterion("verify_count not in", values, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountBetween(Long value1, Long value2) {
            addCriterion("verify_count between", value1, value2, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andVerifyCountNotBetween(Long value1, Long value2) {
            addCriterion("verify_count not between", value1, value2, "verifyCount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountIsNull() {
            addCriterion("reward_amount is null");
            return (Criteria) this;
        }

        public Criteria andRewardAmountIsNotNull() {
            addCriterion("reward_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRewardAmountEqualTo(String value) {
            addCriterion("reward_amount =", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountNotEqualTo(String value) {
            addCriterion("reward_amount <>", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountGreaterThan(String value) {
            addCriterion("reward_amount >", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountGreaterThanOrEqualTo(String value) {
            addCriterion("reward_amount >=", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountLessThan(String value) {
            addCriterion("reward_amount <", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountLessThanOrEqualTo(String value) {
            addCriterion("reward_amount <=", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountLike(String value) {
            addCriterion("reward_amount like", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountNotLike(String value) {
            addCriterion("reward_amount not like", value, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountIn(List<String> values) {
            addCriterion("reward_amount in", values, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountNotIn(List<String> values) {
            addCriterion("reward_amount not in", values, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountBetween(String value1, String value2) {
            addCriterion("reward_amount between", value1, value2, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andRewardAmountNotBetween(String value1, String value2) {
            addCriterion("reward_amount not between", value1, value2, "rewardAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountIsNull() {
            addCriterion("profit_amount is null");
            return (Criteria) this;
        }

        public Criteria andProfitAmountIsNotNull() {
            addCriterion("profit_amount is not null");
            return (Criteria) this;
        }

        public Criteria andProfitAmountEqualTo(String value) {
            addCriterion("profit_amount =", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountNotEqualTo(String value) {
            addCriterion("profit_amount <>", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountGreaterThan(String value) {
            addCriterion("profit_amount >", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountGreaterThanOrEqualTo(String value) {
            addCriterion("profit_amount >=", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountLessThan(String value) {
            addCriterion("profit_amount <", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountLessThanOrEqualTo(String value) {
            addCriterion("profit_amount <=", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountLike(String value) {
            addCriterion("profit_amount like", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountNotLike(String value) {
            addCriterion("profit_amount not like", value, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountIn(List<String> values) {
            addCriterion("profit_amount in", values, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountNotIn(List<String> values) {
            addCriterion("profit_amount not in", values, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountBetween(String value1, String value2) {
            addCriterion("profit_amount between", value1, value2, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andProfitAmountNotBetween(String value1, String value2) {
            addCriterion("profit_amount not between", value1, value2, "profitAmount");
            return (Criteria) this;
        }

        public Criteria andNodeAddressIsNull() {
            addCriterion("node_address is null");
            return (Criteria) this;
        }

        public Criteria andNodeAddressIsNotNull() {
            addCriterion("node_address is not null");
            return (Criteria) this;
        }

        public Criteria andNodeAddressEqualTo(String value) {
            addCriterion("node_address =", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressNotEqualTo(String value) {
            addCriterion("node_address <>", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressGreaterThan(String value) {
            addCriterion("node_address >", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressGreaterThanOrEqualTo(String value) {
            addCriterion("node_address >=", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressLessThan(String value) {
            addCriterion("node_address <", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressLessThanOrEqualTo(String value) {
            addCriterion("node_address <=", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressLike(String value) {
            addCriterion("node_address like", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressNotLike(String value) {
            addCriterion("node_address not like", value, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressIn(List<String> values) {
            addCriterion("node_address in", values, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressNotIn(List<String> values) {
            addCriterion("node_address not in", values, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressBetween(String value1, String value2) {
            addCriterion("node_address between", value1, value2, "nodeAddress");
            return (Criteria) this;
        }

        public Criteria andNodeAddressNotBetween(String value1, String value2) {
            addCriterion("node_address not between", value1, value2, "nodeAddress");
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