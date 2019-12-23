
package com.platon.browser.service;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Data
@Service
public class StakeGenService {

    @Value("${platon.stakingSumQty}")
    private String stakingSumQty;

    public List<Staking> buildStakeInfo ( List <Node> nodeList, Long nowQty ) {
        List<Staking> stakings  = new ArrayList <>();
        for(Node node : nodeList){
            if (Long.parseLong(stakingSumQty) >= nowQty) {
                Staking staking = new Staking();
                BeanUtils.copyProperties(node,staking);
                stakings.add(staking);
            }
        }
        return stakings;
    }
}