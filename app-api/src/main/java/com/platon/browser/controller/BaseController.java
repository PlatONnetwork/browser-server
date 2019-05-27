package com.platon.browser.controller;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 09:36
 * @Description:
 */
public class BaseController {
    /**
     * @api 接口请求头设置说明
     * @apiVersion 1.0.0
     * @apiGroup about request header
     * @apiName 接口请求头设置说明
     * @apiParamExample :
     *
     * 本接口文档中定义的接口中的通用参数统一使用请求头传输，例如访问交易列表接口：
     * curl -X POST \
     *   http://192.168.9.190:20060/app-203/v060//transaction/list \
     *   -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -H 'x-aton-cid: 203' \ # 链ID
    -H 'content-length: 136' \
    -d '{
    "beginSequence":1,
    "listSize":30,
    "address":"0x493301712671ada506ba6ca7891f436d29185821",
    "cid":"20d 3"
    }'
     *
     */
    protected final static String CID = "x-aton-cid";
}
