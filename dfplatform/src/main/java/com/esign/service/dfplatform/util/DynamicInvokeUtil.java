package com.esign.service.dfplatform.util;

import com.esign.service.dfplatform.BO.DfpPublicParamBO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: yunge
 * @Description: 动态加载服务类
 * @Date: 2021/3/1 16:19
 */
@Slf4j
public class DynamicInvokeUtil {


    /**
     * 动态加载
     *
     * @param dfpPublicParamBO
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public static Object dynamicinvoke(DfpPublicParamBO dfpPublicParamBO) {

        if (dfpPublicParamBO != null) {

            //获取参数
            String paramClassType = dfpPublicParamBO.getParamClassType();
            String paramClassPath = dfpPublicParamBO.getParamClassPath();
            String paramClassMethod = dfpPublicParamBO.getParamClassMethod();
            String jarName = dfpPublicParamBO.getJarName();
            //获取实际结果
            String[] actualValues = {};
            if (StringUtils.isNotBlank(dfpPublicParamBO.getActualValue())) {
                actualValues = StringUtils.split(dfpPublicParamBO.getActualValue(), "&");
            }

            //如果方法参数不为空，则解析
            String paramClassParamsData = dfpPublicParamBO.getParamClassParamsData();
            List<Object> invokedata = new ArrayList<>();
            Class<?>[] classTypes = {};
            if (StringUtils.isNotBlank(paramClassParamsData)) {

                //String:20000101&String:20000101格式命名参数
                String[] paramsDatas = StringUtils.split(paramClassParamsData, "&");
                //设置数组长度
                classTypes = new Class[paramsDatas.length];
                for (int i = 0; i < paramsDatas.length; i++) {

                    String paramData = paramsDatas[i];
                    paramData = StringUtils.replace(paramData, "：", ":");
                    String[] param = StringUtils.split(paramData, ":");
                    DfplaformUtil.state(param.length == 2, "公共参数参数配置需要两要素，比如：String:310000");
                    //设置为默认值
                    String paramKeyValueAnother = param[1];
                    if (actualValues.length > i) {

                        //如果有实际值则使用实际值
                        paramKeyValueAnother = actualValues[i];
                    }
                    getinvokedata(param[0], paramKeyValueAnother, invokedata);
                    classTypes[i] = getclasstype(param[0]);
                }
            }

            //获取路径地址
            File directory = new File("");
            String path = directory.getCanonicalPath() + "/src/main/uploadfile/";
            if ("jar".equals(paramClassType)) {
                path = path + jarName;
            } else if ("class".equals(paramClassType)) {
                //如果加载的是class 需要按照paramClassPath创建文件
                mkdirPath(paramClassPath, path, jarName);
            }

            //获取到类实例
            Class aClass = myclassLoader(path, paramClassPath);
            Object object = aClass.newInstance();

            //获取方法
            log.info("方法名：" + paramClassMethod + "----参数类型：" + classTypes);
            Method methodAnother = aClass.getMethod(paramClassMethod, classTypes);
            //获取结果并返回
            Object result = methodAnother.invoke(object, invokedata.toArray());
            log.info("动态获取结果：" + result);
            return result;
        }

        return null;
    }

    /**
     * 类加载器
     *
     * @param path
     * @param paramClassPath
     * @return
     */
    @SneakyThrows
    private static Class myclassLoader(String path, String paramClassPath) {

        log.info("路径：" + path + "----类路径：" + paramClassPath);
        URL url = new URL("file:" + path);
        ClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        add.setAccessible(true);
        add.invoke(classLoader, url);
        return classLoader.loadClass(paramClassPath);
    }

    /**
     * 关键词类型
     *
     * @param paramKeyType
     * @return
     */
    private static Class<?> getclasstype(String paramKeyType) {

        Class<String> classtypestring = String.class;
        Class<Integer> classtypeint = int.class;
        Class<Boolean> classtypeboolean = boolean.class;
        Class<Date> classtypedate = Date.class;
        switch (paramKeyType) {
            case "String":
                return classtypestring;
            case "int":
                return classtypeint;
            case "boolean":
                return classtypeboolean;
            case "Date":
                return classtypedate;
            default:
                return classtypestring;
        }
    }

    /**
     * 转化为实际值
     *
     * @param paramKeyType
     * @param paramKeyValue
     * @param arrayList
     * @return
     */
    private static Object[] getinvokedata(String paramKeyType, String paramKeyValue, List arrayList) {

        switch (paramKeyType) {
            case "String":

                String Stringdata = paramKeyValue;
                arrayList.add(Stringdata);
                break;
            case "int":

                int intdata = Integer.parseInt(paramKeyValue);
                arrayList.add(intdata);
                break;
            case "boolean":

                boolean booleandata = Boolean.parseBoolean(paramKeyValue);
                arrayList.add(booleandata);
                break;
            default:

                arrayList.add(paramKeyValue);
                break;
        }
        return arrayList.toArray();
    }


    /**
     * 创建文件夹并拷贝文件
     *
     * @param paramClassPath
     * @param basePath
     * @param jarName
     */
    private static void mkdirPath(String paramClassPath, String basePath, String jarName) {

        String path = basePath;
        //分割需要的目录名称
        String[] paths = StringUtils.split(paramClassPath, ".");
        for (int i = 0; i < paths.length - 1; i++) {
            path = path + paths[i] + "/";
            File file = new File(path);
            //如果目录不存在则新建
            if (!file.exists()) {
                file.mkdir();
            }
        }
        //复制文件
        FileUtil.copyFile(basePath + jarName, path + jarName);
    }
}
