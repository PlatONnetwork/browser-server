
package com.platon.browser.dto.keybase;

import lombok.Data;

@Data
public class KeyFingerprint extends ValueScore {
    private Integer algo;
    private Integer nbits;
}
