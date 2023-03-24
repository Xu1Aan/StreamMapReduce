package com.xuan.workers.impl;

import com.xuan.Job;
import com.xuan.functions.MapFunction;
import com.xuan.messages.BatchMessage;
import com.xuan.messages.Message;
import com.xuan.operators.MapOperator;
import com.xuan.operators.Operator;
import com.xuan.workers.Mapf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/20/10:43
 * @Description:
 */
public class MapWorker implements Mapf, Serializable {
    private MapFunction fun;

    public MapWorker() {
        List<Operator> operators = Job.jobTwo.getOperators();
        for (Operator operator : operators) {
            if(operator instanceof MapOperator) {
                fun = ((MapOperator) operator).fun;
            }
        }
    }
    private List<Message> batchQueue = new ArrayList<>();

    @Override
    public BatchMessage onBatchMessage(BatchMessage batchMessage) {

        System.out.println("MapWorker received batch: " + batchMessage);

        for(Message message : batchMessage.getMessages()) {
            final Message result = fun.process(message.getKey(), message.getVal());
            batchQueue.add(result);
        }
        System.out.println("batchMessage.getBatchInfo():"+batchMessage.getBatchInfo());
        return new BatchMessage(batchQueue,batchMessage.getBatchInfo());
    }

}
