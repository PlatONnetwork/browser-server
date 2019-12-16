package com.platon.browser.client;

/**
 * @Auther: dongqile
 * @Date: 2019/9/6
 * @Description:
 */
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

	public Integer getAccuVerifiers() {
		return accuVerifiers;
	}

	public void setAccuVerifiers(Integer accuVerifiers) {
		this.accuVerifiers = accuVerifiers;
	}

	public Integer getYeas() {
		return yeas;
	}

	public void setYeas(Integer yeas) {
		this.yeas = yeas;
	}

	public Integer getNays() {
		return nays;
	}

	public void setNays(Integer nays) {
		this.nays = nays;
	}

	public Integer getAbstentions() {
		return abstentions;
	}

	public void setAbstentions(Integer abstentions) {
		this.abstentions = abstentions;
	}
    
}