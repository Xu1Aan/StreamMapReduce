package com.xuan.functions;

import com.xuan.messages.Message;

import java.io.Serializable;

//映射函数
public interface MapFunction extends AbstractFunction, Serializable {
    public Message process(String key, String value);
}
