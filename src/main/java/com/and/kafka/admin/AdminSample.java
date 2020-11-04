package com.and.kafka.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/3 22:29
 */
public class AdminSample {

    static final String TOPIC_NAME = "AND_TOPIC";
    static final String IP = "47.102.115.74:9092";
    static final String PREALLOCATE = "preallocate";

    public static void main(String[] args) throws Exception {
//        AdminClient adminClient = AdminSample.adminClient();
//        System.out.println("adminClient" + adminClient);

        // 创建实例
//        createTopic();
        // 删除实例
//        delTopics();

        // 修改配置
//        alterConfig();
        // 查看所有实例
//        topicList();
        // 增加partitions
        incrPartitions(2);
        // 查看topic具体信息
        describeTopics();
        // 查看配置信息
//        describeConfig();

    }

    /**
     * 增加partitions
     * @param partitions
     * @throws Exception
     */
    public static void incrPartitions(int partitions) throws Exception {
        AdminClient adminClient = adminClient();
        Map<String, NewPartitions> partitionsMap = new HashMap<>(16);
        NewPartitions newPartitions = NewPartitions.increaseTo(partitions);
        partitionsMap.put(TOPIC_NAME, newPartitions);

        CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(partitionsMap);
        createPartitionsResult.all().get();
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
     * 修改配置
     * @throws Exception
     */
    public static void alterConfig() throws Exception {
        AdminClient adminClient = adminClient();

        // 过期的方式
//        HashMap<ConfigResource, Config> configMaps = new HashMap<>(16);
//
//        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
//        Config config = new Config(Arrays.asList(new ConfigEntry(PREALLOCATE, "true")));
//        configMaps.put(configResource, config);
//
//        AlterConfigsResult alterConfigsResult = adminClient.alterConfigs(configMaps);

        // 现在的方式
        HashMap<ConfigResource, Collection<AlterConfigOp>> configMaps = new HashMap<>(16);

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        AlterConfigOp alterConfigOp = new AlterConfigOp(new ConfigEntry(PREALLOCATE, "false"), AlterConfigOp.OpType.SET);

        configMaps.put(configResource, Arrays.asList(alterConfigOp));
        AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(configMaps);
        alterConfigsResult.all().get();
    }


    /**
     * 查看配置信息
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void describeConfig() throws ExecutionException, InterruptedException {
        AdminClient adminClient = adminClient();
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        configResourceConfigMap.entrySet().stream().forEach((entry) -> {
            System.out.println("configResource:" + entry.getKey() + ",config:" + entry.getValue());
        });

    }

    /**
     * 查看topic描述
     */
    public static void describeTopics() throws ExecutionException, InterruptedException {
        AdminClient adminClient = adminClient();
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(TOPIC_NAME));
        Map<String, TopicDescription> stringTopicDescriptionMap = describeTopicsResult.all().get();
        Set<Map.Entry<String, TopicDescription>> entries = stringTopicDescriptionMap.entrySet();
        entries.stream().forEach(System.out::println);
    }

    /**
     * 删除topic
     * @throws Exception
     */
    public static void delTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TOPIC_NAME));
        deleteTopicsResult.all().get();
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
