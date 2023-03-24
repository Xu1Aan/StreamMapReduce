## StreamMapReduce🌟
> 感谢Star, 项目等其他问题交流可发[邮件](mailto:toxuan1998@qq.com)

基于Java语言，利用Zookeeper和DubboRPC框架实现的分布式流式计算引擎，可以实现带有触发时延的窗口wordcount应用。

### 1、架构设计

ToDo

### 2、项目模块介绍

#### 2.1 config

该模块是读取配置文件（application.properties）中得配置信息

#### 2.2 datagen

该模块是数据生成器：持续随机生成指定范围指定格式的数据发往KafkaTopic test-topic。

数据格式：

```
KafkaDataGen:word,count
KafkaDataGenWithTs:timestamp,word,count
```

#### 2.3 function

该模块是计算函数函数的接口 包含map(映射算子)，aggregate(聚合算子)，filter(过滤算子)三种算子，用户可以使用该接口自定义算子的计算逻辑。

#### 2.4 message

该模块是封装PRC直接通信的数据消息

#### 2.5 node

该模块是对分布式节点的一系列操作

#### 2.6 operators

该模块是function的封装，为算子链中的算子对象。

#### 2.7 utils

该模块是Kafka消费者连接模块。KafkaSource:写了测试时Kafka集群的配置，获取Kafka的Consumer，在Source节点被调用拉取数据。

#### 2.9 workers

该模块包含map(映射算子)，aggregate(聚合算子)，filter(过滤算子)三种worker。运行时会由Master在每台机器上创建，接收上游算子数据计算之后发往下游。

#### 2.10 job

封装了用户自定义的Job，一个Job为一个用户自定义的算子链。

```
public static final Job jobOne = new Job(Arrays.asList(
        //先全部变成小写
        new MapOperator("Map",(MapFunction & Serializable)(String k,String v)->{
            String newKey = k.toLowerCase();
            return new Message(newKey,v);
            }),
        //保留h开头的key的kv对
        new FilterOperator("Filter",(FilterFunction & Serializable) (String k,String v)-> {
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
```

### 3、项目部署

项目部署环境为至少两台linux主机或者两个端口（Server和Collaborator）。
