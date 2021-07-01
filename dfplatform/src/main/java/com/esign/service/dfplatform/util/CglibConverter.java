package com.esign.service.dfplatform.util;

import net.sf.cglib.core.Converter;

/**
 * @author huangtai
 * @Description: 类转换器
 * @Date: 2020/6/2 18:11
 */
public class CglibConverter implements Converter {

    /**
     * 类转换器
     *
     * @param value
     * @param target
     * @param context
     * @return
     */
    @SuppressWarnings({"rawtypes", "unused"})
    @Override
    public Object convert(Object value, Class target, Object context) {

        if (value == null) {
            return null;
        }

        Class targetClass = target;
        return value;
    }
}
