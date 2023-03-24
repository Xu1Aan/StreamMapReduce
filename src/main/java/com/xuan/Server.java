package com.xuan;


import com.xuan.nodes.Master;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.Arrays;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/15/19:44
 * @Description:
 */
public class Server {
    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        Master.starterNode(Job.jobTwo.getOperators(),context);
    }
}
