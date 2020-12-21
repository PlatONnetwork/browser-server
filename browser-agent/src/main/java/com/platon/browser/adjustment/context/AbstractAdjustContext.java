package com.platon.browser.adjustment.context;

import com.alibaba.fastjson.JSON;
import com.platon.browser.adjustment.bean.AdjustParam;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 调账上下文
 */
@Slf4j()
@Data
public abstract class AbstractAdjustContext {
    protected AdjustParam adjustParam;
    protected Staking staking;
    protected Node node;
    protected List<String> errors = new ArrayList<>();
    /**
     * 检查上下文是否有错：需要的数据是否都有
     * @return
     */
    public final List<String> validate(){
        if(adjustParam ==null) {
            errors.add("【错误】：调账数据缺失！");
            return errors;
        }
        // 调账目标记录是否都存在
        if(node==null) errors.add("【错误】：节点记录缺失:[节点ID="+ adjustParam.getNodeId()+"]");
        if(staking==null) errors.add("【错误】：质押记录缺失:[节点ID="+ adjustParam.getNodeId()+",节点质押块号="+ adjustParam.getStakingBlockNum()+"]");
        // 调账目标记录各项金额是否都足够扣减
        validateAmount();
        return errors;
    }

    public final String contextInfo(){
        StringBuilder sb = new StringBuilder(adjustParam.getOptType())
                .append("调账参数：\n")
                .append(JSON.toJSONString(adjustParam,true)).append("\n")
                .append("节点记录：\n").append(JSON.toJSONString(node,true)).append("\n")
                .append("质押记录：\n").append(JSON.toJSONString(staking,true)).append("\n")
                ;
        String extraContextInfo = extraContextInfo();
        if(StringUtils.isNotBlank(extraContextInfo)){
            sb.append(extraContextInfo).append("\n");
        }
        return sb.toString();
    }

    /**
     * 把错误列表转成错误字符串信息
     * @return
     */
    public final String errorInfo(){
        if(errors.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("============ ")
                .append(adjustParam.getOptType())
                .append("调账出错 ============\n")
                .append(adjustParam.getOptType())
                .append("调账参数：\n")
                .append(JSON.toJSONString(adjustParam,true))
                .append("\n");
        errors.forEach(e->sb.append(e).append("\n"));
        log.error("{}",sb.toString());
        return sb.toString();
    }

    /**
     * 校验调账金额数据
     */
    abstract void validateAmount();

    /**
     * 额外的上下文信息
     */
    abstract String extraContextInfo();
}
