1. upgrade MySQL database: docs/db/increment/scan-v1.4.0.2-to-v1.5.0.0-false.sql
2. update Elasticsearch index template： estpl/block.estpl.yml
```aidl
PUT _template/browser_platon_block_tpl
index_patterns:
  - "*_block"
settings:
  index:
    max_result_window: '1000000000'
    number_of_shards: '5'
    number_of_replicas: '1'
mappings:
  properties:
    tx_gas_limit:
      norms: false
      index: false
      type: text
      doc_values: false
    reward:
      norms: false
      index: false
      type: text
      doc_values: false
    tx_qty:
      type: short
    num:
      type: long
    gas_used:
      norms: false
      index: false
      type: text
      doc_values: false
    node_name:
      type: text
    s_qty:
      type: short
    tran_qty:
      type: short
    miner:
      norms: false
      index: false
      type: text
      doc_values: false
    p_qty:
      type: short
    tx_fee:
      norms: false
      index: false
      type: text
      doc_values: false
    gas_limit:
      norms: false
      index: false
      type: text
      doc_values: false
    cre_time:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    size:
      norms: false
      index: false
      type: text
      doc_values: false
    d_qty:
      type: short
    p_hash:
      norms: false
      index: false
      type: text
      doc_values: false
    extra:
      norms: false
      index: false
      type: text
      doc_values: false
    baseFeePerGas:
      norms: false
      index: false
      type: text
      doc_values: false
    upd_time:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    time:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    hash:
      type: keyword
    node_id:
      type: keyword

```

3. update Elasticsearch index template： estpl/transaction.estpl.yml
```aidl
PUT _template/browser_platon_transaction_tpl
index_patterns:
  - "*_transaction"
settings:
  index:
    max_result_window: '2000000000'
    number_of_shards: '5'
    number_of_replicas: '1'
mappings:
  properties:
    type:
      type: short
    seq:
      type: long
    bHash:
      type: keyword
    num:
      type: long
    index:
      type: short
    hash:
      type: keyword
    from:
      type: keyword
    to:
      type: keyword
    fromType:
      type: integer
    toType:
      type: integer
    nonce:
      type: long
    gasLimit:
      norms: false
      index: false
      type: text
      doc_values: false
    gasPrice:
      norms: false
      index: false
      type: text
      doc_values: false
    gasUsed:
      norms: false
      index: false
      type: text
      doc_values: false
    cost:
      norms: false
      index: false
      type: text
      doc_values: false
    value:
      type: text
    status:
      type: integer
    time:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    input:
      norms: false
      index: false
      type: text
      doc_values: false
    info:
      norms: false
      index: false
      type: text
      doc_values: false
    erc1155TxInfo:
      norms: false
      index: false
      type: text
      doc_values: false
    erc721TxInfo:
      norms: false
      index: false
      type: text
      doc_values: false
    erc20TxInfo:
      norms: false
      index: false
      type: text
      doc_values: false
    transferTxInfo:
      norms: false
      index: false
      type: text
      doc_values: false
    pposTxInfo:
      norms: false
      index: false
      type: text
      doc_values: false
    failReason:
      norms: false
      index: false
      type: text
      doc_values: false
    creTime:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    updTime:
      format: yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
      type: date
    remark:
      norms: false
      index: false
      type: text
      doc_values: false
    chainId:
      norms: false
      index: false
      type: text
      doc_values: false
    rawEthTxType:
      norms: false
      index: false
      type: integer
      doc_values: false
    maxFeePerGas:
      norms: false
      index: false
      type: text
      doc_values: false
    maxPriorityFeePerGas:
      norms: false
      index: false
      type: text
      doc_values: false
    accessListInfo:
      norms: false
      index: false
      type: text
      doc_values: false

```
 
