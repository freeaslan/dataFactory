package com.esign.service.dfplatform.util.client;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.esign.service.dfplatform.util.ConvertUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Reporter;

/**
 * @author huangtai
 * @Description 请求工具类
 * @Date: 2020/6/2 18:11
 */
@Slf4j
public class HttpClientUtil {

    /**
     * 发起请求
     *
     * @param request
     * @param proxy
     * @param isUseProxy
     * @return
     */
    public static HttpClientResponse sendRequestByProxy(HttpClientRequest request, HttpHost proxy, boolean isUseProxy) {

        //1:GET 2:POST 3:PUT 4:DELETE 5:PATCH
        String type = request.getType().toLowerCase();
        String host = request.getHost();
        if (!StringUtils.startsWith(request.getHost(), "http://") &&
                !StringUtils.startsWith(request.getHost(), "https://")) {

            host = "http://" + host;
        }
        String url = host + request.getUrl();
        Map<String, String> headers = request.getHeaders();
        Object body = request.getRequestBody();
        String bodyStr = null;
        HttpClientResponse response = null;
        log.info("Request url is {}", url);
        if (!StringUtils.isEmpty(url)) {

            HttpClientUtil httpClientUtil = new HttpClientUtil();
            CloseableHttpClient httpclient = null;
            if (isUseProxy) {

                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
                httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
                log.info("Build http client with proxy: {}.", ConvertUtil.toJson(proxy));
            } else {

                httpclient = HttpClientBuilder.create().build();//init connection
            }

            if (null != body) {
                if (body instanceof Map || body instanceof List) {
                    //get body
                    bodyStr = ConvertUtil.toJson(body);
                } else if (body instanceof String) {

                    bodyStr = (String) body;
                }
            } else {
                log.info("Request body is empty.");
            }
            url = url.replace(" ", "%20");
            Reporter.log("<pre><b>请求地址:</b>\n" + url + "</pre>");
            Reporter.log("<pre><b>请求参数:</b>\n" + bodyStr + "</pre>");
            if (headers != null) {
                Reporter.log("<pre><b>请求头部:</b>\n" + headers.toString() + "</pre>");
            }
            try {
                switch (type) {
                    case "get":
                        HttpGet httpGet = new HttpGet(url);
                        response = httpClientUtil.sendHTTPRequest(httpclient, httpGet, headers, null);
                        break;
                    case "post":
                        HttpPost httpPost = new HttpPost(url);
                        response = httpClientUtil.sendHTTPRequest(httpclient, httpPost, headers, bodyStr);
                        break;
                    case "put":
                        HttpPut httpPut = new HttpPut(url);
                        response = httpClientUtil.sendHTTPRequest(httpclient, httpPut, headers, bodyStr);
                        break;
                    case "delete":
                        HttpDelete httpDelete = new HttpDelete(url);
                        response = httpClientUtil.sendHTTPRequest(httpclient, httpDelete, headers, null);
                        break;
                    case "patch":
                        HttpPatch httpPatch = new HttpPatch(url);
                        response = httpClientUtil.sendHTTPRequest(httpclient, httpPatch, headers, bodyStr);
                        break;
                    default:
                        log.error("HttpClientRequest.type must >0 and <6.");
                        break;
                }
            } catch (Exception e) {
                log.error("Send http request failed.");
                e.printStackTrace();
            } finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    log.error("Close connction of http client failed.");
                    e.printStackTrace();
                }
            }
        } else {
            log.error("Url can not be empty: {}.", url);
        }

        Reporter.log("<pre><b>返回结果:</b>\n" + response.getResponseBody() + "</pre>");
        return response;
    }

    /**
     * 发请求
     *
     * @param request
     * @return
     */
    public static HttpClientResponse sendRequest(HttpClientRequest request) {

        return HttpClientUtil.sendRequestByProxy(request, null, false);
    }

    /**
     * @param httpclient
     * @param httpRequestBase
     * @param headers
     * @param body
     * @return
     */
    public HttpClientResponse sendHTTPRequest(CloseableHttpClient httpclient, HttpRequestBase httpRequestBase,
                                              Map<String, String> headers, String body) {

        HttpClientResponse httpClientResponse = null;
        try {
            //format httpRequestBase
            formatHttpRequestBase(headers, httpRequestBase, body);
            //send request
            CloseableHttpResponse response = httpclient.execute(httpRequestBase);
            //edit HttpClientResponse
            httpClientResponse = formatReponse(response);
            response.close();
        } catch (ClientProtocolException e) {
            log.error("HttpClientUtil do not support this protocol.", e);
        } catch (IOException e) {
            log.error("HttpClientUtil send request failed.", e);
        }
        return httpClientResponse;
    }

    /**
     * 格式化返回结果
     *
     * @param response
     * @return
     */
    public HttpClientResponse formatReponse(CloseableHttpResponse response) {

        HttpClientResponse httpClientResponse = new HttpClientResponse();
        httpClientResponse.setStateCode(response.getStatusLine().toString().split(" ")[1]);
        Header[] responseHeaders = response.getAllHeaders();
        HashMap<String, Object> responseHeadersMap = new HashMap<String, Object>();
        for (int i = 0; i < responseHeaders.length; i++) {

            Header header = responseHeaders[i];
            responseHeadersMap.put(header.getName(), header.getValue());
        }
        String traceId = (String) (responseHeadersMap.get("Pinpoint-TxId-Resp") != null ?
                responseHeadersMap.get("Pinpoint-TxId-Resp") : responseHeadersMap.get("X-Trace-TraceID"));
        String deceiverTraceingId = (String) responseHeadersMap.get("Deceiver-Traceing-ID");
        if (null != deceiverTraceingId) {
            log.warn("this response form Deceiver, Deceiver-Traceing-ID: {}", deceiverTraceingId);
        }
        httpClientResponse.setHeaders(responseHeadersMap);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null && (entity.getContentLength() > 0 || entity.isChunked())) {
                if (entity.getContent() != null) {
                    String bodyStr = getBodyStrFromHttpEntity(entity, traceId);
                    httpClientResponse.setResponseBody(bodyStr);
                }
            } else {
                log.warn("Response body is empty.");
                httpClientResponse.setResponseBody(null);
            }
        } catch (Exception e) {
            log.error("Format CloseableHttpResponse to HttpClientResponse failed. {}", e);
        }

        return httpClientResponse;
    }

    /**
     * 格式化request
     *
     * @param headers
     * @param httpRequestBase
     * @param body
     */
    public void formatHttpRequestBase(Map<String, String> headers, HttpRequestBase httpRequestBase, String body) {
        //request body 默认charset
        String encodingOfRequestBody = "UTF-8";
        if (null != headers && !headers.isEmpty()) {
            Iterator<Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Entry<String, String>) iter.next();
                if (!entry.getKey().toLowerCase().equals("content-length")) {//setEntity会同时set content-length
                    httpRequestBase.setHeader(entry.getKey(), entry.getValue());
                    if (entry.getKey().toLowerCase().equals("content-type")
                            && entry.getValue().split(";").length >= 2) {
                        encodingOfRequestBody = entry.getValue().split(";")[1].split("=")[1];
                    }
                }
            }
        }

        //继承HttpRequestBase的子类有：HttpDelete、HttpGet、HttpHead、HttpOptions、HttpTrace
        //继承HttpEntityEnclosingRequestBase的子类有：HttpPut、HttpPost、HttpPatch
        if (null != body && httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(new StringEntity(body, encodingOfRequestBody));
            log.info("Request body is {}.", body);
        }
    }


    /**
     * @param httpEntity
     * @param traceId
     * @return
     */
    public String getBodyStrFromHttpEntity(HttpEntity httpEntity, String traceId) {

        String str = null;
        Header header = httpEntity.getContentType();
        boolean isChunked = httpEntity.isChunked();
        if (isChunked) {
            try {
                String charset = "UTF-8";
                if (null != header && header.getValue().contains("charset")) {
                    //获取body的charset
                    String[] charsetList = header.getValue().split(";");
                    for (int i = 0; i < charsetList.length; i++) {
                        if (charsetList[i].split("=")[0].contains("charset")) {
                            charset = charsetList[i].split("=")[1];
                        }
                    }
                }
                str = IOUtils.toString(httpEntity.getContent(), charset);
            } catch (IOException e) {
                log.error("{}", e.getMessage(), e);
            }
        } else if (header != null) {
            try {
                if (header.getValue().contains("text") || header.getValue().contains("charset")) {
                    String charset = "UTF-8";
                    if (header.getValue().contains("charset")) {
                        //获取body的charset
                        charset = header.getValue().split(";")[1].split("=")[1];
                    }
                    str = IOUtils.toString(httpEntity.getContent(), charset);
                } else if (header.getValue().contains("application/json")) {//Json
                    Object obj = new ObjectMapper().readValue(httpEntity.getContent(), Object.class);
                    if (obj instanceof String) {
                        str = String.valueOf(obj);
                    } else {
                        str = ConvertUtil.toJson(obj);
                    }
                } else {
                    log.error("Wrong contentType. HttpClient can not format. Please contact author.");
                }
            } catch (JsonMappingException e) {
                log.error("{}", e.getMessage(), e);
            } catch (JsonParseException e) {
                log.error("{}", e.getMessage(), e);
            } catch (IOException e) {
                log.error("{}", e.getMessage(), e);
            }
        } else {
            log.error("Transfer method is not chunked and don't hava a header of content-type.");
        }
        log.info("[traceId: {}] Response body is :{}", traceId, str);
        return str;
    }
}
