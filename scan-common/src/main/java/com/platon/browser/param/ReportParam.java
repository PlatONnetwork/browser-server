package com.platon.browser.param;

import com.alibaba.fastjson.JSON;
import com.platon.browser.param.evidence.PrepareEvidence;
import com.platon.browser.param.evidence.VoteEvidence;
import com.platon.browser.utils.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
@AllArgsConstructor
@Accessors(chain = true)
public class ReportParam extends TxParam{

    /**
     * 多签类型
     */
    private BigInteger type;

    /**
     * 证据的json值，格式为RPC接口Evidences的返回值
     */
    private String data;

    /**
     * 被举报的节点id
     */
    private String verify;
    public void setVerify(String verify){
        this.verify= HexUtil.prefix(verify);
    }

    /**
     * 被举报节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private BigInteger stakingBlockNum;
    
    /**
     * 举报金额
     */
    private BigDecimal reward;

    public ReportParam init() {
        this.verify = format(type, data);
        return this;
    }

    private String format ( BigInteger type, String date ) {
        String info = "";
        try {
        	if(BigInteger.ONE.compareTo(type) == 0) {
        		PrepareEvidence evidence = JSON.parseObject(date, PrepareEvidence.class);
            	if (isObjectFieldEmpty(evidence)) {
                    if (isObjectFieldEmpty(evidence.getPrepareA())) {
                        info = evidence.getPrepareA().getValidateNode().getNodeId();
                    }
                }
        	} else if(BigInteger.valueOf(2l).compareTo(type) == 0) {
        		VoteEvidence evidence = JSON.parseObject(date, VoteEvidence.class);
            	if (isObjectFieldEmpty(evidence)) {
                    if (isObjectFieldEmpty(evidence.getVoteA())) {
                        info = evidence.getVoteA().getValidateNode().getNodeId();
                    }
                }
        	}
        	
		} catch (Exception e) {
			log.error("json decode error", e);
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
