package com.platon.browser.common.service.elasticsearch;

import java.io.IOException;
import java.util.Set;

public interface EsService<T> {
    void save(Set<T> data) throws IOException;
}
