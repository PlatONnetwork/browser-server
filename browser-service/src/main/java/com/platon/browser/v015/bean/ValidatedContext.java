package com.platon.browser.v015.bean;

import com.platon.browser.v015.context.AbstractAdjustContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidatedContext {
    private List<AbstractAdjustContext> stakingAdjustContextList=new ArrayList<>();
    private List<AbstractAdjustContext> delegateAdjustContextList=new ArrayList<>();
}
