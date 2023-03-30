package com.platon.browser.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GzipUtilsTest {

    @Test
    public void testCompress() {
        String text = "{\"delegateCost\":[{\"period\":739,\"value\":5143800000000000000000000},{\"period\":738,\"value\":5143800000000000000000000},{\"period\":737,\"value\":5143800000000000000000000},{\"period\":736,\"value\":5143800000000000000000000},{\"period\":735,\"value\":5143800000000000000000000}],\"delegateProfit\":[{\"period\":738,\"value\":137379424730762690283187},{\"period\":737,\"value\":137046835568243902618935},{\"period\":736,\"value\":136714246405725114954683},{\"period\":735,\"value\":136381657243206327290431},{\"period\":734,\"value\":136128746771588941676839}],\"slash\":[],\"stakeCost\":[{\"period\":739,\"value\":119800000000000000000000},{\"period\":738,\"value\":119800000000000000000000},{\"period\":737,\"value\":119800000000000000000000},{\"period\":736,\"value\":119800000000000000000000},{\"period\":735,\"value\":119800000000000000000000}],\"stakeProfit\":[{\"period\":738,\"value\":15264434328088298926202},{\"period\":737,\"value\":15227479976697322519039},{\"period\":736,\"value\":15190525625306346111876},{\"period\":735,\"value\":15153570853915369704713},{\"period\":734,\"value\":15125469690402326858750}]}";
        text = "{\"nodeSettleStatisQueue\":[{\"settleEpochRound\":739,\"blockNumGrandTotal\":0,\"blockNumElected\":0},{\"settleEpochRound\":738,\"blockNumGrandTotal\":30,\"blockNumElected\":3},{\"settleEpochRound\":737,\"blockNumGrandTotal\":30,\"blockNumElected\":3},{\"settleEpochRound\":736,\"blockNumGrandTotal\":30,\"blockNumElected\":3},{\"settleEpochRound\":735,\"blockNumGrandTotal\":10,\"blockNumElected\":2},{\"settleEpochRound\":734,\"blockNumGrandTotal\":50,\"blockNumElected\":4},{\"settleEpochRound\":733,\"blockNumGrandTotal\":30,\"blockNumElected\":3},{\"settleEpochRound\":732,\"blockNumGrandTotal\":30,\"blockNumElected\":3},{\"settleEpochRound\":731,\"blockNumGrandTotal\":0,\"blockNumElected\":0}],\"blockNum\":7927620,\"nodeId\":\"0xff094efcd6c63d66584f24e0cb129a466ae4cbc6eacd9a060c367e9129f1480944e3ffa4d9760a713559cd99cee372a0e1e66ed21955cebec185acb4371b1d15\"}";
        String compressed = GzipUtils.compress(text);
        log.debug("text.length:{}, compressed.length:{}", text.length(), compressed.length());
    }
}
