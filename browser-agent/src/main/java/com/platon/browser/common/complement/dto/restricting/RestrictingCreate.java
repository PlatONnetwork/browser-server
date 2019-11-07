package com.platon.browser.common.complement.dto.restricting;

import java.util.ArrayList;
import java.util.List;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 锁仓消息 <br/>
 * <pre>
insert into `rp_plan` 
	(`address`, 
	`epoch`, 
	`amount`, 
	`number`
	)
	values
	('address', 
	'epoch', 
	'amount', 
	'number'
	);

 * <pre/>
 * @author chendai
 */
@Data
@Builder
@Accessors(chain = true)
public class RestrictingCreate extends BusinessParam {
	
	List<RestrictingItem> itemList;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.RESTRICTING_CREATE;
    }
}
