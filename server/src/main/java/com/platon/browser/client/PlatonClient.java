package com.platon.browser.client;

import com.platon.browser.config.ChainsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class PlatonClient {
    @Autowired
    private ChainsConfig chainsConfig;

    public static class ContractHolder {
        public ContractHolder(Web3j web3j,TicketContract tc,CandidateContract cc){
            this.web3j=web3j;
            this.ticketContract=tc;
            this.candidateContract=cc;
        }
        Web3j web3j;
        TicketContract ticketContract;
        CandidateContract candidateContract;
    }

    private Map<String,ContractHolder> map = new HashMap<>();

    @PostConstruct
    private void init () {
        chainsConfig.getChainIds().forEach(chainId->{
            Web3j web3j = chainsConfig.getWeb3j(chainId);
            TicketContract tc = TicketContract.load(web3j,new ReadonlyTransactionManager(web3j,TicketContract.CONTRACT_ADDRESS),new DefaultWasmGasProvider());
            CandidateContract cc = CandidateContract.load(web3j,new ReadonlyTransactionManager(web3j,CandidateContract.CONTRACT_ADDRESS), new DefaultWasmGasProvider());
            ContractHolder ch = new ContractHolder(web3j,tc,cc);
            map.put(chainId,ch);
        });
    }

    public CandidateContract getCandidateContract(String chainId) {
        ContractHolder ch = map.get(chainId);
        if(ch==null) return null;
        return ch.candidateContract;
    }
    public TicketContract getTicketContract(String chainId) {
        ContractHolder ch = map.get(chainId);
        if(ch==null) return null;
        return ch.ticketContract;
    }
    public Web3j getWeb3j (String chainId) {
        ContractHolder ch = map.get(chainId);
        if(ch==null) return null;
        return ch.web3j;
    }
}
