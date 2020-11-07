package com.and.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.Properties;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 19:55
 */
@Slf4j
public class SamplePartition implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        String keyStr = key + "";
        String keyInt = keyStr.substring(4);

        log.info("keyStr:" + keyStr + " keyInt:" + keyInt);
        int i = Integer.parseInt(keyInt);
        return i % 2;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
