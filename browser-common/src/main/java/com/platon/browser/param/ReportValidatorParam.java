package com.platon.browser.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.param.evidence.Evidence;
import lombok.Data;

import java.lang.reflect.Field;
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

    public void init ( BigInteger type, String data ) {
        this.type = type;
        this.data = data;
        this.verify = format(type, data);
    }


    private String format ( BigInteger type, String date ) {
        String info = "";
        Evidence evidence = JSONObject.parseObject(date, Evidence.class);
        if (isObjectFieldEmpty(evidence)) {
            if (isObjectFieldEmpty(evidence.getPrepareA())) {
                info = evidence.getPrepareA().getValidateNode().getNodeId();
            }
        }
        return info;
    }

    //反射获取对象属性是否为空
    public boolean isObjectFieldEmpty ( Object object ) {
        boolean flag = false;
        if (object != null) {
            Class <?> entity = object.getClass();
            Field[] fields = entity.getDeclaredFields();//获取该类的所有成员变量（私有的）
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    if (field.get(object) != null && !"".equals(field.get(object))) {
                        flag = true;
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

}
