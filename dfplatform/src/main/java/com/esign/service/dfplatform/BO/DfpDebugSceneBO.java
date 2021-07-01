package com.esign.service.dfplatform.BO;

import com.esign.service.dfplatform.daointerface.DfpPublicParamDAO;
import lombok.Data;

/**
 * @Author: huangtai
 * @Description: debug场景模型
 * @Date: 2020/9/14 11:00
 */
@Data
public class DfpDebugSceneBO {

    DfpPublicParamDAO dfpPublicParamDAO;

    DfpSceneBO dfpSceneBO;
}
