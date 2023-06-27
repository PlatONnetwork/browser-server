package com.platon.browser.dao.custommapper;

public interface CustomStakingHistoryMapper {
    /**
     * 把staking表中，已经退出的质押（status=3)的记录，备份到staking_history表中，然后从staking表中删除
     */
    void backupQuitedStaking();
}
