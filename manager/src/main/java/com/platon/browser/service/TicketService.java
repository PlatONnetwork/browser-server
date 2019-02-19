package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.req.ticket.TicketListReq;

import java.math.BigDecimal;

public interface TicketService {
    RespPage<Ticket> getList(TicketListReq req);

    BigDecimal getTicketIncomeSum(String txHasH,String chainId);

    BigDecimal getTicketIncome(String ticketId,String chainId);
}
