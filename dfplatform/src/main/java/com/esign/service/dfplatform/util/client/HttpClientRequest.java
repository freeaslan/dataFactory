package com.esign.service.dfplatform.util.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author huangtai
 * @Description 请求对象
 * @Date: 2020/6/2 18:11
 */
@Data
public class HttpClientRequest {

    //1:GET 2:POST 3:PUT 4:DELETE
    private String type;

    private String host;

    private String url;

    private Map<String, String> headers;

    private Map<String, String> query;

    private Map<String, String> path;

    private Object requestBody;

    public String getUrl() {
        return formatUrl(this.url, this.query, this.path);
    }

    /**
     * 格式化URL
     *
     * @param url
     * @param query
     * @param path
     * @return
     */
    private String formatUrl(String url, Map<String, String> query, Map<String, String> path) {

        String result = "";
        if (null != path && !path.isEmpty()) {
            //正则表达式，示例：/{userid}/Info，替换{userid}部分
            String patternString = "\\{(" + StringUtils.join(path.keySet(), "|") + ")\\}";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(url);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, path.get(matcher.group(1)));
            }
            matcher.appendTail(sb);
            result += sb.toString();
        } else {
            result = url;
        }

        if (null != query && !query.isEmpty()) {
            String params = "?";
            Iterator<Entry<String, String>> iter = query.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Entry<String, String>) iter.next();

                String queryParam = entry.getKey() + "=" + entry.getValue();
                if (url.contains(queryParam)) {

                    return url;
                }
                params += queryParam + "&";
            }
            result += params.substring(0, params.length() - 1);
        }

        if (Objects.isNull(result)) {
            return result;
        } else {
            return result.toString();
        }
    }
}
