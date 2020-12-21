package com.platon.browser.adjustment.bean;

import com.platon.browser.adjustment.context.AbstractAdjustContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidatedContext {
    private List<AbstractAdjustContext> stakingAdjustContextList=new ArrayList<>();
    private List<AbstractAdjustContext> delegateAdjustContextList=new ArrayList<>();
}
