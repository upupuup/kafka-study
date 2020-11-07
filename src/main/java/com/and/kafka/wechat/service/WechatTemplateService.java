package com.and.kafka.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.and.kafka.wechat.conf.WechatTemplateProperties;

public interface WechatTemplateService {
    /**
     * 获取微信调查问卷的模板，获取目前active为true的模板就行了
     * @return
     */
    WechatTemplateProperties.WechatTemplate getWechatTemplate();

    /**
     * 上报调查问卷填写结果
     * @param reportInfo
     */
    void templateReported(JSONObject reportInfo);

    /**
     * 获取调查问卷的统计结果
     * @param templateId
     * @return
     */
    JSONObject templateStatistics(String templateId);
}
