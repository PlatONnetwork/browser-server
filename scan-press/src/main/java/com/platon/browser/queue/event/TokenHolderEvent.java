package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.TokenHolder;
import lombok.Data;

import java.util.List;

@Data
public class TokenHolderEvent implements Event {

    private List<TokenHolder> tokenList;

}
