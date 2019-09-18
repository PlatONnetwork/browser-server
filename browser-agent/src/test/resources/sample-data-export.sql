SELECT DISTINCT
    nd.*
FROM
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number` and st.`node_id`=bl.`node_id`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num` and de.`node_id`=st.`node_id`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num` and st.`node_id`=ud.`node_id`;



select DISTINCT
    bl.*
from
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;


select DISTINCT
    tx.*
from
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;


select DISTINCT
    st.*
from
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;


SELECT DISTINCT
    de.*
FROM
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;


SELECT DISTINCT
    ud.*
FROM
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN staking st
             ON st.`staking_block_num` = bl.`number`
        JOIN delegation de
             ON de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;

SELECT DISTINCT
    p.*
FROM
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN `proposal` p
             ON p.`hash` = tx.`hash`;

SELECT DISTINCT
    ad.*
FROM
    block bl
        JOIN `transaction` tx
             ON tx.`block_number` = bl.`number`
        JOIN `address` ad
             ON ad.`address` = tx.`from` OR ad.`address` = tx.`to`;