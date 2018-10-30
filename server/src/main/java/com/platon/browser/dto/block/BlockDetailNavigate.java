package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockDetailNavigate extends BlockDetail {
    // 是否最后一条
    private boolean last;
}
