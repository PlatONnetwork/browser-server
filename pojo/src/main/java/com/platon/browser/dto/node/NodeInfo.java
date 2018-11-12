package com.platon.browser.dto.node;

import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 节点信息
 */
@Data
public class NodeInfo {
    private Float longitude;
    private Float latitude;
    private Integer nodeType;
    private Integer netState;

    public int hashCode() {
        return  new HashCodeBuilder(17,37)
                .append(longitude)
                .append(latitude).toHashCode( );
    }

    public boolean equals(Object obj) {
        NodeInfo nodeInfo = (NodeInfo)obj;
        if(this==obj || (this.latitude==nodeInfo.latitude&&this.longitude==nodeInfo.longitude))
            return true;
        return false;
    }

}
