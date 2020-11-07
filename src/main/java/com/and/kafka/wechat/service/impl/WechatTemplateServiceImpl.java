package com.and.kafka.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.and.kafka.wechat.conf.WechatTemplateProperties;
import com.and.kafka.wechat.service.WechatTemplateService;
import com.and.kafka.wechat.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 20:56
 */
@Slf4j
@Service
public class WechatTemplateServiceImpl implements WechatTemplateService {
    static final String TOPIC_NAME = "AND_TOPIC";

    @Autowired
    private WechatTemplateProperties properties;

    @Autowired
    private Producer producer;

    /**
     * 获取微信调查问卷的模板，获取目前active为true的模板就行了
     * @return
     */
    @Override
    public WechatTemplateProperties.WechatTemplate getWechatTemplate() {
        List<WechatTemplateProperties.WechatTemplate> templates = properties.getTemplates();

        Optional<WechatTemplateProperties.WechatTemplate> wechatTemplate
                = templates.stream().filter((template) -> template.isActive()).findFirst();

        return wechatTemplate.isPresent() ? wechatTemplate.get() : null;
    }

    /**
     * 上报调查问卷填写结果
     * @param reportInfo
     */
    @Override
    public void templateReported(JSONObject reportInfo) {
        log.info("templateReported:[{}]", reportInfo);

        String templateId = reportInfo.getString("templateId");
        JSONArray reportData = reportInfo.getJSONArray("result");

        // 发送kafka数据，如果templateId相同，那么可以考虑将相同的id放在一个partition，便于分析使用
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC_NAME, templateId, reportData);

        /**
         * 1、kafka producer 是线程安全的，建议多线程复用，如果每个线程都创建，出现大量上下文切换或者争抢的情况，影响kafka效率
         * 2、kafka producer 的key是一个很重要的内容
         *  2.1 我们可以根据key完成partition的负载均衡
         *  2.2 合理的key设计，可以让Flink、 Spark Streaming之类的实时分析工具做快速处理
         * 3、ack-all，kafka层面上只有一次的消息投递保障，如果想真的不丢数据，最好自行处理异常
         */
        try{
            producer.send(record);
        } catch (Exception e) {
            // 将数据放入重发队列 redis，mq
        }
    }

    /**
     * 获取调查问卷的统计结果
     * @param templateId
     * @return
     */
    @Override
    public JSONObject templateStatistics(String templateId) {
        // 判断数据结结果获取类型
        if (properties.getTemplateResultType() == 0) {
            // 文件获取
            return FileUtils.readFile2JsonObject(properties.getTemplateResultFilePath()).get();
        } else {
            // DB
        }
        return null;
    }
}
