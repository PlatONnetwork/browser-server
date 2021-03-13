package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Erc20Token;
import lombok.Data;

import java.util.List;

@Data
public class Erc20TokenEvent implements Event{
    private List<Erc20Token> erc20TokenList;
}