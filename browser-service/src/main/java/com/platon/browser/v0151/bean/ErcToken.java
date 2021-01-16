package com.platon.browser.v0151.bean;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.v0151.enums.ErcTypeEnum;
import lombok.Data;

@Data
public class ErcToken extends Token {
    private ErcTypeEnum typeEnum;
}
