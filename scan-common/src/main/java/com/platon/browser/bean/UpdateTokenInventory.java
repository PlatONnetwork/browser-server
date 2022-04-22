package com.platon.browser.bean;

import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;

public class UpdateTokenInventory extends TokenInventoryWithBLOBs {

    private String imageUrl;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

}
