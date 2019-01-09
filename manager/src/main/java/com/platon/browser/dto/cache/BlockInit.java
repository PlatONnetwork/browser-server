package com.platon.browser.dto.cache;

import com.platon.browser.dto.block.BlockInfo;
import lombok.Data;

import java.util.List;

@Data
public class BlockInit {
    private boolean changed=false; // 是否有改动
    private List<BlockInfo> list; // 增量数据
    public List<BlockInfo> getList(){
        // 统一serverTime
        long severTime = System.currentTimeMillis();
        list.forEach(blockInfo -> blockInfo.setServerTime(severTime));
        return list;
    }
}
