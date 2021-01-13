package com.platon.browser.service.elasticsearch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * ERC20交易记录ES操作类
 */
@Repository
@Slf4j
public class EsErc20TxRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getErc20TxIndexName();
    }
    @Getter
    public String defaultIndexTemplateName = "browser_erc20_tx_template";
    @PostConstruct
    public void init() {
        try {
            // Self-test index template.
            this.putIndexTemplate(this.defaultIndexTemplateName, this.defaultIndexTemplate());
        } catch (IOException e) {
            log.error("Automatic detection of internal transaction index template failed.", e);
            throw new RuntimeException(e);
        }
    }

    public XContentBuilder defaultIndexTemplate() throws IOException {
        XContentBuilder indexPatterns = XContentFactory.jsonBuilder()
                .startObject()
                    .array("index_patterns", "browser_erc20_tx_*")
                    .startObject("settings")
                        .field("number_of_shards", 5)
                        .field("number_of_replicas", 1)
                        .field("max_result_window", 2000000000)
                    .endObject()
                    .startObject("mappings")
                        .startObject("properties")
                            .startObject("seq").field("type", "long").endObject()
                            .startObject("hash").field("type", "keyword").endObject()
                            .startObject("bn").field("type", "long").endObject()
                            .startObject("from").field("type", "keyword").endObject()
                            .startObject("contract").field("type", "keyword").endObject()
                            .startObject("tto").field("type", "keyword").endObject()
                            .startObject("tValue").field("type", "keyword").endObject()
                            .startObject("name").field("type", "text").endObject()
                            .startObject("symbol").field("type", "keyword").endObject()
                            .startObject("fromType").field("type", "integer").endObject()
                            .startObject("toType").field("type", "integer").endObject()
                            .startObject("txFee").field("type", "keyword").endObject()
                            .startObject("bTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject()
                            .startObject("value").field("type", "keyword").endObject()
                            .startObject("info").field("type", "text").endObject()
                            .startObject("ctime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject()
                        .endObject()
                    .endObject()
                .endObject();
        return indexPatterns;
    }
}
