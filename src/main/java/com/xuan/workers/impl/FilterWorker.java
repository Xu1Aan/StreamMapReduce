package com.xuan.workers.impl;

import com.xuan.Job;
import com.xuan.functions.FilterFunction;
import com.xuan.messages.BatchMessage;
import com.xuan.messages.Message;
import com.xuan.operators.FilterOperator;
import com.xuan.operators.Operator;
import com.xuan.workers.Filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/20/16:00
 * @Description:
 */
public class FilterWorker implements Filter, Serializable {

    private  FilterFunction fun;

    private List<Message> batchQueue = new ArrayList<>();

    public FilterWorker() {
        List<Operator> operators = Job.jobTwo.getOperators();
        for (Operator operator : operators) {
            if(operator instanceof FilterOperator) {
                this.fun = ((FilterOperator) operator).fun;
            }
        }
    }

    //处理一批数据（一个窗口）
    @Override
    public BatchMessage onBatchMessage(BatchMessage batchMessage) {

        System.out.println("FilterWorker received batch: " + batchMessage);



        for(Message message : batchMessage.getMessages()) {
            //根据判断函数判断是否要加进结果队列中
            final Boolean predicateResult = fun.predicate(message.getKey(), message.getVal());

            if(predicateResult) {
                batchQueue.add(message);
            }
        }
       /* final int receiver = Math.abs(batchQueue.get(0).getKey().hashCode()) % downstream.size();
        downstream.get(receiver).tell(new BatchMessage(batchQueue,batchMessage.getBatchInfo()), self());*/
        System.out.println("batchMessage.getBatchInfo():"+batchMessage.getBatchInfo());
        return new BatchMessage(batchQueue,batchMessage.getBatchInfo());
    }

}
