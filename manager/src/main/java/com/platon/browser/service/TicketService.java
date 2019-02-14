package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.req.ticket.TicketListReq;

public interface TicketService {
    RespPage<Ticket> getList(TicketListReq req);
}
