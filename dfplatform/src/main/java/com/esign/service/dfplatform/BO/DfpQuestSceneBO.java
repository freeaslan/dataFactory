package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: huangtai
 * @Description: 场景请求模型
 * @Date: 2020/9/22 14:57
 */
@Data
public class DfpQuestSceneBO {

    @Range(min = 1, message = "场景Id不能为空")
    int sceneId;

    @NotBlank(message = "所属项目名称不能为空")
    String projectName;

    @Range(min = 1, message = "环境ID不能为空")
    int envId;

    List<DfpRequestParamsBO> dfpRequestParamsBOs;
}
