package com.platon.browser.client;

import lombok.Data;

/**
 * @Auther: dongqile
 * @Date: 2019/9/6
 * @Description:
 */
@Data
public class AccuVerifiersCount {
    private Integer accuVerifiers = 0 ;
    private Integer yeas = 0 ;
    private Integer nays = 0 ;
    private Integer abstentions = 0 ;

    public void init(String accuVerifiers,String yeas,String nays,String abstentions){
        this.setAccuVerifiers(Integer.valueOf(accuVerifiers));
        this.setYeas(Integer.valueOf(yeas));
        this.setNays(Integer.valueOf(nays));
        this.setAbstentions(Integer.valueOf(abstentions));
    }
}