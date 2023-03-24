package com.xuan.nodes;

import com.xuan.Job;
import com.xuan.config.Config;
import com.xuan.functions.AggregateFunction;
import com.xuan.functions.FilterFunction;
import com.xuan.functions.MapFunction;
import com.xuan.messages.source.KafkaSourceMsg;
import com.xuan.operators.AggregateOperator;
import com.xuan.operators.FilterOperator;
import com.xuan.operators.MapOperator;
import com.xuan.operators.Operator;
import com.xuan.workers.Aggregate;
import com.xuan.workers.Filter;
import com.xuan.workers.Mapf;
import com.xuan.workers.impl.AggregateWorker;
import com.xuan.workers.impl.FilterWorker;
import com.xuan.workers.impl.MapWorker;
import org.apache.dubbo.rpc.cluster.specifyaddress.Address;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/15/20:00
 * @Description:
 */
public class Master {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        starterNode(Job.jobTwo.getOperators(),context);
    }

    public static void  starterNode(List<Operator> operators, ClassPathXmlApplicationContext context) {

        int numMachines = Config.getMachineNumbers();

        //创建rpc节点Sink
        Sink sink = new Sink();

        //一个stage就是一个算子，一批运行在各个机器上的同一个算子
        int numStages = operators.size();

        int posStage = numStages + 1;

        List<Operator> reverseOperators = new ArrayList<>();


        for(Operator op : operators) {
            reverseOperators.add(op.clone());
        }

        List<Mapf> mapWorkers = new ArrayList<>();
        List<Filter> filterWorkers = new ArrayList<>();
        List<Aggregate> aggregateWorkers = new ArrayList<>();

        for(Operator op : reverseOperators) {
            posStage--;
            boolean isLocal;
            String rootName = op.name;

            //对每个算子，遍历机器
            for(int i=0; i < numMachines; i++) {
                //判断计算节点部署在本机还是远程
                if(i==0) {
                    isLocal = true;
                } else {
                    isLocal = false;
                }

                op.name = rootName + "-" + Integer.toString(i+1);
                //进行运算逻辑，给master发送创建算子的消息，master就会创建相应的worker
                // Map
                if(op instanceof MapOperator) {
                    if (isLocal){
                        mapWorkers.add(new MapWorker());
                    }else {
                        Mapf nodeMapWorker = (Mapf) context.getBean("mapWorker");
                        mapWorkers.add(nodeMapWorker);
                    }
                }
                // Filter
                else if(op instanceof FilterOperator) {
                    if (isLocal){
                        filterWorkers.add(new FilterWorker());
                    }else {
                        Filter nodeFilterWorker = (Filter) context.getBean("filterWorker");
                        filterWorkers.add(nodeFilterWorker);
                    }
                }
                // Aggregate
                else if(op instanceof AggregateOperator) {
                    if (isLocal){
                        aggregateWorkers.add(new AggregateWorker());
                    }else {
                        Aggregate nodeAggregateWorker = (Aggregate) context.getBean("aggregateWorker");
                        aggregateWorkers.add(nodeAggregateWorker);
                    }
                }
            }
        }
        System.out.println(String.format("Operators created! Total number of stages: %d", numStages));
        final Source source = new Source(mapWorkers,filterWorkers,aggregateWorkers,sink);

        KafkaSourceMsg kafkaSourceMsg = new KafkaSourceMsg(Config.getTopic(),Config.getSize());
        source.onKafkaMsg(kafkaSourceMsg);

        System.out.println("Source created!");

    }
}
