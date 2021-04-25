package com.platon.browser.task.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenInventoryUpdate {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 当前页码是否已更新
     */
    private boolean isUpdate;

    /**
     * 当前页码的条数
     */
    private Integer num;

    /**
     * 当前页码是否已更新
     *
     * @param page 页码
     * @param size 条数
     * @return boolean
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/22
     */
    public boolean getPageUpdate(Integer page, Integer size) {
        if (page == this.getPage()) {
            return this.isUpdate;
        } else {
            return false;
        }
    }

    public void update(Integer page, boolean isUpdate, Integer num) {
        this.page = page;
        this.isUpdate = isUpdate;
        this.num = num;
    }


}
