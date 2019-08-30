
package com.platon.browser.engine.bean.keystore;
import lombok.Data;

import java.util.List;

@Data
public class KeystoreUser {
    private Status status;
    private List<Completion> completions;
}
