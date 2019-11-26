#清除数据库
echo 'use `platon_browser_0.7.4.0_test`;truncate table `address`;truncate table `tx_bak`;truncate table `delegation`;truncate table `network_stat`;truncate table `node`;truncate table `n_opt_bak`;truncate table `proposal`;truncate table `rp_plan`;truncate table `slash`;truncate table `staking`;truncate table `staking_history`;truncate table `vote`;truncate table `config`;' > tmp.sql;
mysql -uroot -pJuzhen123! < tmp.sql;

#清除Redis缓存
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.7.4.0:test:blocks;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.7.4.0:test:networkStat;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.7.4.0:test:transactions;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.7.4.0:test:transactions;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.7.4.0:test:transactions;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.7.4.0:test:transactions;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.7.4.0:test:transactions;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.7.4.0:test:transactions;

#清除ES索引
curl -X DELETE http://192.168.120.103:9200/browser_block_1
curl -X DELETE http://192.168.120.103:9200/browser_transaction_1
curl -X DELETE http://192.168.120.103:9200/browser_delegation_1
curl -X DELETE http://192.168.120.103:9200/browser_nodeopt_1