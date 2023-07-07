package com.platon.browser.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JUnitTestUtils {

    private static Boolean isRunningTest = null;

    /**
     * 检测是否在运行单元测试
     * @return
     */
    public static Boolean IsRunningTest() {
        /*System.out.println("检测是否在运行单元测试");
        int idx = 0;*/
        if (null == isRunningTest) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            List statckList = Arrays.asList(stackTrace);
            for (Iterator i = statckList.iterator(); i.hasNext(); ) {
                String stackString = i.next().toString();
                //System.out.println(idx++ + ":" + stackString);
                // junit.runners: 4.0 , org.junit.jupiter.engine: 5.0
                if (stackString.contains("junit.runners") || stackString.contains("org.junit.jupiter.engine")) {
                    isRunningTest = true;
                    return isRunningTest;
                }
            }
            isRunningTest = false;
            return isRunningTest;
        } else {
            return isRunningTest;
        }
    }
}
