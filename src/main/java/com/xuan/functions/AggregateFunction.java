package com.xuan.functions;

import com.xuan.messages.Message;

import java.io.Serializable;
import java.util.List;
//聚合函数
public interface AggregateFunction extends AbstractFunction, Serializable {
    public Message process(String key, List<String> values);

}
