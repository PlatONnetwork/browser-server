package com.platon.browser.request.internaltransfer;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class QueryByAddressRequest extends PageReq{
    @NotBlank(message = "{address not null}")
    @Size(min = 42,max = 42)
    private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.toLowerCase();
	}
    
}