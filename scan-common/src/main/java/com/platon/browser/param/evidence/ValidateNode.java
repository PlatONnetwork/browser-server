
package com.platon.browser.param.evidence;

import com.platon.browser.utils.HexUtil;
import lombok.Data;

@Data
public class ValidateNode {

    private int index;
    private String address;
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId= HexUtil.prefix(nodeId);
    }
    private String blsPubKey;

}