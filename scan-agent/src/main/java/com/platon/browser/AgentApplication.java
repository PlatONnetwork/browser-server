package com.platon.browser;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.ES721Bean;
import com.platon.browser.bean.ESBean;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@EnableRetry
@Configuration
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser", "com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper", "com.platon.browser.v0150.dao", "com.platon.browser.v0151.dao"})
public class AgentApplication implements ApplicationRunner {

    @Resource
    private EsTransactionRepository esTransactionRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        updateTransaction();
        log.info("================修改es索引browser_platon_transaction完成=====================");
        update721Transaction();
        log.info("================修改es索引browser_platon_erc721_tx完成=====================");
    }


    public void updateTransaction() throws Exception {
        HttpRequest request = HttpUtil.createGet("http://192.168.120.103:9200/browser_platon_transaction/_search");
        request.contentType("application/json");
        String body = "{\"query\":{\"term\":{\"to\":\"lat10lwnl9ktmeghx757yj6xrelf9gzhcwaly3q36j\"}},\"from\":0,\"size\":6000,\"_source\":[\"id\",\"hash\",\"bHash\",\"num\",\"index\",\"time\",\"nonce\",\"status\",\"gasPrice\",\"gasUsed\",\"gasLimit\",\"from\",\"to\",\"value\",\"type\",\"cost\",\"toType\",\"seq\",\"creTime\",\"updTime\",\"input\",\"info\",\"erc721TxInfo\",\"erc20TxInfo\",\"transferTxInfo\",\"pposTxInfo\",\"failReason\",\"contractType\",\"method\",\"bin\",\"contractAddress\"]}";
        request.body(body);
        request.basicAuth("elastic", "changeme");
        HttpResponse response = request.execute();
        String res = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(res);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        List<ESBean> esBeanList = jsonArray.toList(ESBean.class);
        int updateCount = 0;
        for (ESBean esBean : esBeanList) {
            List<ErcTx> ercTxList = JSONUtil.toList(esBean.get_source().getErc721TxInfo(), ErcTx.class);
            for (ErcTx ercTx : ercTxList) {
                ercTx.setName("HashKey DID");
                ercTx.setSymbol("HKDID");
            }
            esBean.get_source().setErc721TxInfo(JSON.toJSONString(ercTxList));
            esTransactionRepository.update(esBean.get_id(), esBean.get_source());
            updateCount++;
            Thread.sleep(200);
            log.info("[{}]该id[{}]修改成功", updateCount, esBean.get_id());
        }
    }

    public void update721Transaction() throws Exception {
        HttpRequest request = HttpUtil.createGet("http://192.168.120.103:9200/browser_platon_erc721_tx/_search");
        request.contentType("application/json");
        String body = "{\"query\":{\"term\":{\"contract\":\"lat10lwnl9ktmeghx757yj6xrelf9gzhcwaly3q36j\"}},\"from\":0,\"size\":6000,\"_source\":[\"seq\",\"name\",\"symbol\",\"decimal\",\"contract\",\"hash\",\"from\",\"to\",\"value\",\"bn\",\"bTime\",\"toType\",\"fromType\",\"remark\",\"txFee\"]}";
        request.body(body);
        request.basicAuth("elastic", "changeme");
        HttpResponse response = request.execute();
        String res = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(res);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        List<ES721Bean> esBeanList = jsonArray.toList(ES721Bean.class);
        int updateCount = 0;
        for (ES721Bean esBean : esBeanList) {
            esBean.get_source().setName("HashKey DID");
            esBean.get_source().setSymbol("HKDID");
            esErc721TxRepository.update(esBean.get_id(), esBean.get_source());
            updateCount++;
            Thread.sleep(200);
            log.info("[{}]该id[{}]修改成功", updateCount, esBean.get_id());
        }
    }


}
