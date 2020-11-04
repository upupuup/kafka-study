package com.and.kafka.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.*;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/3 22:29
 */
public class AdminSample {

    static final String TOPIC_NAME = "AND_TOPIC";
    static final String IP = "47.102.115.74:9092";

    public static void main(String[] args) throws Exception {
//        AdminClient adminClient = AdminSample.adminClient();
//        System.out.println("adminClient" + adminClient);

//        createTopic();

        topicList();
    }

    /**
     * 创建topic
     */
    public static void createTopic() {
        AdminClient adminClient = adminClient();
        Short rs = 1;
        NewTopic newTopic = new NewTopic(TOPIC_NAME, 1, rs);
        CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));
        System.out.println("create topics result:" + topics);
    }

    /**
     * 获取topic列表
     * @throws Exception
     */
    public static void topicList() throws Exception {
        AdminClient adminClient = adminClient();

        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true);

//        ListTopicsResult listTopicsResult = adminClient.listTopics();
        ListTopicsResult listTopicsResult = adminClient.listTopics(options);
        Set<String> names = listTopicsResult.names().get();
        Collection<TopicListing> topicListings = listTopicsResult.listings().get();
        KafkaFuture<Map<String, TopicListing>> mapKafkaFuture = listTopicsResult.namesToListings();

        // 打印
        names.stream().forEach(System.out::println);
        topicListings.stream().forEach(System.out::println);
        System.out.println(mapKafkaFuture.toString());
    }

    /**
     * 设置AdminClient
     * @return
     */
    public static AdminClient adminClient() {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, IP);

        AdminClient adminClient =  AdminClient.create(properties);
        return adminClient;
    }
}
