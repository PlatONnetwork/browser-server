#清除Redis缓存
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.13.5.0:local:blocks;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.13.5.0:local:networkStat;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:0.13.5.0:local:transactions;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:0.13.5.0:local:transactions;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:0.13.5.0:local:transactions;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:0.13.5.0:local:transactions;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:0.13.5.0:local:transactions;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:0.13.5.0:local:transactions;

#清除ES索引
curl -X DELETE http://192.168.120.103:9200/browser_block_local
curl -X DELETE http://192.168.120.103:9200/browser_transaction_local
curl -X DELETE http://192.168.120.103:9200/browser_delegation_local
curl -X DELETE http://192.168.120.103:9200/browser_nodeopt_local
curl -X DELETE http://192.168.120.103:9200/browser_delegation_reward_local
curl -X DELETE http://192.168.120.103:9200/browser_inner_tx_local

#清除MYSQL
echo "clean db begin"
echo 'use `browser_0.13.5.0_test`;truncate table `address`;truncate table `tx_bak`;truncate table `delegation`;truncate table `network_stat`;truncate table `node`;truncate table `n_opt_bak`;truncate table `proposal`;truncate table `rp_plan`;truncate table `slash`;truncate table `staking`;truncate table `staking_history`;truncate table `vote`;truncate table `config`;truncate table `gas_estimate_log`;truncate table `gas_estimate`;truncate table `erc20_token`;truncate table `erc20_token_address_rel`;truncate table `erc20_token_detail`; truncate table `erc20_token_transfer_record`;truncate table `block_node`; ' | mysql -uroot -h192.168.21.73 -proot
echo "clean db end"
