package com.platon.browser.dto.node;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.exception.UnknownLocationException;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Data
public class NodeDetail {
    private Long id;
    private String address;
    private String name;
    private String logo;
    private Integer electionStatus;
    private String location;
    private Long joinTime;
    private String deposit;
    private Double rewardRatio;
    private Integer ranking;
    private String profitAmount;
    private Integer verifyCount;
    private Long blockCount;
    private Double avgBlockTime;
    private String rewardAmount;
    private String nodeUrl;
    private String publicKey;
    private String wallet;
    private String intro;
    private String orgName;
    private String orgWebsite;

    public void init(NodeRanking initData) throws UnknownLocationException {
        BeanUtils.copyProperties(initData,this);
        this.setJoinTime(initData.getJoinTime().getTime());
        this.setNodeUrl("http://"+initData.getIp()+":"+initData.getPort());
        // 公钥就是节点ID
        this.setPublicKey(initData.getNodeId());
        // 钱包就是address
        this.setWallet(initData.getAddress());

        // 设置地理位置信息
        try {
            CityResponse response = GeoUtil.getResponse(initData.getIp());
            Country country = response.getCountry();
            if(StringUtils.isNotBlank(country.getName())){
                this.setLocation(country.getName());
            }
            City city = response.getCity();
            if(StringUtils.isNotBlank(city.getName())){
                this.setLocation(this.getLocation()+" "+city.getName());
            }
        }catch (Exception e){
            throw new UnknownLocationException(I18nEnum.UNKNOWN_LOCATION.name());
        }
    }
}