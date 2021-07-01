package com.esign.service.dfplatform.util.client;

import lombok.Data;

import java.util.Map;

/**
 * @author huangtai
 * @Description 返回结果对象
 * @Date: 2020/6/2 18:11
 */
@Data
public class HttpClientResponse {

    private String stateCode;

    private Object responseBody;

    private Map<String, Object> headers;
}
