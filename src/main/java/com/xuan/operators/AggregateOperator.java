package com.xuan.operators;

import com.xuan.Job;
import com.xuan.functions.AggregateFunction;

//聚合算子
public class AggregateOperator extends Operator {
    public AggregateFunction fun;


    public AggregateOperator(String name, AggregateFunction fun) {
        super(name);
        this.fun = fun;
    }

    @Override
    public AggregateOperator clone() {
        return new AggregateOperator(this.name, this.fun);
    }
}