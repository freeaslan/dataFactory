package com.esign.service.dfplatform.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import org.apache.commons.lang.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 黄台
 * @Description 对象转换器
 * @Date: 2020/6/2 18:11
 */
@Slf4j
public class ConvertUtil {

    /**
     * json转map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> jsonToMap(Object jsonStr) {

        Map<String, Object> map = new HashMap<String, Object>();
        if (jsonStr == null || StringUtils.isEmpty(jsonStr.toString())) {

            return map;
        }

        JSONObject json = JSONObject.parseObject(jsonStr.toString());
        for (Object key : json.keySet()) {
            Object value = json.get(key);
            if (value != null && !JSONNull.getInstance().equals(value)) {
                if (value instanceof JSONArray) {
                    List<Object> list = new ArrayList<Object>();
                    Iterator<JSONObject> it = ((JSONArray) value).iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        if (obj instanceof JSONObject) {
                            JSONObject json2 = (JSONObject) obj;
                            list.add(jsonToMap(json2.toString()));
                        } else {
                            list.add(obj);
                        }
                    }
                    map.put(key.toString(), list);
                } else {
                    map.put(key.toString(), value);
                }
            }
        }
        return map;
    }

    /**
     * 对象转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {

        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            log.error("Convert bean to map failed. Bean is {}.", toJson(obj), e);
        }

        return map;
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {

        String jsonStr = null;
        try {
            jsonStr = new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Convert bean to json failed.", e);
        }

        return jsonStr;
    }

    /**
     * json转对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> clazz) {

        T t = null;
        try {
            t = new ObjectMapper().readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.error("Convert json to bean failed. Please check the json whether correct.", e);
        }

        return t;
    }
}