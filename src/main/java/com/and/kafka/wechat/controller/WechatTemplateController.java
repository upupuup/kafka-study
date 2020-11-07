package com.and.kafka.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.and.kafka.wechat.common.BaseResponseVo;
import com.and.kafka.wechat.conf.WechatTemplateProperties;
import com.and.kafka.wechat.service.WechatTemplateService;
import com.and.kafka.wechat.utils.FileUtils;
import com.google.common.collect.Maps;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 20:55
 */
@RestController
@RequestMapping("/v1")
public class WechatTemplateController {
    @Autowired
    private WechatTemplateProperties wechatTemplateProperties;

    @Autowired
    private WechatTemplateService wechatTemplateService;

    @GetMapping("/template")
    public BaseResponseVo getTemplate() {
        WechatTemplateProperties.WechatTemplate wechatTemplate = wechatTemplateService.getWechatTemplate();

        Map<Object, Object> result = Maps.newHashMap();
        result.put("templateId", wechatTemplate.getTemplateId());
        result.put("template", FileUtils.readFile2JsonArray(wechatTemplate.getTemplateFilePath()));

        return BaseResponseVo.success(result);
    }

    @GetMapping("/template/result")
    public BaseResponseVo templateStatistics(@RequestParam(value = "templateId", required = false) String templateId) {
        JSONObject statistics = wechatTemplateService.templateStatistics(templateId);
        return BaseResponseVo.success(statistics);
    }

    @PostMapping("/template/report")
    public BaseResponseVo dataReported(@RequestBody String reportData) {
        wechatTemplateService.templateReported(JSON.parseObject(reportData));

        return BaseResponseVo.success();
    }
}
