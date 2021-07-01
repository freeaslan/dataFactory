package com.esign.service.dfplatform.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: huangtai
 * @Description: 缓存工具类
 * @Date: 2020/6/2 18:11
 */
public class CacheUtil {

    //默认大小
    private static final int DEFAULT_CAPACITY = 1024;

    // 最大缓存大小
    private static final int MAX_CAPACITY = 10000;

    //默认缓存过期时间
    private static final long DEFAULT_TIMEOUT = 120000;

    //存储缓存的Map
    private static final ConcurrentHashMap<String, Object> map;

    private static final Timer timer;

    static {

        map = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
        timer = new Timer();
    }

    //私有化构造方法
    private CacheUtil() {

    }

    /**
     * 缓存任务清除类
     */
    static class ClearTask extends TimerTask {

        private String key;

        public ClearTask(String key) {

            this.key = key;
        }

        @Override
        public void run() {

            CacheUtil.remove(key);
        }
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param object
     * @return
     */
    public static boolean put(String key, Object object) {

        if (checkCapacity()) {

            map.put(key, object);
            //默认缓存时间
            timer.schedule(new ClearTask(key), DEFAULT_TIMEOUT);
            return true;
        }
        return false;
    }

    /**
     * 判断容量大小
     *
     * @return
     */
    public static boolean checkCapacity() {

        return map.size() < MAX_CAPACITY;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void remove(String key) {

        map.remove(key);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {

        return map.get(key);
    }
}
