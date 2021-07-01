package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpModuleModel;
import lombok.Data;

/**
 * @Author: huangtai
 * @Description: 场景试图
 * @Date: 2020/9/24 9:33
 */
@Data
public class DfpSceneVO extends DfpSceneDetailVO {

    DfpModuleModel dfpModuleModel;
}
