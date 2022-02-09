package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenInventoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TokenInventoryExample() {
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

        public Criteria andTokenAddressIsNull() {
            addCriterion("token_address is null");
            return (Criteria) this;
        }

        public Criteria andTokenAddressIsNotNull() {
            addCriterion("token_address is not null");
            return (Criteria) this;
        }

        public Criteria andTokenAddressEqualTo(String value) {
            addCriterion("token_address =", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressNotEqualTo(String value) {
            addCriterion("token_address <>", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressGreaterThan(String value) {
            addCriterion("token_address >", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressGreaterThanOrEqualTo(String value) {
            addCriterion("token_address >=", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressLessThan(String value) {
            addCriterion("token_address <", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressLessThanOrEqualTo(String value) {
            addCriterion("token_address <=", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressLike(String value) {
            addCriterion("token_address like", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressNotLike(String value) {
            addCriterion("token_address not like", value, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressIn(List<String> values) {
            addCriterion("token_address in", values, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressNotIn(List<String> values) {
            addCriterion("token_address not in", values, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressBetween(String value1, String value2) {
            addCriterion("token_address between", value1, value2, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenAddressNotBetween(String value1, String value2) {
            addCriterion("token_address not between", value1, value2, "tokenAddress");
            return (Criteria) this;
        }

        public Criteria andTokenIdIsNull() {
            addCriterion("token_id is null");
            return (Criteria) this;
        }

        public Criteria andTokenIdIsNotNull() {
            addCriterion("token_id is not null");
            return (Criteria) this;
        }

        public Criteria andTokenIdEqualTo(String value) {
            addCriterion("token_id =", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdNotEqualTo(String value) {
            addCriterion("token_id <>", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdGreaterThan(String value) {
            addCriterion("token_id >", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdGreaterThanOrEqualTo(String value) {
            addCriterion("token_id >=", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdLessThan(String value) {
            addCriterion("token_id <", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdLessThanOrEqualTo(String value) {
            addCriterion("token_id <=", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdLike(String value) {
            addCriterion("token_id like", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdNotLike(String value) {
            addCriterion("token_id not like", value, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdIn(List<String> values) {
            addCriterion("token_id in", values, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdNotIn(List<String> values) {
            addCriterion("token_id not in", values, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdBetween(String value1, String value2) {
            addCriterion("token_id between", value1, value2, "tokenId");
            return (Criteria) this;
        }

        public Criteria andTokenIdNotBetween(String value1, String value2) {
            addCriterion("token_id not between", value1, value2, "tokenId");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNull() {
            addCriterion("`owner` is null");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNotNull() {
            addCriterion("`owner` is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerEqualTo(String value) {
            addCriterion("`owner` =", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotEqualTo(String value) {
            addCriterion("`owner` <>", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThan(String value) {
            addCriterion("`owner` >", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThanOrEqualTo(String value) {
            addCriterion("`owner` >=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThan(String value) {
            addCriterion("`owner` <", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThanOrEqualTo(String value) {
            addCriterion("`owner` <=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLike(String value) {
            addCriterion("`owner` like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotLike(String value) {
            addCriterion("`owner` not like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerIn(List<String> values) {
            addCriterion("`owner` in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotIn(List<String> values) {
            addCriterion("`owner` not in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerBetween(String value1, String value2) {
            addCriterion("`owner` between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotBetween(String value1, String value2) {
            addCriterion("`owner` not between", value1, value2, "owner");
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

        public Criteria andImageIsNull() {
            addCriterion("image is null");
            return (Criteria) this;
        }

        public Criteria andImageIsNotNull() {
            addCriterion("image is not null");
            return (Criteria) this;
        }

        public Criteria andImageEqualTo(String value) {
            addCriterion("image =", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotEqualTo(String value) {
            addCriterion("image <>", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThan(String value) {
            addCriterion("image >", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThanOrEqualTo(String value) {
            addCriterion("image >=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThan(String value) {
            addCriterion("image <", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThanOrEqualTo(String value) {
            addCriterion("image <=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLike(String value) {
            addCriterion("image like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotLike(String value) {
            addCriterion("image not like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageIn(List<String> values) {
            addCriterion("image in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotIn(List<String> values) {
            addCriterion("image not in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageBetween(String value1, String value2) {
            addCriterion("image between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotBetween(String value1, String value2) {
            addCriterion("image not between", value1, value2, "image");
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

        public Criteria andTokenOwnerTxQtyIsNull() {
            addCriterion("token_owner_tx_qty is null");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyIsNotNull() {
            addCriterion("token_owner_tx_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyEqualTo(Integer value) {
            addCriterion("token_owner_tx_qty =", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyNotEqualTo(Integer value) {
            addCriterion("token_owner_tx_qty <>", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyGreaterThan(Integer value) {
            addCriterion("token_owner_tx_qty >", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("token_owner_tx_qty >=", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyLessThan(Integer value) {
            addCriterion("token_owner_tx_qty <", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyLessThanOrEqualTo(Integer value) {
            addCriterion("token_owner_tx_qty <=", value, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyIn(List<Integer> values) {
            addCriterion("token_owner_tx_qty in", values, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyNotIn(List<Integer> values) {
            addCriterion("token_owner_tx_qty not in", values, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyBetween(Integer value1, Integer value2) {
            addCriterion("token_owner_tx_qty between", value1, value2, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andTokenOwnerTxQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("token_owner_tx_qty not between", value1, value2, "tokenOwnerTxQty");
            return (Criteria) this;
        }

        public Criteria andSmallImageIsNull() {
            addCriterion("small_image is null");
            return (Criteria) this;
        }

        public Criteria andSmallImageIsNotNull() {
            addCriterion("small_image is not null");
            return (Criteria) this;
        }

        public Criteria andSmallImageEqualTo(String value) {
            addCriterion("small_image =", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageNotEqualTo(String value) {
            addCriterion("small_image <>", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageGreaterThan(String value) {
            addCriterion("small_image >", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageGreaterThanOrEqualTo(String value) {
            addCriterion("small_image >=", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageLessThan(String value) {
            addCriterion("small_image <", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageLessThanOrEqualTo(String value) {
            addCriterion("small_image <=", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageLike(String value) {
            addCriterion("small_image like", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageNotLike(String value) {
            addCriterion("small_image not like", value, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageIn(List<String> values) {
            addCriterion("small_image in", values, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageNotIn(List<String> values) {
            addCriterion("small_image not in", values, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageBetween(String value1, String value2) {
            addCriterion("small_image between", value1, value2, "smallImage");
            return (Criteria) this;
        }

        public Criteria andSmallImageNotBetween(String value1, String value2) {
            addCriterion("small_image not between", value1, value2, "smallImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageIsNull() {
            addCriterion("medium_image is null");
            return (Criteria) this;
        }

        public Criteria andMediumImageIsNotNull() {
            addCriterion("medium_image is not null");
            return (Criteria) this;
        }

        public Criteria andMediumImageEqualTo(String value) {
            addCriterion("medium_image =", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageNotEqualTo(String value) {
            addCriterion("medium_image <>", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageGreaterThan(String value) {
            addCriterion("medium_image >", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageGreaterThanOrEqualTo(String value) {
            addCriterion("medium_image >=", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageLessThan(String value) {
            addCriterion("medium_image <", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageLessThanOrEqualTo(String value) {
            addCriterion("medium_image <=", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageLike(String value) {
            addCriterion("medium_image like", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageNotLike(String value) {
            addCriterion("medium_image not like", value, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageIn(List<String> values) {
            addCriterion("medium_image in", values, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageNotIn(List<String> values) {
            addCriterion("medium_image not in", values, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageBetween(String value1, String value2) {
            addCriterion("medium_image between", value1, value2, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andMediumImageNotBetween(String value1, String value2) {
            addCriterion("medium_image not between", value1, value2, "mediumImage");
            return (Criteria) this;
        }

        public Criteria andRetryNumIsNull() {
            addCriterion("retry_num is null");
            return (Criteria) this;
        }

        public Criteria andRetryNumIsNotNull() {
            addCriterion("retry_num is not null");
            return (Criteria) this;
        }

        public Criteria andRetryNumEqualTo(Integer value) {
            addCriterion("retry_num =", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumNotEqualTo(Integer value) {
            addCriterion("retry_num <>", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumGreaterThan(Integer value) {
            addCriterion("retry_num >", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("retry_num >=", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumLessThan(Integer value) {
            addCriterion("retry_num <", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumLessThanOrEqualTo(Integer value) {
            addCriterion("retry_num <=", value, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumIn(List<Integer> values) {
            addCriterion("retry_num in", values, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumNotIn(List<Integer> values) {
            addCriterion("retry_num not in", values, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumBetween(Integer value1, Integer value2) {
            addCriterion("retry_num between", value1, value2, "retryNum");
            return (Criteria) this;
        }

        public Criteria andRetryNumNotBetween(Integer value1, Integer value2) {
            addCriterion("retry_num not between", value1, value2, "retryNum");
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