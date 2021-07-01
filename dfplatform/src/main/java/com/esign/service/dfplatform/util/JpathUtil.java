package com.esign.service.dfplatform.util;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: huangtai
 * @Description: Jpath工具类
 * @Date: 2020/9/22 19:42
 */
public class JpathUtil {

    /**
     * 根据JsonPath表达式获取结果
     *
     * @param json
     * @param jsonPath
     * @return
     */
    public static String getValueByPath(String json, String jsonPath) {

        if (jsonPath.startsWith("str")) {

            String[] path = StringUtils.split(jsonPath, ".");
            return StringUtils.substringBetween(json, path[1], path[2]);
        } else {

            return JsonPath.read(json, jsonPath) + "";
        }
    }
}
