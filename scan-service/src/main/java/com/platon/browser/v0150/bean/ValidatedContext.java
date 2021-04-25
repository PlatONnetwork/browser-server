package com.platon.browser.v0150.bean;

import com.platon.browser.v0150.context.AbstractAdjustContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidatedContext {
    private List<AbstractAdjustContext> stakingAdjustContextList=new ArrayList<>();
    private List<AbstractAdjustContext> delegateAdjustContextList=new ArrayList<>();
}
