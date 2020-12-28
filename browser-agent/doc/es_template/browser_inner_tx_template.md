## Token 内部交易 ES 索引模板

URL: PUT /_template/browser_inner_tx_template
```
{
    "index_patterns": [
        "browser_inner_tx_*"
    ],
    "settings": {
        "number_of_shards": 5,
        "number_of_replicas": 1,
        "max_result_window": 2000000000
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "long"
            },
            "txHash": {
                "type": "keyword"
            },
            "blockNumber": {
                "type": "long"
            },
            "contract": {
                "type": "keyword"
            },
            "transferTo": {
                "type": "keyword"
            },
            "transferValue": {
                "type": "text"
            },
            "decimal": {
                "type": "integer"
            },
            "symbol": {
                "type": "keyword"
            },
            "methodSign": {
                "type": "keyword"
            },
            "result": {
                "type": "integer"
            },
            "blockTimestamp": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            },
            "value": {
                "type": "text",
                "index": false,
                "doc_values": false,
                "norms": false
            },
            "createTime": {
                "type": "date",
                "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
            }
        }
    }
}
```