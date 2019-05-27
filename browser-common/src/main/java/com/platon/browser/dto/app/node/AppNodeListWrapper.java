package com.platon.browser.dto.app.node;

import lombok.Data;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 17:17
 * @Description:
 */
@Data
public class AppNodeListWrapper {
    private long voteCount;
    private long totalCount;
    private String ticketPrice;
    private List<AppNodeDto> list;
}
