package com.xuan.functions;

import java.io.Serializable;

//过滤函数
public interface FilterFunction extends AbstractFunction, Serializable {
    public Boolean predicate(String key, String value);
}
