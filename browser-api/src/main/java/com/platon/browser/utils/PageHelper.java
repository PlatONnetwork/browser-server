package com.platon.browser.utils;


import com.platon.browser.request.PageReq;

import java.util.Map;

public final class PageHelper {

    public static class PageParams {
        private int offset;
        private int size;
        private int number;

        public PageParams(int offset, int size, int number) {
            this.offset = offset;
            this.size = size;
            this.number = number;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static PageParams buildPageParams(Map map) {
        String pageNumber = null == map.get("pageNumber") ? "1" : map.get("pageNumber").toString();
        String pageSize = null == map.get("pageSize") ? "10" : map.get("pageSize").toString();
        int offset = (Integer.valueOf(pageNumber) -1) * Integer.valueOf(pageSize);
        return new PageParams(offset, Integer.valueOf(pageSize), Integer.valueOf(pageNumber));
    }

    public static PageParams buildPageParams(PageReq req) {
        int pageNumber = req.getPageNo();
        int pageSize = req.getPageSize();
        int offset = (pageNumber -1) * pageSize;
        return new PageParams(offset, pageSize, pageNumber);
    }

    public static int getPageTotal(int total, int pageSize) {
        int pageTotal = 0;
        if (total > 0 && total <= pageSize) {
            pageTotal = 1;
        } else {
            pageTotal = total / pageSize;
            if((total % pageSize)>0){
                pageTotal +=1;
            }
        }
        return pageTotal;
    }


}
