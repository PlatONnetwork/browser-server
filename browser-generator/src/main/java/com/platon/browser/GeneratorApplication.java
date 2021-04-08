package com.platon.browser;

import com.platon.browser.core.BuildFactory;

public class GeneratorApplication {

    public static void main(String[] args) {
        BuildFactory bf = new BuildFactory();
        bf.coreRun("address", "token");
    }

}
