package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import lombok.Data;

import java.util.List;

@Data
public class TokenInventoryEvent implements Event {

    private List<TokenInventoryWithBLOBs> tokenList;

}
