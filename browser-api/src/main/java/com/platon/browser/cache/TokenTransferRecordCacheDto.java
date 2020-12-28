package com.platon.browser.cache;

import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.response.RespPage;

import java.util.ArrayList;
import java.util.List;


public class TokenTransferRecordCacheDto {

	public TokenTransferRecordCacheDto() {
		this.transferRecordList = new ArrayList<>();
	}

	public TokenTransferRecordCacheDto(List<ESTokenTransferRecord> transferRecordList, RespPage page) {
		this.transferRecordList = transferRecordList;
		this.page = page;
	}
	private List<ESTokenTransferRecord> transferRecordList;
	
	private RespPage page;

	public List<ESTokenTransferRecord> getTransferRecordList() {
		return transferRecordList;
	}

	public void setTransferRecordList(List<ESTokenTransferRecord> transferRecordList) {
		this.transferRecordList = transferRecordList;
	}

	public RespPage getPage() {
		return page;
	}

	public void setPage(RespPage page) {
		this.page = page;
	}
	
	
	
}
