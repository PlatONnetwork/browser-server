package com.platon.browser.dto.block;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockDetailNavigate extends BlockDetail {
    // 是否最后一条
    private boolean last;
}
