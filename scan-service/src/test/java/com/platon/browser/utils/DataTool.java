package com.platon.browser.utils;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

public class DataTool extends TestData {

    public static <T> List<T> getTestData(String chainId, TestDataFileNameEnum dataFileNameEnum, Class<T> clazz) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(testDataDir+dataFileNameEnum.prefix+chainId+".json"));
            StringBuffer sb = new StringBuffer();
            reader.lines().forEach(line->sb.append(line));
            reader.close();
            if(sb.length()>0){
                List<T> data = JSON.parseArray(sb.toString(),clazz);
                return data;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
