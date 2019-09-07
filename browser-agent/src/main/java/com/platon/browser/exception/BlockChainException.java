package com.platon.browser.exception;

/**
 * 区块链内部异常
 * @Auther: Chendongming
 * @Date: 2019/8/17 16:27
 * @Description:
 */
public class BlockChainException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlockChainException(String msg){
        super(msg);
    }
}
