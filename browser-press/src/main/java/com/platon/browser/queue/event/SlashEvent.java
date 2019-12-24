package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.entity.Vote;
import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class SlashEvent {
    private List <Slash> slashList;
}