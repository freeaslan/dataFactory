package com.esign.service.dfplatform.BO;

import com.esign.service.dfplatform.model.DfpSceneParamsModel;
import lombok.Data;

/**
 * @Author: huangtai
 * @Description: 请求参数模型
 * @Date: 2020/9/21 16:09
 */
@Data
public class DfpRequestHttpParamBO {

    DfpPublicParamBO dfpPublicParamBO;

    DfpSceneParamsModel dfpSceneParamsModel;
}
