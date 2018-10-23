package com.platon.browserweb.common.base;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.platon.browserweb.common.enums.ErrorCodeEnum;
import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.util.I18NUtils;


@XmlRootElement
public class JsonResp<T> {

    private T data;

    //成功（0），失败则由相关失败码
    private int result = 0;

    //相关的错误信息
    private String errMsg = "";

    private int pageNo;       //	option	int	第几页数据
    private int totalPages;    //	option	int	数据总页数
    private int totalCount;     //	option	int	数据总条数

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrMsg() {
        //return I18NUtils.getInstance().getResource(2000);
        if(result > 0 && StringUtils.isEmpty(errMsg)){
            errMsg = I18NUtils.getInstance().getResource(result);
        }
        return errMsg;
    }

    private JsonResp () {

    }


    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setData(T value) {
        data=value;
    }

    public T getData(){
        return data;
    }

    public void setPagination(int pageNo, int pageSize, int totalCount ){
        //总记录数
        this.setTotalCount(totalCount);

        //
        int totalPages;
        if (pageSize <= 0){
            totalPages = 1;
        } else {
            totalPages = (totalCount - 1) / pageSize + 1;
        }

        //总页数，每页记录数用请求消息里的定义
        this.setTotalPages(totalPages);

        //当前数据是第几页，如果请求的页码小余重新计算的总记录数，则页码不变；否则页码就是总页数
        this.setPageNo(pageNo <= totalPages ? pageNo : totalPages);

    }

    /**
     * 基础响应
     */
    public static class BaseResp {

        JsonResp resp;

        public BaseResp(JsonResp resp) {
            this.resp = resp;
        }

        public JsonResp build() {
            return resp;
        }

        /**
         * 设置响应失败标志.
         *
         * @param errorCodeEnum 消息.
         * @return 该响应对象.
         */
        public BaseResp errorMsg(ErrorCodeEnum errorCodeEnum) {
            if (errorCodeEnum == null) {
                errorCodeEnum = ErrorCodeEnum.Default;
            }
            resp.setResult(errorCodeEnum.getCode());
            resp.setErrMsg(errorCodeEnum.getDesc());
            return this;
        }

        /**
         * 设置响应失败标志.
         *
         * @param message 消息.
         * @return 该响应对象.
         */
        public BaseResp errorMsg(int result, String message) {
            resp.setResult(result);
            resp.setErrMsg(message);
            return this;
        }

        /**
         * 设置响应分页信息.
         * @param req 分页请求
         * @return 该响应对象.
         */
        public BaseResp pagination(PageReq req) {
            if (req.getPageNo() != null && req.getPageSize() != null) {
                resp.setPagination(req.getPageNo(), req.getPageSize(), req.getTotal());
            }
            return this;
        }

        /**
         * 设置响应分页信息.
         * @param pageNo 当前页
         * @return 该响应对象.
         */
        public BaseResp pagination(int pageNo, int pageSize, int totalCount) {
            resp.setPagination(pageNo, pageSize, totalCount);
            return this;
        }

        /**
         * 设置响应对象.
         *
         * @param data 对象.
         * @return 该响应对象.
         */
        public BaseResp data(Object data) {
            resp.setData(data);
            return this;
        }
    }

    /**
     * 树形结构响应
     */
    public static class JMap extends BaseResp {

        public JMap(JsonResp resp) {
            super(resp);
            resp.data = new HashMap<String, Object>();
        }

        /**
         * 增加一个map的内容到数据对象中.
         *
         * @param data map内容
         * @return 返回该响应对象.
         */
        @SuppressWarnings("unchecked")
        public JMap putAll(Map<String, ?> data) {
            if (MapUtils.isNotEmpty(data)) {
                ((Map) resp.data).putAll(data);
            }
            return this;
        }

        /**
         * 增加一个kv对到数据对象中.
         *
         * @param key key
         * @param value value
         * @return 返回该响应对象.
         */
        @SuppressWarnings("unchecked")
        public JMap put(String key, Object value) {
            ((Map) resp.data).put(key, value);
            return this;
        }
    }

    /**
     * 创建树形结构响应.
     *
     * @return 响应对象.
     */
    public static JMap asMap() {
        JsonResp resp = new JsonResp();
        JMap jMap = new JMap(resp);
        return jMap;
    }

    /**
     *  列表类
     */
    public static class JList extends BaseResp {

        public JList(JsonResp resp) {
            super(resp);
            resp.data = new ArrayList<Object>();
        }

        /**
         * 增加数据集合到数据列表中.
         *
         * @param collection 数据集合
         * @return 该响应对象.
         */
        @SuppressWarnings("unchecked")
        public JList addAll(Collection collection) {
            if (CollectionUtils.isNotEmpty(collection)) {
                ((List) resp.data).addAll(collection);
            }
            return this;
        }
    }

    /**
     * 创建列表响应
     *
     * @return 列表响应
     */
    public static JList asList() {
        JsonResp resp = new JsonResp();
        JList jList = new JList(resp);
        return jList;
    }

    /**
     * 创建空结构响应.
     *
     * @return 响应对象.
     */
    public static JEmpty asEmpty() {
        JsonResp resp = new JsonResp();
        JEmpty jEmpty = new JEmpty(resp);
        return jEmpty;
    }

    /**
     * 空结构响应
     */
    public static class JEmpty extends BaseResp {

        public JEmpty(JsonResp resp) {
            super(resp);
        }
    }

    @Override
    public String toString() {
        return "JsonResp{" +
                "data=" + data +
                ", result=" + result +
                ", errMsg='" + errMsg + '\'' +
                ", pageNo=" + pageNo +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                '}';
    }
}
