package com.platon.browser.util;

import com.platon.browser.dto.keybase.Completion;
import com.platon.browser.dto.keybase.Components;
import com.platon.browser.dto.keybase.KeyBaseUser;
import com.platon.browser.dto.keybase.ValueScore;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/9/28
 * @Description: keyBase解析工具
 */
public class KeyBaseAnalysis {

    public static String getKeyBaseUseName( KeyBaseUser keyBaseUser) throws Exception{
        List <Completion> completions = keyBaseUser.getCompletions();
        if (completions == null || completions.isEmpty()) return null;
        // 取最新一条
        Completion completion = completions.get(0);
        Components components = completion.getComponents();
        ValueScore vs = components.getUsername();
        if(vs==null) return null;
        String username = vs.getVal();
        return username;
    }


    public static String getKeyBaseIcon( KeyBaseUser keyBaseUser) throws Exception{
        List <Completion> completions = keyBaseUser.getCompletions();
        if (completions == null || completions.isEmpty()) return null;
        // 取最新一条
        Completion completion = completions.get(0);
        //获取头像
        String icon = completion.getThumbnail();
        return icon;
    }
}