package com.esign.service.dfplatform.BO;

import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 请求基础模型
 * @Date: 2020/9/29 17:23
 */
@Data
public class DfpRequestBaseBO {

    String apiRequest;

    List<DfpRequestHttpParamBO> dfpSceneParamsModels;
}
