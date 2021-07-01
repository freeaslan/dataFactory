package com.esign.service.dfplatform.scenetest;

import com.esign.service.dfplatform.BO.*;
import com.esign.service.dfplatform.daointerface.DfpPublicParamDAO;
import com.esign.service.dfplatform.exception.BaseCheckException;
import com.esign.service.dfplatform.model.DfpPublicParamModel;
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
 * @Description: 场景调试
 * @author: lingxi
 * @date: 2020/11/20 14:11
 **/
public class SceneDebug {

    /**
     * @param sceneName 场景名称,测试报告中展示用
     * @param taskId    任务ID，获取请求信息用
     */
    @Test
    @Parameters({"sceneName", "taskId"})
    public void testSceneTest(@Optional("") String sceneName, @Optional("") String taskId) {

        //定义请求结果集
        List<Map<String, Object>> httpClientResponses = new ArrayList<>();

        //如果任务Id为空，则直接返回
        if (StringUtils.isEmpty(taskId)) {
            return;
        }

        try {
            //从缓存获取场景信息，如果场景为空则返回
            DfpDebugSceneBO dfpDebugSceneBO = (DfpDebugSceneBO) CacheUtil.get(taskId);
            DfpSceneBO dfpSceneBO = dfpDebugSceneBO.getDfpSceneBO();
            DfpPublicParamDAO dfpPublicParamDAO = dfpDebugSceneBO.getDfpPublicParamDAO();
            if (dfpSceneBO == null) {
                return;
            }
            //获取请求信息
            List<DfpHttpRequestBO> dfpHttpRequestBOS = dfpSceneBO.getDfpHttpRequestBOS();

            //用户保存解析response的数据
            List<DfpParamsBO> dfpParamsBOS = new ArrayList<>();
            List<DfpParamsBO> afterOtherParamsBOS = new ArrayList<>();
            for (DfpHttpRequestBO dfpHttpRequestBO : dfpHttpRequestBOS) {

                //请求中如果URL包含参数，把{xxxx}替换为{{xxxx}}
                HttpClientRequest httpClientRequestTemp = dfpHttpRequestBO.getHttpClientRequest();
                String url = httpClientRequestTemp.getUrl();
                if (url.contains("/{") && url.contains("}")) {

                    String key = StringUtils.substringBetween(url, "/{", "}");
                    url = StringUtils.replace(url, "/{" + key + "}", "/{{" + key + "}}");
                    httpClientRequestTemp.setUrl(url);
                    dfpHttpRequestBO.setHttpClientRequest(httpClientRequestTemp);
                }

                //把需要替换的参数放到一个map中
                if (dfpHttpRequestBO.getDfpParamsBOS() != null) {

                    dfpParamsBOS.addAll(dfpHttpRequestBO.getDfpParamsBOS());
                }

                //请求数据
                String apiRequest = ConvertUtil.toJson(dfpHttpRequestBO.getHttpClientRequest());
                for (DfpParamsBO dfpParamsBO : dfpParamsBOS) {

                    String actualValue = dfpParamsBO.getDefaultValue();
                    //如果是sleep类型，进行等待操作,
                    if ("public".equals(dfpParamsBO.getParamType())) {

                        DfpPublicParamModel dfpPublicParamModel = dfpPublicParamDAO.findByParamKey(dfpParamsBO.getParamKey());
                        if (dfpPublicParamModel.getAfterOtherParam() == 0) {
                            afterOtherParamsBOS.add(dfpParamsBO);
                        } else {

                            //动态加载获取数据
                            DfpPublicParamBO dfpPublicParamBO = new DfpPublicParamBO();
                            ObjectConverterUtil.convert(dfpPublicParamModel, dfpPublicParamBO);
                            dfpPublicParamBO.setActualValue(actualValue);
                            if (dfpPublicParamBO.getIsNeedBody() == 0) {

                                dfpPublicParamBO.setParamClassParamsData("String:" + apiRequest + "&" + dfpPublicParamBO.getParamClassParamsData());
                            }
                            actualValue = DynamicInvokeUtil.dynamicinvoke(dfpPublicParamBO).toString();
                        }
                    }
                    //必填参数
                    actualValue = StringUtils.isNotBlank(actualValue) ? actualValue : System.currentTimeMillis() + "";
                    apiRequest = StringUtils.replace(apiRequest, "{{" + dfpParamsBO.getParamKey() + "}}", actualValue);
                }

                //其它参数替换完在执行的参数
                for (DfpParamsBO dfpParamsBO : afterOtherParamsBOS) {
                    if ("sleep".equals(dfpParamsBO.getParamType())) {

                        //如果是sleep 则进行等待
                        long time = StringUtils.isBlank(dfpParamsBO.getDefaultValue()) ?
                                500 : Long.valueOf(dfpParamsBO.getDefaultValue());

                        Thread.sleep(time);
                    } else {

                        //动态加载获取数据
                        DfpPublicParamModel dfpPublicParamModel = dfpPublicParamDAO.findByParamKey(dfpParamsBO.getParamKey());
                        DfpPublicParamBO dfpPublicParamBO = new DfpPublicParamBO();
                        ObjectConverterUtil.convert(dfpPublicParamModel, dfpPublicParamBO);
                        dfpPublicParamBO.setActualValue(dfpParamsBO.getDefaultValue());
                        if (dfpPublicParamBO.getIsNeedBody() == 0) {

                            dfpPublicParamBO.setParamClassParamsData("String:" + apiRequest + "&" + dfpPublicParamBO.getParamClassParamsData());
                        }
                        String signValue = DynamicInvokeUtil.dynamicinvoke(dfpPublicParamBO).toString();
                        apiRequest = StringUtils.replace(apiRequest, "{{" + dfpParamsBO.getParamKey() + "}}", signValue);
                    }
                }

                //把请求String 转化为请求对象
                HttpClientRequest httpClientRequest = ConvertUtil.jsonToBean(apiRequest, HttpClientRequest.class);
                //发起请求
                HttpClientResponse httpClientResponse = HttpClientUtil.sendRequest(httpClientRequest);
                //把请求结果放到集合中
                httpClientResponses.add(ConvertUtil.jsonToMap(httpClientResponse.getResponseBody()));
                if (!"200".equals(httpClientResponse.getStateCode())) {
                    //抛出所有的请求结果信息
                    throw new BaseCheckException(httpClientResponses.toString());
                }
                //如果该请求设置了需要从结果获取数据，则提取数据
                List<DfpGetFromResponseBO> dfpGetFromResponseBOS = dfpHttpRequestBO.getDfpGetFromResponseBOS();
                for (DfpGetFromResponseBO dfpGetFromResponseBO : dfpGetFromResponseBOS) {

                    //如果没有填结果，则选择默认值
                    String jsonPath = dfpGetFromResponseBO.getParamValue();

                    //根据jsonPath表达式获取值
                    String value = JpathUtil.getValueByPath(httpClientResponse.getResponseBody().toString(), jsonPath);
                    DfpParamsBO dfpParamsBO = new DfpParamsBO();
                    dfpParamsBO.setParamKey(dfpGetFromResponseBO.getParamKey());
                    dfpParamsBO.setDefaultValue(value);
                    //把获取结果以及key放到参数列表中
                    dfpParamsBOS.add(dfpParamsBO);
                }
            }
            //把任务结果放到缓存中
            CacheUtil.put(taskId + "success", httpClientResponses);
        } catch (Exception e) {
            if (e instanceof BaseCheckException) {

                //异常信息
                CacheUtil.put(taskId + "fail", e.getMessage());
                DfplaformUtil.state(false, e.getMessage());
            } else {

                //异常信息
                CacheUtil.put(taskId + "fail", httpClientResponses + "--------" + DfplaformUtil.getExceptionDetail(e));
                DfplaformUtil.state(false, e.getMessage());
            }
        }
    }
}
