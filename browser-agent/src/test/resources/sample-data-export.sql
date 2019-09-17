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
        join `transaction` tx
             on tx.`block_number` = bl.`number`
        join staking st
             on st.`staking_block_num` = bl.`number`
        join delegation de
             on de.`staking_block_num` = st.`staking_block_num`
        join node nd
             on nd.`node_id` = bl.`node_id`
        join un_delegation ud
             on ud.`staking_block_num` = st.`staking_block_num`;

select DISTINCT
    tx.*
from
    block bl
        join `transaction` tx
             on tx.`block_number` = bl.`number`
        join staking st
             on st.`staking_block_num` = bl.`number`
        join delegation de
             on de.`staking_block_num` = st.`staking_block_num`
        JOIN node nd
             ON nd.`node_id` = bl.`node_id`
        JOIN un_delegation ud
             ON ud.`staking_block_num` = st.`staking_block_num`;

select DISTINCT
    st.*
from
    block bl
        join `transaction` tx
             on tx.`block_number` = bl.`number`
        join staking st
             on st.`staking_block_num` = bl.`number`
        join delegation de
             on de.`staking_block_num` = st.`staking_block_num`
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
