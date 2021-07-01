package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: huangtai
 * @Description: 场景模型
 * @Date: 2020/9/14 11:00
 */
@Data
public class DfpSceneBO {

    int sceneId;

    @NotBlank(message = "所属项目名称不能为空")
    String projectName;

    @Range(min = 1, message = "模块Id不能为空")
    int moduleId;

    @NotBlank(message = "场景名称不能为空")
    String sceneName;

    List<DfpHttpRequestBO> dfpHttpRequestBOS;

    int userId;
}
