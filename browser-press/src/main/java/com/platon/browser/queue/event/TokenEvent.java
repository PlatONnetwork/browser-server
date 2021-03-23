package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Token;
import lombok.Data;

import java.util.List;

@Data
public class TokenEvent implements Event {

    private List<Token> tokenList;
    
}
