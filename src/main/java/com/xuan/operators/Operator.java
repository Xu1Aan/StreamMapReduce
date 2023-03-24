package com.xuan.operators;

//算子基类
public abstract class Operator {
    public String name;


    public Operator(String name) {
        this.name = name;

    }

    public abstract Operator clone();

}

