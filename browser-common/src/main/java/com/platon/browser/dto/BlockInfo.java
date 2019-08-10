package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 14:20
 * @Description:
 */
@Data
public class BlockInfo extends Block {
    private List<TransactionInfo> transactions = new ArrayList<>();
}
