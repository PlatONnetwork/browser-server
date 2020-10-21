package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import lombok.Data;

import java.util.List;

@Data
public class ESTokenTransferRecordEvent {
    private List<ESTokenTransferRecord> esTokenTransferRecordList;
}