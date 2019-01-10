package com.platon.browser.dto.node;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.exception.UnknownLocationException;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import jnr.ffi.annotations.In;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.web3j.abi.datatypes.Int;

@Data
public class NodeListItem {
    private Long id;
    private String nodeId;
    private Integer ranking;
    private String logo;
    private String name;
    private Integer electionStatus;
    private String countryCode;
    private String location;
    private String deposit;
    private Integer blockCount;
    private Double rewardRatio;
    private String address;
    public void init(NodeRanking initData) throws UnknownLocationException {
        BeanUtils.copyProperties(initData,this);
        try {
            CityResponse response = GeoUtil.getResponse(initData.getIp());
            Country country = response.getCountry();
            this.setCountryCode(country.getIsoCode());
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
