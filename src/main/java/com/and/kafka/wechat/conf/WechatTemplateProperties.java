package com.and.kafka.wechat.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 21:30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "template")
public class WechatTemplateProperties {

    private List<WechatTemplate> templates;
    // 0：文件获取，1：数据库获取，2：ES
    private int templateResultType;
    private String templateResultFilePath;

    @Data
    public static class WechatTemplate {
        private String templateId;
        private String templateFilePath;
        private boolean active;
    }
}
