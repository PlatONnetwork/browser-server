package com.platon.browser.utils;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.platon.rlp.wasm.datatypes.Int;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ConvertUtilTest {

    @Test
    public void test() {
        BigInteger bigint = ConvertUtil.hexToBigInteger("1869F");
        assertTrue(bigint.intValue() == 99999);
    }

    @Test
    public void convertByFactorTest() {
        Integer decimal = 16;
        BigDecimal totalSupply = ConvertUtil.convertByFactor(new BigDecimal("500000000000000000000000000"), decimal);

        BigDecimal balance = ConvertUtil.convertByFactor(new BigDecimal("499989999999999999999999900"), decimal);
        log.info("1========{}", balance);
        String res = balance.divide(totalSupply, 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(6, RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString();
        log.info("1========{}", res);

        balance = ConvertUtil.convertByFactor(new BigDecimal("10000000000000000000000"), decimal);
        log.info("2========{}", balance);
        res = balance.divide(totalSupply, 6, RoundingMode.HALF_UP)
                     .multiply(BigDecimal.valueOf(100))
                     .setScale(6, RoundingMode.HALF_UP)
                     .stripTrailingZeros()
                     .toPlainString();
        log.info("2========{}", res);

        balance = ConvertUtil.convertByFactor(new BigDecimal("100"), decimal);
        log.info("3========{}", balance);
        res = balance.divide(totalSupply, 6, RoundingMode.HALF_UP)
                     .multiply(BigDecimal.valueOf(100))
                     .setScale(6, RoundingMode.HALF_UP)
                     .stripTrailingZeros()
                     .toPlainString();
        log.info("3========{}", res);

        log.error("======{}",new BigDecimal(EnergonUtil.format(new BigDecimal("100").divide(BigDecimal.TEN.pow(16))
                                                                    .setScale(12, BigDecimal.ROUND_DOWN), 12)));

    }

}
