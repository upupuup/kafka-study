package com.and.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/4 21:49
 */
public class ProducerSample {
    static final String TOPIC_NAME = "AND_TOPIC";
    static final String IP = "47.102.115.74:9092";

    public static void main(String[] args) throws Exception {
        // producer异步发送演示
//        producerSend();

        // 同步发送,异步阻塞发送
//        producerSyncSend();

        // producer异步发送带回调函数演示
//        producerSendWithCallBack();

        // producer异步发送带回调函数和Partition负载均衡
        producerSendWithCallBackAndPartition();
    }

    /**
     * 同步发送,异步阻塞发送
     */
    private static void producerSyncSend() throws Exception {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 1000; i++) {
            String key = "key-" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            Future<RecordMetadata> send = producer.send(record);
            RecordMetadata recordMetadata = send.get();
            System.out.println(key + ",partitions:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());
        }

        // 所有的通道都需要关闭
        producer.close();
    }


    /**
     * producer异步发送带回调函数和Partition负载均衡
     */
    public static void producerSendWithCallBackAndPartition() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.and.kafka.producer.SamplePartition");

        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 100; i++) {
            String key = "key-" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            producer.send(record, (recordMetadata, e) -> {
                System.out.println(key + ",partitions:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());
            });
        }

        // 所有的通道都需要关闭
        producer.close();
    }

    /**
     * producer异步发送带回调函数演示
     */
    public static void producerSendWithCallBack() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            String key = "key-" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            producer.send(record, (recordMetadata, e) -> {
                System.out.println(key + ",partitions:" + recordMetadata.partition() + ",offset:" + recordMetadata.offset());
            });
        }

        // 所有的通道都需要关闭
        producer.close();
    }

    /**
     * producer异步发送演示
     */
    public static void producerSend() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            producer.send(record);
        }

        // 所有的通道都需要关闭
        producer.close();
    }
}
