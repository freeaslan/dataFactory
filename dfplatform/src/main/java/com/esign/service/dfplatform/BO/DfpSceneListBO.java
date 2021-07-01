package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Author: huangtai
 * @Description: 场景列表模型
 * @Date: 2020/9/14 11:24
 */
@Data
public class DfpSceneListBO {

    @Range(min = 0, message = "当前页码不能小于0")
    int pageIndex;

    @Range(min = 0, message = "总页数不能小于0")
    int pageSize;

    @NotBlank(message = "所属项目名称不能为空")
    String projectName;

    String sceneName;

    int moduleId;

    int modifierId;

    int creatorId;
}
