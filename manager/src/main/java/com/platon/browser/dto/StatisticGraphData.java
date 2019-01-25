package com.platon.browser.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class StatisticGraphData {
    private List<Long> x=new LinkedList<>();
    private List<Double> ya=new LinkedList<>();
    private List<Long> yb=new LinkedList<>();
}
