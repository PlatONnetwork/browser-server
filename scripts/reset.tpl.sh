PATH=$PATH:../../../tools
echo $PATH
#清除Redis缓存
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:blocks;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:networkStat;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:transactions;
redis-cli -c -h 192.168.16.171 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:transferTx;
redis-cli -c -h 192.168.16.171 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:transferTx;
redis-cli -c -h 192.168.16.171 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:transferTx;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:erc20Tx;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:erc20Tx;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:erc20Tx;
redis-cli -c -h 192.168.16.172 -p 7000 -a platscan DEL browser:_VERSION_:_PROFILE_:erc721Tx;
redis-cli -c -h 192.168.16.172 -p 7001 -a platscan DEL browser:_VERSION_:_PROFILE_:erc721Tx;
redis-cli -c -h 192.168.16.172 -p 7002 -a platscan DEL browser:_VERSION_:_PROFILE_:erc721Tx;

#清除ES索引
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__block
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__transaction
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__delegation
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__nodeopt
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__delegation_reward
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__transfer_tx
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__erc20_tx
curl -X DELETE http://192.168.120.103:9200/browser__VERSION___PROFILE__erc721_tx

echo 'use `alaya_browser__VERSION_`;truncate table `address`;truncate table `tx_bak`;truncate table `delegation`;truncate table `network_stat`;truncate table `node`;truncate table `n_opt_bak`;truncate table `proposal`;truncate table `rp_plan`;truncate table `slash`;truncate table `staking`;truncate table `staking_history`;truncate table `vote`;truncate table `config`;truncate table `gas_estimate_log`;truncate table `gas_estimate`;truncate table `erc20_token`;truncate table `erc20_token_address_rel`;truncate table `erc20_token_detail`; truncate table `erc20_token_transfer_record`;truncate table `block_node`;truncate table `token`;truncate table `token_expand`;truncate table `token_holder`;truncate table `token_inventory`;' | mysql -uplaton -h192.168.9.191 -pplaton
