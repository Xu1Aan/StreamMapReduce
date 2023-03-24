package com.xuan;



import com.xuan.functions.AggregateFunction;
import com.xuan.functions.FilterFunction;
import com.xuan.functions.MapFunction;
import com.xuan.messages.Message;
import com.xuan.operators.AggregateOperator;
import com.xuan.operators.FilterOperator;
import com.xuan.operators.MapOperator;
import com.xuan.operators.Operator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Job implements Serializable {
    private static final long serialVersionUID = -4035435954110471507L;

    private List<Operator> operators;

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Job(List<Operator> operators, String name) {
        this.operators = operators;
        this.name = name;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public String getName() {
        return name;
    }

    //一个聚合函数，wordcountJob
    public static final Job jobOne = new Job(Arrays.asList(
            //先全部变成小写
            new MapOperator("Map",(MapFunction & Serializable)(String k, String v)->{
                String newKey = k.toLowerCase();
                return new Message(newKey,v);
                }),
            //保留h开头的key的kv对
            new FilterOperator("Filter",(FilterFunction & Serializable) (String k, String v)-> {
                if(k.startsWith("h")){
                    return true;
                }
                else {
                    return false;
                }
            }),
            //聚合
            new AggregateOperator(  "Aggregate",(AggregateFunction & Serializable) (String k, List<String> vs)->{
                int sum = 0;
                for(int i=0;i<vs.size();i++){
                    sum+=Integer.parseInt(vs.get(i));
                }
                return new Message(k,sum+"");
            }
            )
            ), "jobOne");
    public static final Job jobTwo = new Job(Arrays.asList(
            //先全部变成小写
            new MapOperator("Map",(MapFunction & Serializable)(String k,String v)->{
                String newKey = k.toLowerCase();
                return new Message(newKey,v);
            }),
            //聚合
            new AggregateOperator(  "Aggregate",(AggregateFunction & Serializable) (String k, List<String> vs)->{
                int sum = 0;
                for(int i=0;i<vs.size();i++){
                    sum+=Integer.parseInt(vs.get(i));
                }
                return new Message(k,sum+"");
            }
            )
    ), "jobTwo");

}


