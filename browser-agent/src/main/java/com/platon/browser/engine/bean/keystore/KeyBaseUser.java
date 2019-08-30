
package com.platon.browser.engine.bean.keystore;
import lombok.Data;

import java.util.List;

@Data
public class KeyBaseUser {
    private Status status;
    private List<Completion> completions;
}
