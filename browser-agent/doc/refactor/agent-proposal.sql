-- 提交文本提案（2000）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';  -- 提交提案的验证人
set @pIDID = '100';
set @url = 'https://github.com/danielgogo/PIPs/PIP-{pip_id}.md';   -- pip_id由@pIDID替换
set @pip_num = 'PIP-{pip_id}';                     -- pip_id由@pIDID替换
set @end_voting_block = '2000';                  -- 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + 提案入参轮数 * 共识周期块数 - 20
set @topic='inquiry';                            -- 占位，后续任务补充
set @description='inquiry';                      -- 占位，后续任务补充
set @opt_desc='ID|TITLE|TYPE|VERSION';           -- ID=@pIDID;TITLE=@topic;TYPE=1;VERSION=''
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存）
set @stakingName = "";  -- 质押名称

-- 1. proposal 新建
insert into `proposal` 
	(`hash`, 
	`type`, 
	`verifier`, 
	`verifier_name`, 
	`url`, 
	`end_voting_block`, 
	`timestamp`, 
	`pip_num`, 
	`pip_id`, 
	`topic`, 
	`description`, 
	`block_number`
	)
	values
	(@tx_hash,
	'1', 
	`node_id`, 
	@stakingName,
	@url, 
	@end_voting_block, 
	@tx_timestamp, 
	@pip_num, 
	@pIDID, 
	@topic, 
	@description, 
	@block_number
	);

-- 2. node_opt 创建
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(@node_id, 
	'4', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp,
	@opt_desc
	);
	
	
-- 提交升级提案（2001）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';  -- 提交提案的验证人
set @pIDID = '100';
set @url = 'https://github.com/danielgogo/PIPs/PIP-{pip_id}.md';   -- pip_id由@pIDID替换
set @pip_num = 'PIP-{pip_id}';                     -- pip_id由@pIDID替换
set @end_voting_block = '2000';                  -- 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + 提案入参轮数 * 共识周期块数 - 20
set @active_block = '3000';                      -- 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + (提案入参轮数+设置预升级开始轮数) * 共识周期块数  + 1
set @topic='inquiry';                            -- 占位，后续任务补充
set @description='inquiry';                      -- 占位，后续任务补充
set @opt_desc='ID|TITLE|TYPE|VERSION';           -- ID=@pIDID; TITLE=@topic; TYPE=1;VERSION=''
set @newVersion='2000';						     -- 新的版本
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存）
set @stakingName = "";  -- 质押名称

-- 1. proposal 新建
insert into `proposal` 
	(`hash`, 
	`type`, 
	`verifier`, 
	`verifier_name`, 
	`url`,
	`new_version`, 
	`end_voting_block`, 
	`active_block`,
	`timestamp`, 
	`pip_num`, 
	`pip_id`, 
	`topic`, 
	`description`, 
	`block_number`
	)
	values
	(@tx_hash, 
	'2', 
	`node_id`, 
	@stakingName,
	@url, 
	@newVersion,
	@end_voting_block, 
	@active_block,
	@tx_timestamp, 
	@pip_num, 
	@pIDID, 
	@topic, 
	@description, 
	@block_number
	);
	
-- 2. node_opt 创建
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(@node_id, 
	'4', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp,
	@opt_desc
	);

	
-- 提交取消提案（2005）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';  -- 提交提案的验证人
set @pIDID = '100';
set @url = 'https://github.com/danielgogo/PIPs/PIP-{pip_id}.md';   -- pip_id由@pIDID替换
set @pip_num = 'PIP-{pip_id}';                     -- pip_id由@pIDID替换
set @end_voting_block = '2000';                  -- 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + 提案入参轮数 * 共识周期块数 - 20
set @topic='inquiry';                            -- 占位，后续任务补充
set @description='inquiry';                      -- 占位，后续任务补充
set @opt_desc='ID|TITLE|TYPE|VERSION';           -- ID=@pIDID; TITLE=@topic; TYPE=1;VERSION='''
set @canceled_id='0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';  -- 被取消提案id
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存）
set @stakingName = "";  -- 质押名称

-- 1. proposal 新建
insert into `proposal` 
	(`hash`, 
	`type`, 
	`verifier`, 
	`verifier_name`, 
	`url`, 
	`end_voting_block`, 
	`timestamp`, 
	`pip_num`, 
	`pip_id`, 
	`topic`, 
	`description`, 
	`canceled_pip_id`,
	`block_number`
	)
	values
	(@tx_hash, 
	'4', 
	`node_id`, 
	@stakingName, 
	@url, 
	@end_voting_block, 
	@tx_timestamp, 
	@pip_num, 
	@pIDID, 
	@topic, 
	@description, 
	@canceled_id,
	@block_number
	);

-- 2. node_opt 创建
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(@node_id, 
	'4', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp,
	@opt_desc
	);
	
	
-- 给提案投票（2003）
-- 入参（input中参数）
set @node_id = '0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18';  -- 提交提案的验证人
set @opt_desc='ID|TITLE|OPTION|TYPE|VERSION';           -- ID=@pIDID; TITLE=@topic;TYPE=1;VERSION='''
set @proposal_hash='0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';  -- 投票提案id
set @vote_option='1';         -- 投票选项
-- 入参（交易中参数）
set @tx_hash = '0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7';
set @block_number = '300';
set @tx_timestamp = '2019-10-13 07:31:20';
-- 入参（进程缓存）
set @stakingName = "";  -- 质押名称

-- 1. vote 创建
insert into `vote` 
	(`hash`, 
	`verifier_name`, 
	`verifier`, 
	`option`, 
	`proposal_hash`, 
	`timestamp`
	)
	values
	(@tx_hash,
	@stakingName,
	@node_id, 
	@vote_option, 
	@proposal_hash, 
	@tx_timestamp
	);

-- 2. node_opt 创建
insert into `node_opt` 
	(`node_id`, 
	`type`, 
	`tx_hash`, 
	`block_number`, 
	`timestamp`,
	`desc`
	)
	values
	(@node_id,
	'5', 
	@tx_hash, 
	@block_number, 
	@tx_timestamp,
	@opt_desc
	);