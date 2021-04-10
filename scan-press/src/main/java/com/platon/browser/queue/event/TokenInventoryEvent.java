package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.TokenInventory;
import lombok.Data;

import java.util.List;

@Data
public class TokenInventoryEvent implements Event {

    private List<TokenInventory> tokenList;

}
