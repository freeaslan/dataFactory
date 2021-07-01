package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpSwaggerBO;
import com.esign.service.dfplatform.VO.DfpApisModelVO;
import com.esign.service.dfplatform.VO.DfpServiceVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpApisDAO;
import com.esign.service.dfplatform.model.DfpApisModel;
import com.esign.service.dfplatform.util.DfplaformUtil;
import com.esign.service.dfplatform.util.ObjectConverterUtil;
import com.esign.service.dfplatform.util.client.HttpClientRequest;
import com.esign.service.dfplatform.util.client.HttpClientResponse;
import com.esign.service.dfplatform.util.client.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: huangtai
 * @Description: swagger服务
 * @Date: 2021/6/15 11:01
 */
@Service
@Slf4j
public class DfpSwaggerService {

    @Autowired
    DfpApisDAO dfpApisDAO;

    @Autowired
    OperateTemplate operateTemplate;

    /**
     * wagger解析
     *
     * @param dfpSwaggerBO
     * @return
     */
    public DfplatformResult<Integer> importSwaggers(DfpSwaggerBO dfpSwaggerBO) {

        //定义结果
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpSwaggerBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //调用swagger接口,获取接口信息
                HttpClientRequest httpClientRequest = new HttpClientRequest();
                httpClientRequest.setHost(StringUtils.substringBetween(dfpSwaggerBO.getSwaggerLink(), "http://", "/"));
                httpClientRequest.setUrl(StringUtils.substringAfter(dfpSwaggerBO.getSwaggerLink(), httpClientRequest.getHost()));
                httpClientRequest.setType("get");
                HttpClientResponse httpClientResponse = HttpClientUtil.sendRequest(httpClientRequest);
                JSONObject jsonObject = JSONObject.fromObject(httpClientResponse.getResponseBody());

                //获取接口路径
                JSONObject paths = jsonObject.getJSONObject("paths");
                String basePath = "";
                if (jsonObject.containsKey("basePath")) {
                    if (!"/".equals(jsonObject.getString("basePath"))) {
                        basePath = jsonObject.getString("basePath");
                    }
                }
                for (Object path : paths.keySet()) {

                    //定义对象
                    DfpApisModel dfpApisModel = new DfpApisModel();
                    dfpApisModel.setHost(jsonObject.getString("host"));
                    dfpApisModel.setServiceName(dfpSwaggerBO.getServiceName());
                    dfpApisModel.setProjectName(dfpSwaggerBO.getProjectName());

                    //获取接口key
                    String key = path.toString();
                    dfpApisModel.setUrl(basePath + key);
                    //获取接口key对应的接口信息
                    JSONObject pathMessage = paths.getJSONObject(key);
                    for (Object method : pathMessage.keySet()) {

                        //获取方法key
                        String methodKey = method.toString();
                        dfpApisModel.setMethod(methodKey);
                        //获取方法key对应的方法信息
                        JSONObject methodMessage = pathMessage.getJSONObject(methodKey);
                        //获取接口描述信息
                        String summary = methodMessage.getString("summary");
                        dfpApisModel.setSummary(summary);
                        //获取接口所属tag
                        JSONArray tags = methodMessage.getJSONArray("tags");
                        if (tags != null && tags.size() > 0) {

                            dfpApisModel.setTag(tags.get(0).toString());
                        }

                        //如果相同项目、相同服务存在同一个方法和接口名称，则更新接口信息，否则新增接口信息
                        DfpApisModel dfpApisModelCheck = dfpApisDAO.findByProjectNameAndServiceNameAndUrlAndMethod(
                                dfpApisModel.getProjectName(), dfpApisModel.getServiceName(), dfpApisModel.getUrl(), dfpApisModel.getMethod());
                        if (dfpApisModelCheck != null) {

                            int id = dfpApisModelCheck.getId();
                            ObjectConverterUtil.convert(dfpApisModel, dfpApisModelCheck);
                            dfpApisModelCheck.setId(id);
                            dfpApisDAO.save(dfpApisModelCheck);
                        } else {
                            dfpApisDAO.save(dfpApisModel);
                        }
                    }
                }
            }
        }, defenderResult);

        defenderResult.setMessage("swagger导入成功");
        return defenderResult;
    }


    /**
     * 根据项目名称查询服务名
     *
     * @param projectName
     * @return
     */
    public DfplatformResult<List<DfpServiceVO>> getServices(String projectName) {

        //定义返回值
        DfplatformResult<List<DfpServiceVO>> defaultResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotEmpty(projectName, "项目名称不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {
                List<DfpServiceVO> dfpServiceVOS = new ArrayList<>();
                List<String> dfpApisModels = dfpApisDAO.findDistinctByProjectName(projectName);
                for (String dfpApisModel : dfpApisModels) {

                    DfpServiceVO dfpServiceVO = new DfpServiceVO();
                    dfpServiceVO.setServiceName(dfpApisModel);
                    dfpServiceVO.setId(dfpServiceVOS.size());
                    dfpServiceVOS.add(dfpServiceVO);
                }

                defaultResult.setData(dfpServiceVOS);
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 根据项目名称接口信息
     *
     * @param serviceName
     * @return
     */
    public DfplatformResult<List<DfpServiceVO>> getPaths(String serviceName) {

        //定义返回值
        DfplatformResult<List<DfpServiceVO>> defaultResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotEmpty(serviceName, "服务名称不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                List<DfpServiceVO> dfpServiceVOS = new ArrayList<>();
                List<DfpApisModel> dfpApisModels = dfpApisDAO.findByServiceName(serviceName);
                for (DfpApisModel dfpApisModel : dfpApisModels) {

                    DfpServiceVO dfpServiceVO = new DfpServiceVO();
                    dfpServiceVO.setPathName(dfpApisModel.getServiceName() + "_" + dfpApisModel.getUrl() +
                            "_" + dfpApisModel.getMethod() + "_" + dfpApisModel.getSummary());
                    dfpServiceVO.setId(dfpApisModel.getId());
                    dfpServiceVOS.add(dfpServiceVO);
                }

                defaultResult.setData(dfpServiceVOS);
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 根据Id查询接口详细信息
     *
     * @param id
     * @return
     */
    public DfplatformResult<DfpApisModelVO> getApis(int id) {

        //定义返回值
        DfplatformResult<DfpApisModelVO> defaultResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(id > 0, "接口ID必须大于0");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                DfpApisModel dfpApisModel = dfpApisDAO.findById(id);
                DfplaformUtil.isNotNull(dfpApisModel, "添加的接口不存在");
                DfpApisModelVO dfpApisModelVO = new DfpApisModelVO();
                ObjectConverterUtil.convert(dfpApisModel, dfpApisModelVO);
                dfpApisModelVO.setHeader("{}");
                dfpApisModelVO.setData(new HashMap<>());
                defaultResult.setData(dfpApisModelVO);
            }
        }, defaultResult);

        return defaultResult;
    }
}
