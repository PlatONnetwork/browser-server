PATH=$PATH:../../../tools
echo $PATH
REDIS_HOST1=192.168.16.171
REDIS_HOST2=192.168.16.172
VERSION=_VERSION_
PROFILE=_PROFILE_
#清除Redis缓存
redis-cli -c -h $REDIS_HOST1 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST1 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST1 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:blocks;
redis-cli -c -h $REDIS_HOST1 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST1 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST1 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:networkStat;
redis-cli -c -h $REDIS_HOST1 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST1 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST1 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:transactions;
redis-cli -c -h $REDIS_HOST1 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:transferTx;
redis-cli -c -h $REDIS_HOST1 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:transferTx;
redis-cli -c -h $REDIS_HOST1 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:transferTx;
redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:erc20Tx;
redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:erc20Tx;
redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:erc20Tx;
redis-cli -c -h $REDIS_HOST2 -p 7000 -a platscan DEL browser:${VERSION}:${PROFILE}:erc721Tx;
redis-cli -c -h $REDIS_HOST2 -p 7001 -a platscan DEL browser:${VERSION}:${PROFILE}:erc721Tx;
redis-cli -c -h $REDIS_HOST2 -p 7002 -a platscan DEL browser:${VERSION}:${PROFILE}:erc721Tx;

ES_HOST=192.168.120.103
ES_PORT=9200
#清除ES索引
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_block
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_transaction
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_delegation
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_nodeopt
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_delegation_reward
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_transfer_tx
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_erc20_tx
curl -X DELETE http://${ES_HOST}:${ES_PORT}/browser_${VERSION}_${PROFILE}_erc721_tx

DB_HOST=_DB_HOST_
DB_USER=_DB_USER_
DB_PASS=_DB_PASS_
echo 'use alaya_browser_'${VERSION}';
truncate table `address`;\
truncate table `tx_bak`;\
truncate table `delegation`;\
truncate table `network_stat`;\
truncate table `node`;\
truncate table `n_opt_bak`;\
truncate table `proposal`;\
truncate table `rp_plan`;\
truncate table `slash`;\
truncate table `staking`;\
truncate table `staking_history`;\
truncate table `vote`;\
truncate table `config`;\
truncate table `gas_estimate_log`;\
truncate table `gas_estimate`;\
truncate table `block_node`;\
truncate table `token`;\
truncate table `token_expand`;\
truncate table `token_holder`;\
truncate table `token_inventory`;' | mysql -u${DB_USER} -h${DB_HOST} -p${DB_PASS}
