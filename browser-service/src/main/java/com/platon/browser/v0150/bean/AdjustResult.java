package com.platon.browser.v0150.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 调整结果
 */
@Data
public class AdjustResult {
    // 是否成功
    private boolean success=true;
    // 错误信息<调整参数,错误信息(字符串)>
    private Map<AdjustParam, String> errors = new HashMap<>();

    /**
     * 校验是否有错误
     */
    public boolean validate(){
        for (Map.Entry<AdjustParam, String> entry : errors.entrySet()) {
            String v = entry.getValue();
            if (v!=null) {
                success = false;
                break;
            }
        }
        return success;
    }
}
