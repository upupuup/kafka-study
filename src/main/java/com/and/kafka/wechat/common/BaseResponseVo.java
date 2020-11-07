package com.and.kafka.wechat.common;

import lombok.Data;

import java.util.UUID;

/**
 * @Description: [java类作用描述]
 * @Author: jiangzhihong
 * @CreateDate: 2020/11/5 20:59
 */
@Data
public class BaseResponseVo<M> {
    private String requestId;

    private M result;

    public static<M> BaseResponseVo success() {
        BaseResponseVo baseResponseVo = new BaseResponseVo();
        baseResponseVo.setRequestId(genRequestId());

        return baseResponseVo;
    }

    public static<M> BaseResponseVo success(M result) {
        BaseResponseVo baseResponseVo = new BaseResponseVo();
        baseResponseVo.setRequestId(genRequestId());
        baseResponseVo.setResult(result);

        return baseResponseVo;
    }

    private static String genRequestId() {
        return UUID.randomUUID().toString();
    }
}
