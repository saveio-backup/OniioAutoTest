package com.ontfs;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        TestNG tng = new TestNG();
        List<String> suites = Lists.newArrayList();
        //添加要执行的testng.xml文件
        suites.add("testNG.xml");
        tng.setTestSuites(suites);
        tng.run();
    }
}
