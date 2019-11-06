package com.platon.browser.common.service.redis;

import java.io.IOException;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: Redis服务
 */
public interface RedisService<T> {
    void save(Set<T> data) throws IOException;
    void clear();
}
