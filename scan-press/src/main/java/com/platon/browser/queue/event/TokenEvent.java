package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TokenEvent implements Event {

    private List<Token> tokenList = new ArrayList<>();

    private List<ErcTx> erc20TxList = new ArrayList<>();

    private List<ErcTx> erc721TxList = new ArrayList<>();

}
