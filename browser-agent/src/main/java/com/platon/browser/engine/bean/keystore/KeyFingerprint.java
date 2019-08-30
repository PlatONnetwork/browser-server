
package com.platon.browser.engine.bean.keystore;

import lombok.Data;

@Data
public class KeyFingerprint extends ValueScore {
    private int algo;
    private int nbits;
}
