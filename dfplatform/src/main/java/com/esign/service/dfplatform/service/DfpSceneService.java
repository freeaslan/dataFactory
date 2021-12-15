package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.*;
import com.esign.service.dfplatform.VO.*;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.*;
import com.esign.service.dfplatform.model.*;
import com.esign.service.dfplatform.util.*;
import com.esign.service.dfplatform.util.client.HttpClientRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: huangtai
 * @Description: 场景操作服务
 * @Date: 2020/9/15 13:54
 */
@Service
@Slf4j
public class DfpSceneService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpSceneDAO dfpSceneDAO;

    @Autowired
    DfpModuleDAO dfpModuleDAO;

    @Autowired
    DfpSceneRequestDAO dfpSceneRequestDAO;

    @Autowired
    DfpSceneParamsDAO dfpSceneParamsDAO;

    @Autowired
    DfpCustomApiDAO dfpCustomApiDAO;

    @Value("${httpdPort}")
    String httpdPort;

    @Value("${httpDocumentRoot}")
    String httpDocumentRoot;

    @Autowired
    DfpEnvParamsDAO dfpEnvParamsDAO;

    @Autowired
    DfpApisDAO dfpApisDAO;

    @Autowired
    DfpUserDAO dfpUserDAO;

    @Autowired
    DfpPublicParamDAO dfpPublicParamDAO;

    /**
     * 获取场景列表
     *
     * @param dfpSceneListBO
     * @return
     */
    public DfplatformResult<DfpSceneListVO> getSceneList(DfpSceneListBO dfpSceneListBO) {

        //变量定义
        DfplatformResult<DfpSceneListVO> defenderResult = new DfplatformResult<>();
        DfpSceneListVO dfpSceneListVO = new DfpSceneListVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpSceneListBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

                dfpSceneListVO.setPageIndex(dfpSceneListBO.getPageIndex());
                dfpSceneListVO.setPageSize(dfpSceneListBO.getPageSize());
            }

            @Override
            public void doOperate() throws Exception {

                //获取分页数据
                Page<DfpSceneModel> dfpSceneModelPage = getSceneByPage(dfpSceneListBO);
                if (dfpSceneModelPage != null) {

                    //设置数据总数
                    dfpSceneListVO.setTotalNum(dfpSceneModelPage.getTotalElements());

                    List<DfpSceneVO> dfpSceneVOS = new ArrayList<>();
                    List<DfpSceneModel> dfpSceneModels = dfpSceneModelPage.getContent();
                    for (DfpSceneModel dfpSceneModel : dfpSceneModels) {

                        //设置场景所属模块信息
                        dfpSceneVOS.add(convertSceneModelToVO(dfpSceneModel, new DfpSceneVO()));
                    }

                    dfpSceneListVO.setDfpSceneVOS(dfpSceneVOS);
                    defenderResult.setData(dfpSceneListVO);
                }
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取场景所需要的参数列表
     *
     * @param sceneId
     * @return
     */
    public DfplatformResult<String> getSceneParams(int sceneId) {

        //定义返回为swagger格式
        DfplatformResult<String> defenderResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(sceneId > 0, "场景id不存在");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //获取场景所需要的所有参数
                List<DfpSceneParamsModel> dfpSceneParamsModels =
                        dfpSceneParamsDAO.findBySceneIdAndParamTypeNot(sceneId, "response");

                //获取必填参数以及属性
                List<String> required = new ArrayList<>();
                JSONObject property = new JSONObject();
                for (DfpSceneParamsModel dfpSceneParamsModel : dfpSceneParamsModels) {

                    DfpParamsVO dfpParamsBO = new DfpParamsVO();
                    ObjectConverterUtil.convert(dfpSceneParamsModel, dfpParamsBO);
                    dfpParamsBO.setParamType("string");
                    property.put(dfpParamsBO.getParamKey(), ConvertUtil.toMap(dfpParamsBO));
                    required.add(dfpParamsBO.getParamKey());
                }

                //组装json
                JSONObject properties = new JSONObject();
                properties.put("properties", property);
                properties.put("required", required);

                DfpSceneModel dfpSceneModel = dfpSceneDAO.findById(sceneId);
                JSONObject definition = new JSONObject();
                definition.put(dfpSceneModel.getSceneName(), properties);

                JSONObject info = new JSONObject();
                info.put("description", "powered by Flasgger");
                info.put("termsOfservice", "/tos");
                info.put("title", "a swagger api");
                info.put("version", "0.0.1");

                JSONObject schema = new JSONObject();
                schema.put("$ref", "#/definitions/" + dfpSceneModel.getSceneName());

                JSONObject parameter = new JSONObject();
                parameter.put("in", "body");
                parameter.put("name", "body");
                parameter.put("required", "false");
                parameter.put("schema", schema);
                JSONArray parameters = new JSONArray();
                parameters.add(parameter);

                JSONObject example = new JSONObject();
                example.put("code", 0);
                example.put("message", "创建成功");

                JSONObject status = new JSONObject();
                status.put("description", dfpSceneModel.getSceneName());
                status.put("example", example);

                JSONObject response = new JSONObject();
                response.put("200", status);

                JSONObject type = new JSONObject();
                type.put("description", dfpSceneModel.getSceneName());
                type.put("parameters", parameters);
                type.put("responses", response);
                type.put("summary", dfpSceneModel.getSceneName());
                type.put("tags", new ArrayList<>(Arrays.asList("test")));

                JSONObject apiType = new JSONObject();
                apiType.put("post", type);
                JSONObject path = new JSONObject();
                path.put("url", apiType);

                JSONObject definitions = new JSONObject();
                definitions.put("definitions", definition);
                definitions.put("info", info);
                definitions.put("swagger", "2.0");
                definitions.put("paths", path);
                log.info(definitions.toString());
                defenderResult.setData(definitions.toString());
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 请求创景
     *
     * @param dfpQuestSceneBO
     * @return
     */
    public DfplatformResult<DfpRequestSceneVO> requestScene(DfpQuestSceneBO dfpQuestSceneBO) {

        //定义变量
        DfplatformResult<DfpRequestSceneVO> defenderResult = new DfplatformResult<>();
        DfpRequestSceneVO dfpRequestSceneVO = new DfpRequestSceneVO();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpQuestSceneBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                List<DfpRequestBaseBO> apiRequests = new ArrayList<>();

                //获取场景请求数据
                List<DfpSceneRequestModel> dfpSceneRequestModels =
                        dfpSceneRequestDAO.findBySceneIdOrderByApiOrderAsc(dfpQuestSceneBO.getSceneId());

                //获取场景所有变量
                List<DfpSceneParamsModel> dfpParamsBOS =
                        dfpSceneParamsDAO.findBySceneIdAndParamTypeNot(dfpQuestSceneBO.getSceneId(), "response");
                DfpSceneModel dfpSceneModel = dfpSceneDAO.findById(dfpQuestSceneBO.getSceneId());

                //处理场景请求数据
                for (DfpSceneRequestModel dfpSceneRequestModel : dfpSceneRequestModels) {

                    DfpRequestBaseBO dfpRequestBaseBO = new DfpRequestBaseBO();
                    //请求数据
                    String apiRequest = dfpSceneRequestModel.getApiRequest();

                    //请求中如果URL包含参数，把{xxxx}替换为{{xxxx}}
                    HttpClientRequest httpClientRequestTemp = ConvertUtil.jsonToBean(apiRequest, HttpClientRequest.class);
                    String url = httpClientRequestTemp.getUrl();
                    if (url.contains("/{") && url.contains("}")) {
                        String key = StringUtils.substringBetween(url, "/{", "}");
                        url = StringUtils.replace(url, "/{" + key + "}", "/{{" + key + "}}");
                        httpClientRequestTemp.setUrl(url);
                        apiRequest = ConvertUtil.toJson(httpClientRequestTemp);
                    }

                    if (!"自定义接口".equals(dfpSceneRequestModel.getRequestOtherName())) {

                        //根据Api获取服务名称
                        DfpApisModel dfpApisParams = dfpApisDAO.findById(dfpSceneRequestModel.getApiId());
                        String serviceName = dfpApisParams.getServiceName();

                        //获取环境变量信息
                        DfpEnvParamsModel dfpEnvParamsModel =
                                dfpEnvParamsDAO.findByEnvIdAndProjectNameAndServiceName(dfpQuestSceneBO.getEnvId(),
                                        dfpQuestSceneBO.getProjectName(), serviceName);
                        DfplaformUtil.isNotNull(dfpEnvParamsModel, "没有配置环境信息，请先配置相关环境参数！");

                        //host替换
                        String requestHost = dfpEnvParamsModel.getHost();
                        httpClientRequestTemp.setHost(requestHost);

                        //环境设置的header
                        String requestHeader = dfpEnvParamsModel.getHeader();
                        Map<String, Object> envHeader = ConvertUtil.jsonToMap(requestHeader);
                        //场景设置header
                        Map<String, String> header = httpClientRequestTemp.getHeaders();
                        //取并集,环境header和场景header都有以环境header为准；环境header没有，场景header有则以场景header为准
                        if (!header.isEmpty()) {
                            for (Map.Entry<String, String> entry : header.entrySet()) {
                                if (!envHeader.containsKey(entry.getKey())) {

                                    envHeader.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                        Map<String, String> resultHeader = new HashMap<String, String>();
                        for (Map.Entry<String, Object> entry : envHeader.entrySet()) {

                            resultHeader.put(entry.getKey(), entry.getValue().toString());
                        }

                        //重新设置header，设置对应环境需要的header
                        httpClientRequestTemp.setHeaders(resultHeader);
                        apiRequest = ConvertUtil.toJson(httpClientRequestTemp);
                    }

                    //页面提交的变量数据
                    List<DfpRequestParamsBO> dfpRequestParamsBOS = dfpQuestSceneBO.getDfpRequestParamsBOs();
                    Map<String, String> result = new HashMap<>();
                    if (dfpRequestParamsBOS != null) {

                        //把list对象变为map
                        result = dfpRequestParamsBOS.stream().collect(Collectors.toMap(DfpRequestParamsBO::getParamKey,
                                DfpRequestParamsBO::getParamValue));
                    }

                    //循环参数
                    List<DfpRequestHttpParamBO> dfpRequestHttpParamBOS = new ArrayList<>();
                    for (DfpSceneParamsModel dfpParamsBO : dfpParamsBOS) {

                        //获取页面提交的数据作为实际值
                        String actualValue = result.get(dfpParamsBO.getParamKey());
                        if ("public".equals(dfpParamsBO.getParamType())) {

                            //动态获取数据
                            DfpPublicParamModel dfpPublicParamModel = dfpPublicParamDAO.findByParamKey(dfpParamsBO.getParamKey());
                            DfpPublicParamBO dfpPublicParamBO = new DfpPublicParamBO();
                            ObjectConverterUtil.convert(dfpPublicParamModel, dfpPublicParamBO);
                            String value = StringUtils.isNotBlank(actualValue) ? actualValue : dfpParamsBO.getDefaultValue();
                            dfpPublicParamBO.setActualValue(value);

                            //如果是需要在其它参数执行完后再执行，则放到afterOtherParamModel集合中
                            if (dfpPublicParamModel.getAfterOtherParam() == 0) {

                                DfpRequestHttpParamBO dfpRequestHttpParamBO = new DfpRequestHttpParamBO();
                                dfpRequestHttpParamBO.setDfpSceneParamsModel(dfpParamsBO);
                                dfpRequestHttpParamBO.setDfpPublicParamBO(dfpPublicParamBO);
                                dfpRequestHttpParamBOS.add(dfpRequestHttpParamBO);
                            } else {

                                if (dfpPublicParamBO.getIsNeedBody() == 0) {

                                    dfpPublicParamBO.setParamClassParamsData("String:" + apiRequest + "&" + dfpPublicParamBO.getParamClassParamsData());
                                }
                                actualValue = DynamicInvokeUtil.dynamicinvoke(dfpPublicParamBO).toString();
                            }
                        }

                        String value = StringUtils.isNotEmpty(actualValue) ? actualValue : dfpParamsBO.getDefaultValue();
                        value = StringUtils.isNotBlank(value) ? value : System.currentTimeMillis() + "";
                        apiRequest = StringUtils.replace(apiRequest, "{{" + dfpParamsBO.getParamKey() + "}}", value);
                    }

                    //把替换后的apiRequest设置进对象
                    dfpRequestBaseBO.setApiRequest(apiRequest);

                    //获取需要从response获取数据的参数
                    List<DfpSceneParamsModel> dfpSceneParamsModelList = dfpSceneParamsDAO.
                            findBySceneIdAndApiIdAndParamType(dfpQuestSceneBO.getSceneId(), dfpSceneRequestModel.getApiId(), "response");
                    for (DfpSceneParamsModel dfpSceneParamsModel : dfpSceneParamsModelList) {

                        DfpRequestHttpParamBO dfpRequestHttpParamBO = new DfpRequestHttpParamBO();
                        dfpRequestHttpParamBO.setDfpSceneParamsModel(dfpSceneParamsModel);
                        dfpRequestHttpParamBOS.add(dfpRequestHttpParamBO);
                    }

                    dfpRequestBaseBO.setDfpSceneParamsModels(dfpRequestHttpParamBOS);
                    //参数替换后的请求数据
                    apiRequests.add(dfpRequestBaseBO);
                }

                //把请求信息放到缓存中
                DfpRequestBO dfpRequestBO = new DfpRequestBO();
                dfpRequestBO.setSceneId(dfpQuestSceneBO.getSceneId());
                dfpRequestBO.setApiRequests(apiRequests);
                String uuid = DfplaformUtil.getUUID();
                CacheUtil.put(uuid, dfpRequestBO);
                dfpRequestSceneVO.setTaskId(uuid);

                //创建testng.xml
                TestngUtil.createTestngXml(uuid, dfpSceneModel.getSceneName(),
                        "com.esign.service.dfplatform.scenetest.SceneTest");
                //执行testng.xml
                TestngUtil.executeTestngXml(uuid);
                //执行结果处理
                resultProcess(uuid, defenderResult, dfpRequestSceneVO);
                //测试报告处理
                reportProcess(uuid, dfpSceneModel.getSceneName(), defenderResult, dfpRequestSceneVO);
            }
        }, defenderResult);

        return defenderResult;
    }


    /**
     * 调试场景
     *
     * @param dfpSceneBO
     * @return
     */
    public DfplatformResult<DfpRequestSceneVO> debugSecne(DfpSceneBO dfpSceneBO) {

        //定义变量
        DfplatformResult<DfpRequestSceneVO> defenderResult = new DfplatformResult<>();
        DfpRequestSceneVO dfpRequestSceneVO = new DfpRequestSceneVO();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpSceneBO, "请求对象不能为空");
                DfplaformUtil.isNotBlank(dfpSceneBO.getDfpHttpRequestBOS(), "请求数据不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //把请求信息放到缓存中
                String debugXmlName = dfpSceneBO.getSceneName();
                String uuid = DfplaformUtil.getUUID() + "debug";
                //组合对象
                DfpDebugSceneBO dfpDebugSceneBO = new DfpDebugSceneBO();
                dfpDebugSceneBO.setDfpSceneBO(dfpSceneBO);
                dfpDebugSceneBO.setDfpPublicParamDAO(dfpPublicParamDAO);
                CacheUtil.put(uuid, dfpDebugSceneBO);
                dfpRequestSceneVO.setTaskId(uuid);

                //创建testng.xml
                TestngUtil.createTestngXml(uuid, debugXmlName + "debug",
                        "com.esign.service.dfplatform.scenetest.SceneDebug");
                //运行testng.xml
                TestngUtil.executeTestngXml(uuid);
                //执行结果处理
                resultProcess(uuid, defenderResult, dfpRequestSceneVO);
                //测试报告处理
                reportProcess(uuid, debugXmlName + "debug", defenderResult, dfpRequestSceneVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 执行testng文件
     *
     * @param uuid
     * @param defaultResult
     * @param dfpRequestSceneVO
     * @throws Exception
     */
    private void resultProcess(String uuid, DfplatformResult<DfpRequestSceneVO> defaultResult,
                               DfpRequestSceneVO dfpRequestSceneVO) throws Exception {

        //循环获取结果，三分钟内每500毫秒获取一次结果
        Long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 180000) {

            //如果异常信息不为空，则说明异常中断，返回异常错误信息
            if (CacheUtil.get(uuid + "fail") != null) {

                String errorResult = (String) CacheUtil.get(uuid + "fail");
                List<Map<String, Object>> error = new ArrayList<>();
                Map<String, Object> message = new HashMap<>();
                message.put("failMessage", errorResult);
                error.add(message);
                dfpRequestSceneVO.setResponses(error);
                defaultResult.setCode(-1);
                break;
            }

            //如果执行成功，则返回结果详细信息
            if (CacheUtil.get(uuid + "success") != null) {

                //正常返回信息
                dfpRequestSceneVO.setResponses((List<Map<String, Object>>) CacheUtil.get(uuid + "success"));
                break;
            }

            Thread.sleep(500);
        }
    }

    /**
     * 测试报告处理
     *
     * @param uuid
     * @param xmlName
     * @param defaultResult
     * @param dfpRequestSceneVO
     */
    private void reportProcess(String uuid, String xmlName, DfplatformResult<DfpRequestSceneVO> defaultResult,
                               DfpRequestSceneVO dfpRequestSceneVO) {

        //移动报告文件到html下面，可以使用httpd方式浏览器访问
        if (defaultResult.code == 0) {

            String path = new File("report").getAbsolutePath() + "/" + xmlName + "/" + uuid + ".html";
            FileUtil.copyFile(path, httpDocumentRoot + uuid + ".html");
            dfpRequestSceneVO.setReportUrl(DfplaformUtil.getInet4Address() + httpdPort + "/" + uuid + ".html");
        }

        FileUtil.deleteFile(uuid + ".xml");
        //获取结果
        defaultResult.setData(dfpRequestSceneVO);
    }

    /**
     * 创建编辑场景
     *
     * @param dfpSceneBO
     * @param isEdit
     * @return
     */
    public DfplatformResult<Integer> addOrEditSecne(DfpSceneBO dfpSceneBO, boolean isEdit) {

        //定义变量
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpSceneModel dfpSceneModel = new DfpSceneModel();

            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpSceneBO, "请求对象不能为空");
                DfplaformUtil.isNotBlank(dfpSceneBO.getDfpHttpRequestBOS(), "请求数据不能为空");
                if (isEdit) {

                    DfplaformUtil.state(dfpSceneBO.getSceneId() > 0, "编辑时场景ID大于零");
                    dfpSceneModel = dfpSceneDAO.findById(dfpSceneBO.getSceneId());
                    DfplaformUtil.isNotNull(dfpSceneModel, "编辑的场景不存在");
                }
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {
                if (isEdit) {
                    //编辑时删除数据重新添加
                    int sceneId = dfpSceneBO.getSceneId();
                    dfpSceneParamsDAO.deleteBySceneId(sceneId);
                    dfpSceneRequestDAO.deleteBySceneId(sceneId);
                    dfpCustomApiDAO.deleteBySceneId(sceneId);
                } else {
                    dfpSceneModel.setCreatorId(dfpSceneBO.getUserId());
                }
                dfpSceneModel.setModifierId(dfpSceneBO.getUserId());
                //保存场景表数据
                ObjectConverterUtil.convert(dfpSceneBO, dfpSceneModel);
                dfpSceneModel.setId(dfpSceneBO.getSceneId());
                dfpSceneModel.setModuleId(dfpSceneBO.getModuleId());
                dfpSceneModel = dfpSceneDAO.save(dfpSceneModel);

                //获取请求数据
                List<DfpHttpRequestBO> dfpHttpRequestBOS = dfpSceneBO.getDfpHttpRequestBOS();
                for (DfpHttpRequestBO dfpHttpRequestBO : dfpHttpRequestBOS) {

                    //设置场景请求信息
                    DfpSceneRequestModel dfpSceneRequestModel = new DfpSceneRequestModel();
                    ObjectConverterUtil.convert(dfpHttpRequestBO, dfpSceneRequestModel);
                    dfpSceneRequestModel.setSceneId(dfpSceneModel.getId());
                    dfpSceneRequestModel.setApiRequest(ConvertUtil.toJson(dfpHttpRequestBO.getHttpClientRequest()));

                    if (dfpHttpRequestBO.getApiId() == 0) {

                        //自定义请求，先保存自定基本信息到自定义api表中
                        DfpCustomApiModel dfpCustomApiModel = new DfpCustomApiModel();
                        dfpCustomApiModel.setProjectName(dfpSceneModel.getProjectName());
                        dfpCustomApiModel.setSceneId(dfpSceneModel.getId());
                        HttpClientRequest httpClientRequest = dfpHttpRequestBO.getHttpClientRequest();
                        dfpCustomApiModel.setUrl(httpClientRequest.getUrl());
                        dfpCustomApiModel.setHost(StringUtils.substringBetween(httpClientRequest.getUrl(), "://", "/"));
                        dfpCustomApiModel.setMethod(httpClientRequest.getType());
                        dfpCustomApiModel = dfpCustomApiDAO.save(dfpCustomApiModel);
                        dfpSceneRequestModel.setApiId(dfpCustomApiModel.getId());
                    }

                    //保存场景请求信息
                    dfpSceneRequestDAO.save(dfpSceneRequestModel);

                    //如果请求设置了可变参数，则保存参数信息到参数表
                    List<DfpParamsBO> dfpParamsBOS = dfpHttpRequestBO.getDfpParamsBOS();
                    for (DfpParamsBO dfpParamsBO : dfpParamsBOS) {

                        //设置场景参数
                        DfpSceneParamsModel dfpSceneParamsModel = new DfpSceneParamsModel();
                        ObjectConverterUtil.convert(dfpParamsBO, dfpSceneParamsModel);
                        //public表示公共参数，其它都表示普通
                        if ("public".equals(dfpParamsBO.getParamType())) {
                            dfpSceneParamsModel.setParamType("public");
                        } else {
                            dfpSceneParamsModel.setParamType("normal");
                        }
                        dfpSceneParamsModel.setApiId(dfpSceneRequestModel.getApiId());
                        dfpSceneParamsModel.setSceneId(dfpSceneModel.getId());
                        dfpSceneParamsDAO.save(dfpSceneParamsModel);
                    }

                    //如果请求设置从response获取数据,保存数据到参数表
                    List<DfpGetFromResponseBO> dfpGetFromResponseBOS = dfpHttpRequestBO.getDfpGetFromResponseBOS();
                    for (DfpGetFromResponseBO dfpGetFromResponseBO : dfpGetFromResponseBOS) {

                        DfpSceneParamsModel dfpSceneParamsModel = new DfpSceneParamsModel();
                        dfpSceneParamsModel.setParamKey(dfpGetFromResponseBO.getParamKey());
                        dfpSceneParamsModel.setDefaultValue(dfpGetFromResponseBO.getParamValue());
                        dfpSceneParamsModel.setParamType("response");
                        dfpSceneParamsModel.setDescription("dfpGetFromResponseBO.getParamKey()从response获取值");
                        dfpSceneParamsModel.setApiId(dfpSceneRequestModel.getApiId());
                        dfpSceneParamsModel.setSceneId(dfpSceneModel.getId());
                        dfpSceneParamsDAO.save(dfpSceneParamsModel);
                    }
                }

                defenderResult.setData(dfpSceneModel.getId());
                defenderResult.setMessage(isEdit ? "编辑动态参数成功" : "新增动态参数成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 删除场景
     *
     * @param secneId
     * @return
     */
    public DfplatformResult<Integer> deleteScene(int secneId) {

        //定义返回结果
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpSceneModel dfpSceneModel = new DfpSceneModel();

            @Override
            public void doCheck() {

                DfplaformUtil.state(secneId > 0, "场景ID必须大于零");
                dfpSceneModel = dfpSceneDAO.findById(secneId);
                DfplaformUtil.isNotNull(dfpSceneModel, "要删除的场景不存在");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //直接物理删除
                dfpSceneDAO.deleteById(secneId);
                dfpSceneParamsDAO.deleteBySceneId(secneId);
                dfpSceneRequestDAO.deleteBySceneId(secneId);
                dfpCustomApiDAO.deleteBySceneId(secneId);
                defenderResult.setData(secneId);
                defenderResult.setMessage("删除场景成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取场景信息
     *
     * @param sceneId
     * @return
     */
    public DfplatformResult<DfpSceneRequestVO> queryScene(int sceneId) {

        //定义结果
        DfplatformResult<DfpSceneRequestVO> defenderResult = new DfplatformResult<>();
        DfpSceneRequestVO dfpSceneRequestVO = new DfpSceneRequestVO();
        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {

            @Override
            public void doCheck() {

                DfplaformUtil.state(sceneId > 0, "场景ID必须大于零");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                DfpSceneModel dfpSceneModel = dfpSceneDAO.findById(sceneId);
                DfplaformUtil.isNotNull(dfpSceneModel, "查询的场景不存在");

                //设置场景信息
                ObjectConverterUtil.convert(dfpSceneModel, dfpSceneRequestVO);
                dfpSceneRequestVO.setSceneId(dfpSceneModel.getId());

                //查询请求信息
                List<DfpHttpRequestBO> dfpHttpRequestBOS = new ArrayList<>();
                List<DfpSceneRequestModel> sceneRequestModels = dfpSceneRequestDAO.findBySceneIdOrderByApiOrderAsc(sceneId);
                for (DfpSceneRequestModel sceneRequestModel : sceneRequestModels) {

                    //设置请求信息
                    DfpHttpRequestBO dfpHttpRequestBO = new DfpHttpRequestBO();
                    ObjectConverterUtil.convert(sceneRequestModel, dfpHttpRequestBO);
                    HttpClientRequest httpClientRequest = ConvertUtil.jsonToBean(sceneRequestModel.getApiRequest(), HttpClientRequest.class);
                    dfpHttpRequestBO.setHttpClientRequest(httpClientRequest);

                    //设置api对应的参数信息
                    List<DfpParamsBO> dfpParamsBOS = new ArrayList<>();
                    List<DfpSceneParamsModel> dfpSceneParamsModels = dfpSceneParamsDAO.findBySceneIdAndApiIdAndParamTypeNot(sceneId,
                            sceneRequestModel.getApiId(), "response");
                    ObjectConverterUtil.convertList(dfpSceneParamsModels, dfpParamsBOS, DfpParamsBO.class);

                    //设置需要从结果解析的参数
                    List<DfpGetFromResponseBO> dfpGetFromResponseBOS = new ArrayList<>();
                    List<DfpSceneParamsModel> dfpSceneParamsModelList = dfpSceneParamsDAO.findBySceneIdAndApiIdAndParamType(sceneId,
                            sceneRequestModel.getApiId(), "response");
                    for (DfpSceneParamsModel dfpSceneParamsModel : dfpSceneParamsModelList) {

                        DfpGetFromResponseBO dfpGetFromResponseBO = new DfpGetFromResponseBO();
                        dfpGetFromResponseBO.setParamKey(dfpSceneParamsModel.getParamKey());
                        dfpGetFromResponseBO.setParamValue(dfpSceneParamsModel.getDefaultValue());
                        dfpGetFromResponseBOS.add(dfpGetFromResponseBO);
                    }

                    //设置对象
                    dfpHttpRequestBO.setDfpParamsBOS(dfpParamsBOS);
                    dfpHttpRequestBO.setDfpGetFromResponseBOS(dfpGetFromResponseBOS);
                    dfpHttpRequestBOS.add(dfpHttpRequestBO);
                }

                dfpSceneRequestVO.setDfpHttpRequestBOS(dfpHttpRequestBOS);
                defenderResult.setData(dfpSceneRequestVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 分页查询场景信息
     *
     * @param dfpSceneListBO
     * @return
     */
    private Page<DfpSceneModel> getSceneByPage(DfpSceneListBO dfpSceneListBO) {

        //设置排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(dfpSceneListBO.getPageIndex(), dfpSceneListBO.getPageSize(), sort);
        Specification<DfpSceneModel> spec = new Specification<DfpSceneModel>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<DfpSceneModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //项目名称
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(criteriaBuilder.equal(root.get("projectName"), dfpSceneListBO.getProjectName()));

                //模块Id不为空，设置模块ID
                if (dfpSceneListBO.getModuleId() > 0) {

                    list.add(criteriaBuilder.equal(root.get("moduleId"), dfpSceneListBO.getModuleId()));
                }

                //创建人不为空，则进行查询
                if (dfpSceneListBO.getCreatorId() > 0) {

                    list.add(criteriaBuilder.equal(root.get("creatorId"), dfpSceneListBO.getCreatorId()));
                }

                //场景不为空，模糊查询
                if (StringUtils.isNotBlank(dfpSceneListBO.getSceneName())) {

                    list.add(criteriaBuilder.like(root.get("sceneName"), "%" + dfpSceneListBO.getSceneName() + "%"));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };

        return dfpSceneDAO.findAll(spec, pageable);
    }

    /**
     * SceneModel 转SceneVO
     *
     * @param dfpSceneModel
     * @param dfpSceneVO
     * @return
     */
    private DfpSceneVO convertSceneModelToVO(DfpSceneModel dfpSceneModel, DfpSceneVO dfpSceneVO) {

        ObjectConverterUtil.convert(dfpSceneModel, dfpSceneVO);

        //创建人信息
        DfpUserModel creator = dfpUserDAO.findById(dfpSceneModel.getCreatorId());
        String creatorName = creator == null ? "" : creator.getUsername();
        dfpSceneVO.setCreatorName(creatorName);

        //修改人信息
        DfpUserModel modifier = dfpUserDAO.findById(dfpSceneModel.getModifierId());
        String modifierName = modifier == null ? "" : modifier.getUsername();
        dfpSceneVO.setModifierName(modifierName);

        DfpModuleModel dfpModuleModel = dfpModuleDAO.findById(dfpSceneModel.getModuleId());
        dfpSceneVO.setDfpModuleModel(dfpModuleModel);

        return dfpSceneVO;
    }

    /**
     * 复制场景
     *
     * @param dfpCpSceneBO
     * @return
     */
    public DfplatformResult<Integer> copyScene(DfpCpSceneBO dfpCpSceneBO) {

        //定义返回结果
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            @Override
            public void doCheck() {

            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                int sceneId = dfpCpSceneBO.getSceneId();

                //查询已有场景信息
                DfpSceneModel dfpSceneModlOld = dfpSceneDAO.findById(sceneId);

                //复制场景信息
                DfpSceneModel dfpSceneModel = new DfpSceneModel();
                ObjectConverterUtil.convert(dfpSceneModlOld, dfpSceneModel);
                dfpSceneModel.setSceneName("copy" + dfpSceneModel.getSceneName());
                dfpSceneModel.setCreatorId(dfpCpSceneBO.getUserId());
                dfpSceneModel.setModifierId(dfpCpSceneBO.getUserId());
                dfpSceneModel.setId(0);
                dfpSceneModel = dfpSceneDAO.save(dfpSceneModel);

                //复制场景参数信息
                List<DfpSceneParamsModel> sceneParamsModels = dfpSceneParamsDAO.findBySceneId(dfpCpSceneBO.getSceneId());
                for (DfpSceneParamsModel dfpSceneParamsModel : sceneParamsModels) {

                    DfpSceneParamsModel sceneParamsModel = new DfpSceneParamsModel();
                    ObjectConverterUtil.convert(dfpSceneParamsModel, sceneParamsModel);
                    sceneParamsModel.setSceneId(dfpSceneModel.getId());
                    sceneParamsModel.setId(0);
                    dfpSceneParamsDAO.save(sceneParamsModel);
                }
                //复制场景请求信息
                List<DfpSceneRequestModel> sceneRequestModels = dfpSceneRequestDAO.findBySceneIdOrderByApiOrderAsc(dfpCpSceneBO.getSceneId());
                for (DfpSceneRequestModel sceneRequestModel : sceneRequestModels) {

                    DfpSceneRequestModel dfpSceneRequestModel = new DfpSceneRequestModel();
                    ObjectConverterUtil.convert(sceneRequestModel, dfpSceneRequestModel);
                    dfpSceneRequestModel.setSceneId(dfpSceneModel.getId());
                    dfpSceneRequestModel.setId(0);
                    dfpSceneRequestDAO.save(dfpSceneRequestModel);
                }

                //复制场景自定义APi信息
                List<DfpCustomApiModel> dfpCustomApiModels = dfpCustomApiDAO.findBySceneId(dfpCpSceneBO.getSceneId());
                for (DfpCustomApiModel dfpCustomApiModel : dfpCustomApiModels) {

                    DfpCustomApiModel customApiModel = new DfpCustomApiModel();
                    ObjectConverterUtil.convert(dfpCustomApiModel, customApiModel);
                    customApiModel.setSceneId(dfpSceneModel.getId());
                    customApiModel.setId(0);
                    dfpCustomApiDAO.save(customApiModel);
                }

                //设置结果信息
                defenderResult.setData(dfpSceneModel.getId());
                defenderResult.setMessage("复制场景成功");
            }
        }, defenderResult);

        return defenderResult;
    }
}
