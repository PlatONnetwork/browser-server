package com.platon.browser.adjustment.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调整结果
 */
@Data
public class AdjustResult {
    // 是否成功
    private boolean success=true;
    // 错误信息<调整参数,错误信息>
    private Map<AdjustParam, List<String>> errors = new HashMap<>();

    /**
     * 校验是否有错误
     */
    public boolean validate(){
        for (Map.Entry<AdjustParam, List<String>> entry : errors.entrySet()) {
            AdjustParam k = entry.getKey();
            List<String> v = entry.getValue();
            if (!v.isEmpty()) {
                success = false;
                break;
            }
        }
        return success;
    }
}
