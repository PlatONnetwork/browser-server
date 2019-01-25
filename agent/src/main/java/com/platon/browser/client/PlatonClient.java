package com.platon.browser.client;

import com.platon.browser.config.ChainsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import javax.annotation.PostConstruct;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class PlatonClient {
    private TicketContract ticketContract;
    private CandidateContract candidateContract;
    @Value("${chain.id}")
    private String chainId;
    @Autowired
    private ChainsConfig chainsConfig;

    @PostConstruct
    private void init () {
        Web3j web3j = chainsConfig.getWeb3j(chainId);
        ticketContract = TicketContract.load(web3j,new ReadonlyTransactionManager(web3j,TicketContract.CONTRACT_ADDRESS),new DefaultWasmGasProvider());
        candidateContract = candidateContract.load(web3j,new ReadonlyTransactionManager(web3j,CandidateContract.CONTRACT_ADDRESS), new DefaultWasmGasProvider());
    }

    public CandidateContract getCandidateContract () {
        return candidateContract;
    }
    public TicketContract getTicketContract () {
        return ticketContract;
    }
    public Web3j getWeb3j () {
        return chainsConfig.getWeb3j(chainId);
    }
    public String getChainId(){return chainId;}
}
