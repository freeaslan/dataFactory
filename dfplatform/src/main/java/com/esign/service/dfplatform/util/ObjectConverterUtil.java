package com.esign.service.dfplatform.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author houlandong
 * @Description: 模型转换器
 * @Date: 2020/9/22 19:42
 */
@Slf4j
public class ObjectConverterUtil {

    private static final Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    private static final Converter converter = new CglibConverter();

    /**
     * 把source对象属性值赋值给target对象
     *
     * @param source
     * @param target
     */
    public static <T, E> void convert(T source, E target) {

        if (source == null || target == null) {
            return;
        }

        log.info("进行模型转换");
        String key = source.getClass() + " " + target.getClass();
        BeanCopier beanCopier;
        if (beanCopierMap.get(key) == null) {

            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
            beanCopierMap.put(key, beanCopier);
        } else {

            beanCopier = beanCopierMap.get(key);
        }

        beanCopier.copy(source, target, converter);
    }

    /**
     * 数组对象转换
     *
     * @param targetClass
     * @param sources
     * @param targets
     * @param <T>
     * @param <E>
     */
    public static <T, E extends Object> void convertList(Collection<T> sources, Collection<E> targets,
                                                         Class targetClass) {

        // 参数校验
        if (targetClass == null || CollectionUtils.isEmpty(sources) || targets == null) {

            return;
        }

        try {
            for (T source : sources) {

                E target = (E) targetClass.newInstance();
                convert(source, target);
                targets.add(target);
            }
        } catch (Exception e) {

            log.error("转化出现异常", e);
        }
    }
}
