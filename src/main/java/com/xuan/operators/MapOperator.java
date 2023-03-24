package com.xuan.operators;

import com.xuan.functions.MapFunction;

import java.io.Serializable;

//映射算子
public class  MapOperator extends Operator implements Serializable {
    public MapFunction fun;

    public MapOperator(String name,  MapFunction fun) {
        super(name);
        this.fun = fun;
    }

    @Override
    public MapOperator clone() {
        return new MapOperator(this.name, this.fun);
    }
}
