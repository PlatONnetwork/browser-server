package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
public class RestrictingCreate implements BusinessParam {
	
	List<RestrictingItem> itemList;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.RESTRICTING_CREATE;
    }
}
