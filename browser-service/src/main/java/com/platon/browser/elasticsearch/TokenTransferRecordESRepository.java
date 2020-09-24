package com.platon.browser.elasticsearch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 针对处理合约内部转账记录的ES处理器
 */
@Repository
@Slf4j
public class TokenTransferRecordESRepository extends ESRepository {

    @Value("${spring.elasticsearch.high-level-client.innerTxIndexName}")
    private String indexName;

    @Getter
    public String defaultIndexTemplateName = "browser_inner_tx_template";

    @PostConstruct
    public void init() {
        try {
            // Self-test index template.
            putIndexTemplate(defaultIndexTemplateName, defaultIndexTemplate());
        } catch (IOException e) {
            log.error("Automatic detection of internal transaction index template failed.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    public XContentBuilder defaultIndexTemplate() throws IOException {
        XContentBuilder indexPatterns = XContentFactory.jsonBuilder()
                .startObject()
                    .array("index_patterns", "browser_inner_tx_*")
                    .startObject("settings")
                        .field("number_of_shards", 5)
                        .field("number_of_replicas", 1)
                        .field("max_result_window", 2000000000)
                    .endObject()
                    .startObject("mappings")
                        .startObject("properties")
                            .startObject("id").field("type", "long").endObject()
                            .startObject("txHash").field("type", "keyword").endObject()
                            .startObject("blockNumber").field("type", "long").endObject()
                            .startObject("contract").field("type", "keyword").endObject()
                            .startObject("transferTo").field("type", "keyword").endObject()
                            .startObject("transferValue").field("type", "text").endObject()
                            .startObject("decimal").field("type", "integer").endObject()
                            .startObject("symbol").field("type", "keyword").endObject()
                            .startObject("methodSign").field("type", "keyword").endObject()
                            .startObject("result").field("type", "integer").endObject()
                            .startObject("blockTimestamp").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject()
                            .startObject("value").field("type", "text").endObject()
                            .startObject("createTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject()
                        .endObject()
                    .endObject()
                .endObject();
        return indexPatterns;
    }

    public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

}
