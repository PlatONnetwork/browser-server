package com.platon.browser.dto.node;

import com.platon.browser.dao.entity.NodeRanking;
import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.BeanUtils;

/**
 * 节点信息
 */
@Data
public class NodePushItem {
    private String ip;
    private String longitude;
    private String latitude;
    private Integer nodeType;
    private Integer netState;

    public void init(NodeRanking initData){
        BeanUtils.copyProperties(initData,this);
        this.setNodeType(initData.getType());
        this.setNetState(1);
    }

    public int hashCode() {
        return  new HashCodeBuilder(17,37)
                .append(longitude)
                .append(latitude).toHashCode( );
    }

    public boolean equals(Object obj) {
        NodePushItem nodeInfo = (NodePushItem)obj;
        if(this==obj || (this.latitude==nodeInfo.latitude&&this.longitude==nodeInfo.longitude))
            return true;
        return false;
    }

}
