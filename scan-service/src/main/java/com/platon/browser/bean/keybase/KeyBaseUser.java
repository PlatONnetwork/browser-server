
package com.platon.browser.bean.keybase;

import java.util.List;

public class KeyBaseUser {
    private Status status;
    private List<Completion> completions;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public List<Completion> getCompletions() {
		return completions;
	}
	public void setCompletions(List<Completion> completions) {
		this.completions = completions;
	}
    
}
