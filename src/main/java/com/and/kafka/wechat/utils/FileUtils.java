package com.and.kafka.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Optional;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 21:06
 */
@Slf4j
public class FileUtils {
    public static String readFile(String filePath) throws Exception {
        @Cleanup
        BufferedReader reader = new BufferedReader(
                new FileReader(new File(filePath))
        );

        String lingStr = "";
        StringBuffer stringBuffer = new StringBuffer();
        while ((lingStr = reader.readLine()) != null) {
            stringBuffer.append(lingStr);
        }

        return stringBuffer.toString();
    }

    public static Optional<JSONObject> readFile2JsonObject(String filePath) {
        String fileContent = null;
        try {
            fileContent = readFile(filePath);
            log.info("readFile2JsonObject fileContent: [{}]" + fileContent);
            return Optional.ofNullable(JSON.parseObject(fileContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<JSONArray> readFile2JsonArray(String filePath) {
        try {
            String fileContent = readFile(filePath);
            log.info("readFile2JsonArray fileContent: [{}]" + fileContent);
            return Optional.ofNullable(JSON.parseArray(fileContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
