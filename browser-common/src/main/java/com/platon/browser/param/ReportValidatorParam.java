package com.platon.browser.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:17
 * txType=3000举报多签(举报验证人)
 */
@Data
public class ReportValidatorParam {

    /**
     * 多签类型
     */
    private BigInteger type;

    /**
     * 证据的json值，格式为RPC接口Evidences的返回值
     */
    private String data;

    /**
     * 举报的节点id
     */
    private String verify;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private String stakingBlockNum;

    public void init(BigInteger type, String data)throws Exception{
        this.type = type;
        this.data = data;
        this.verify = format(type,data);
    }


    private String format(BigInteger type,String date){
        JSONObject jsonObject = JSON.parseObject(date);
        JSONObject base = new JSONObject();
        switch (type.intValue()){
            case 1:
                base =  jsonObject.getJSONObject("prepare_a");
                break;
            case 2:
                base =  jsonObject.getJSONObject("vote_a");
                break;
            case 3:
                base =  jsonObject.getJSONObject("view_a");
                break;
        }
        JSONObject validateNode = base.getJSONObject("validate_node");
        String nodeId = validateNode.getString("NodeID");
        return nodeId;
    }
}
