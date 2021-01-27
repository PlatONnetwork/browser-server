#!/bin/bash
PATH=$PATH:../../../tools
echo "$PATH"
VERSION=_VERSION_
PROFILE=_PROFILE_
REDIS_HOST1=192.168.16.171
REDIS_HOST2=192.168.16.172
#清除Redis缓存
keys=(
"browser:${VERSION}:${PROFILE}:blocks"
"browser:${VERSION}:${PROFILE}:networkStat"
"browser:${VERSION}:${PROFILE}:transactions"
"browser:${VERSION}:${PROFILE}:transferTx"
"browser:${VERSION}:${PROFILE}:erc20Tx"
"browser:${VERSION}:${PROFILE}:erc721Tx"
)
for key in "${keys[@]}"
do
  redis-cli -c -h $REDIS_HOST1 -p 7000 -a platscan DEL "${key}";
  redis-cli -c -h $REDIS_HOST1 -p 7001 -a platscan DEL "${key}";
  redis-cli -c -h $REDIS_HOST1 -p 7002 -a platscan DEL "${key}";
  redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL "${key}";
  redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL "${key}";
  redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL "${key}";
done

ES_HOST=192.168.120.103
ES_PORT=9200
#清除ES索引
indexes=(
  "browser_${VERSION}_${PROFILE}_block"
  "browser_${VERSION}_${PROFILE}_transaction"
  "browser_${VERSION}_${PROFILE}_delegation"
  "browser_${VERSION}_${PROFILE}_nodeopt"
  "browser_${VERSION}_${PROFILE}_delegation_reward"
  "browser_${VERSION}_${PROFILE}_transfer_tx"
  "browser_${VERSION}_${PROFILE}_erc20_tx"
  "browser_${VERSION}_${PROFILE}_erc721_tx"
)
for index in "${indexes[@]}"
do
 curl -X DELETE http://${ES_HOST}:${ES_PORT}/"${index}"
done

tables=(
  'address'
  'tx_bak'
  'delegation'
  'network_stat'
  'node'
  'n_opt_bak'
  'proposal'
  'rp_plan'
  'slash'
  'staking'
  'staking_history'
  'vote'
  'config'
  'gas_estimate_log'
  'gas_estimate'
  'block_node'
  'token'
  'token_expand'
  'token_holder'
  'token_inventory'
)
truncateSql="USE alaya_browser_${VERSION};"
for table in "${tables[@]}"
do
 truncateSql="${truncateSql}TRUNCATE TABLE \`${table}\`;"
done
echo "${truncateSql}"
echo "$truncateSql" | mysql -u_DB_USER_ -h_DB_HOST_ -p_DB_PASS_
