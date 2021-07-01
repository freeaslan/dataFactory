package com.esign.service.dfplatform.scenetest;

import com.esign.service.dfplatform.BO.DfpPublicParamBO;
import com.esign.service.dfplatform.BO.DfpRequestBO;
import com.esign.service.dfplatform.BO.DfpRequestBaseBO;
import com.esign.service.dfplatform.BO.DfpRequestHttpParamBO;
import com.esign.service.dfplatform.exception.BaseCheckException;
import com.esign.service.dfplatform.model.DfpSceneParamsModel;
import com.esign.service.dfplatform.util.*;
import com.esign.service.dfplatform.util.client.HttpClientRequest;
import com.esign.service.dfplatform.util.client.HttpClientResponse;
import com.esign.service.dfplatform.util.client.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.*;

/**
 * @Author: huangtai
 * @Description: 请求场景  设置测试类
 * @Date: 2020/9/24 17:00
 */
public class SceneTest {

    /**
     * @param sceneName 场景名称,测试报告中展示用
     * @param taskId    任务ID，获取请求信息用
     */
    @Test
    @Parameters({"sceneName", "taskId"})
    public void testSceneTest(@Optional("") String sceneName, @Optional("") String taskId) {

        //定义请求结果集
        List<Map<String, Object>> httpClientResponses = new ArrayList<>();
        //定义需要替换的参数
        Map<String, String> replaceParam = new HashMap<>();

        //如果任务Id为空，则直接返回
        if (StringUtils.isEmpty(taskId)) {
            return;
        }

        try {
            //从缓存获取任务信息，如果为空则直接返回
            DfpRequestBO dfpRequestBO = (DfpRequestBO) CacheUtil.get(taskId);
            if (dfpRequestBO == null) {
                return;
            }

            //获取所有请求
            List<DfpRequestBaseBO> apiRequests = dfpRequestBO.getApiRequests();
            for (DfpRequestBaseBO apiRequest : apiRequests) {

                //请求字符串
                String apiRequestTemp = apiRequest.getApiRequest();
                for (String key : replaceParam.keySet()) {

                    //替换掉请求中参数值。{{key}}
                    apiRequestTemp = StringUtils.replace(apiRequestTemp, "{{" + key + "}}", replaceParam.get(key));
                }

                //获取请求参数信息
                List<DfpRequestHttpParamBO> dfpRequestHttpParamBOS = apiRequest.getDfpSceneParamsModels();
                List<DfpSceneParamsModel> responseSceneParamsModels = new ArrayList<>();
                for (DfpRequestHttpParamBO dfpRequestHttpParamBO : dfpRequestHttpParamBOS) {

                    DfpSceneParamsModel dfpSceneParamsModel = dfpRequestHttpParamBO.getDfpSceneParamsModel();
                    DfpPublicParamBO dfpPublicParamBO = dfpRequestHttpParamBO.getDfpPublicParamBO();
                    if (dfpSceneParamsModel != null) {
                        if ("sleep".equals(dfpSceneParamsModel.getParamKey())) {

                            //如果是sleep 则进行等待
                            long time = StringUtils.isBlank(dfpSceneParamsModel.getDefaultValue()) ?
                                    500 : Long.valueOf(dfpSceneParamsModel.getDefaultValue());

                            Thread.sleep(time);
                        } else if ("public".equals(dfpSceneParamsModel.getParamType())) {

                            //动态加载
                            if (dfpPublicParamBO.getIsNeedBody() == 0) {

                                dfpPublicParamBO.setParamClassParamsData("String:" + apiRequestTemp + "&" + dfpPublicParamBO.getParamClassParamsData());
                            }
                            String signValue = DynamicInvokeUtil.dynamicinvoke(dfpPublicParamBO).toString();
                            apiRequestTemp = StringUtils.replace(apiRequestTemp, "{{" + dfpSceneParamsModel.getParamKey() + "}}", signValue);
                        } else {

                            responseSceneParamsModels.add(dfpSceneParamsModel);
                        }
                    }
                }

                //把请求String 转化为请求对象
                HttpClientRequest httpClientRequest = ConvertUtil.jsonToBean(apiRequestTemp, HttpClientRequest.class);
                //发起请求
                HttpClientResponse httpClientResponse = HttpClientUtil.sendRequest(httpClientRequest);
                //把请求结果放到集合中
                httpClientResponses.add(ConvertUtil.jsonToMap(httpClientResponse.getResponseBody()));
                if (!"200".equals(httpClientResponse.getStateCode())) {
                    //抛出所有的请求结果信息
                    throw new BaseCheckException(httpClientResponses.toString());
                }
                //如果该请求设置了需要从结果获取数据，则提取数据
                for (DfpSceneParamsModel dfpSceneParamsModel : responseSceneParamsModels) {

                    //把key和解析的结果放到需要替换的map中
                    replaceParam.put(dfpSceneParamsModel.getParamKey(),
                            JpathUtil.getValueByPath(httpClientResponse.getResponseBody().toString(), dfpSceneParamsModel.getDefaultValue()));
                }
            }

            //把任务结果放到缓存中
            CacheUtil.put(taskId + "success", httpClientResponses);
        } catch (
                Exception e)

        {

            if (e instanceof BaseCheckException) {
                //异常信息
                CacheUtil.put(taskId + "fail", e.getMessage());
                DfplaformUtil.state(false, e.getMessage());
            } else {

                //异常信息
                CacheUtil.put(taskId + "fail", httpClientResponses +
                        "--------" + DfplaformUtil.getExceptionDetail(e));
                DfplaformUtil.state(false, e.getMessage());
            }
        }
    }
}
