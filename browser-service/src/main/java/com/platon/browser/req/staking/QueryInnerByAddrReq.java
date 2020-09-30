package com.platon.browser.req.staking;

import com.platon.browser.req.PageReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/***
*@program: QueryInnerByAddrReq.java
*@description:
*@author: Rongjin Zhang
*@create: 2020/9/22
*/
@Data
public class QueryInnerByAddrReq extends PageReq {

	@NotBlank(message = "{address not null}")
	@Size(min = 42,max = 42)
	private String address;

	@NotNull(message = "{type not null}")
	@Size(min = 1,max = 2)
	private Integer type;

}
