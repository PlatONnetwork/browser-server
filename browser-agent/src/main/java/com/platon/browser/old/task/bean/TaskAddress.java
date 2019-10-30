package com.platon.browser.old.task.bean;

import lombok.Data;

@Data
public class TaskAddress extends TaskBase{
    private String address;
    private String balance;
    private String restrictingBalance;
}
