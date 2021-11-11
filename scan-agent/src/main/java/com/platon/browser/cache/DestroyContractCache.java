package com.platon.browser.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 销毁的合约缓存
 *
 * @date: 2021/10/14
 */
@Data
@Slf4j
@Component
public class DestroyContractCache {

    private Set<String> destroyContracts = new HashSet<>();

}
