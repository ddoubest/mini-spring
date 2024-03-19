package com.minis.aop;

public interface PointCutAdvisor extends Advisor {
    PointCut getPointCut();
}
