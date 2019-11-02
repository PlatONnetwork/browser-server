package com.platon.browser.param;

import com.alibaba.fastjson.JSON;
import com.platon.browser.param.evidence.Evidence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:17
 * txType=3000举报多签(举报验证人)
 */
@Data
@Slf4j
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReportValidatorParam extends TxParam{

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

    public void init() {
        this.verify = format(type, data);
    }

    private String format ( BigInteger type, String date ) {
        String info = "";
        Evidence evidence = JSON.parseObject(date, Evidence.class);
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
                    log.error("",e);
                }
            }
        }
        return flag;
    }

}
