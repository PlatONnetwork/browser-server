package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.req.ticket.TicketListReq;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TicketService {
    RespPage<Ticket> getList(TicketListReq req);
    Map<String,BigDecimal> getTicketIncome(String chainId, List<String> ticketIds);
    Map<String,BigDecimal> getTicketIncome(String chainId, String txHash);
}
