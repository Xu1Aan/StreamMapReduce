package com.xuan.workers.impl;

import com.xuan.Job;
import com.xuan.functions.AggregateFunction;
import com.xuan.messages.BatchMessage;
import com.xuan.messages.Message;
import com.xuan.operators.AggregateOperator;
import com.xuan.operators.Operator;
import com.xuan.workers.Aggregate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/20/16:02
 * @Description:
 */
public class AggregateWorker  implements Aggregate, Serializable {
    private List<Message> batchQueue = new ArrayList<>();
    private AggregateFunction fun;

    public AggregateWorker() {
        List<Operator> operators = Job.jobTwo.getOperators();
        for (Operator operator : operators) {
            if(operator instanceof AggregateOperator) {
                this.fun = ((AggregateOperator) operator).fun;
            }
        }
    }

    @Override
    public BatchMessage onBatchMessage(BatchMessage batchMessage) {

        //stagePoss是用来打印信息的
        System.out.println("AggregateWorker received batch: " + batchMessage);

        // 遍历那一批的数据
        // 需要有一个Map<key,List(value)>
        HashMap<String, List<String>> window = new HashMap<>();
        for(Message message : batchMessage.getMessages()) {
            // Get key and value of the message
            // 取出每一个message的key和value
            final String key = message.getKey();
            final String value = message.getVal();

            List<String> winValues = window.get(key);
            if (winValues == null) {
                winValues = new ArrayList<>();
                window.put(key, winValues);
            }
            winValues.add(value);
        }
        for (Map.Entry < String, List<String> > entry: window.entrySet()) {
            String word = entry.getKey();
            List<String> winValues = entry.getValue();
            final Message result = fun.process(word, winValues);
            batchQueue.add(result);
        }

        System.out.println("batchMessage.getBatchInfo():"+batchMessage.getBatchInfo());
        return new BatchMessage(batchQueue,batchMessage.getBatchInfo());
    }
}
