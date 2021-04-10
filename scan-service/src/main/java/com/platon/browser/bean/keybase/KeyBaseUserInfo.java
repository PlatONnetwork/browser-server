
package com.platon.browser.bean.keybase;

import java.util.List;

public class KeyBaseUserInfo {
    private Status status;
    private List<Them> them;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<Them> getThem() {
		return them;
	}
	public void setThem(List<Them> them) {
		this.them = them;
	}
    
}
