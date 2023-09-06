package com.platon.browser.bean;


import com.platon.protocol.core.Response;
import lombok.Data;

import java.util.List;

@Data
public class NodeResult extends Response<List<ValidatorEx>> {

}
