package com.platon.browser.dto.node;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.IPUtil;
import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.BeanUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 节点信息
 */
@Data
public class NodeInfo {
    private Float longitude;
    private Float latitude;
    private Integer nodeType;
    private Integer netState;

    public void init(NodeRanking initData){
        BeanUtils.copyProperties(initData,this);
        String ip = initData.getIp();
        if(!IPUtil.isIPv4Address(ip)){
            try {
                ip = InetAddress.getByName(ip).getHostAddress();
            } catch (UnknownHostException e) {
                ip = "";
                e.printStackTrace();
            }
        }
        CityResponse response = GeoUtil.getResponse(ip);
        if(response!=null){
            Location location = response.getLocation();
            this.setLongitude(location.getLongitude().floatValue());
            this.setLatitude(location.getLatitude().floatValue());
        }else{
            // 默认设置为深圳的经纬度
            this.setLongitude(114.06667f);
            this.setLatitude(22.61667f);
        }
        this.setNodeType(1);
        this.setNetState(initData.getIsValid());
    }

    public int hashCode() {
        return  new HashCodeBuilder(17,37)
                .append(longitude)
                .append(latitude).toHashCode( );
    }

    public boolean equals(Object obj) {
        NodeInfo nodeInfo = (NodeInfo)obj;
        if(this==obj || (this.latitude==nodeInfo.latitude&&this.longitude==nodeInfo.longitude))
            return true;
        return false;
    }

}
