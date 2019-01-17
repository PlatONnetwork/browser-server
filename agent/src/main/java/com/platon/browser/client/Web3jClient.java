package com.platon.browser.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class Web3jClient {

    TicketContract ticketContract;

    CandidateContract candidateContract;

    @Value("${node.ip}")
    private String nodeIp;

    private static Web3j web3j;

    private Web3jClient () {
    }

    @PostConstruct
    private void init () {
        web3j = Web3j.build(new HttpService(nodeIp));
    }

    private Credentials loadCredentials() throws IOException, CipherException {
        InputStream in = Web3jClient.class.getClassLoader().getResource("wallet.json").openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        File file = new File(System.getProperty("user.dir")+"/tmp.txt");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        String line;
        while ((line = br.readLine())!=null){
            bw.write(line);
        }
        bw.flush();
        bw.close();
        br.close();
        Credentials credentials = WalletUtils.loadCredentials("88888888", file);
        return credentials;
    }

    public static Web3j getWeb3jClient () {
        return web3j;
    }

    public TicketContract getTicketContract () {
        Web3j web3j = Web3jClient.getWeb3jClient();
        if (ticketContract == null) {
            try {
                ticketContract = TicketContract.load(web3j,loadCredentials(), new DefaultWasmGasProvider());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
        }
        return ticketContract;
    }


    public CandidateContract getCandidateContract () {
        Web3j web3j = Web3jClient.getWeb3jClient();
        if (candidateContract == null) {
            try {
                candidateContract = candidateContract.load(web3j,loadCredentials(), new DefaultWasmGasProvider());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
        }
        return candidateContract;
    }
}
