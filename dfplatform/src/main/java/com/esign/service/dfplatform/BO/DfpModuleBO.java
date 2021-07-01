package com.esign.service.dfplatform.BO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: huangtai
 * @Description: 模块模型
 * @Date: 2020/9/14 13:49
 */
@Data
public class DfpModuleBO {

    @NotBlank(message = "项目名称不能为空")
    String projectName;

    @NotBlank(message = "模块名称不能为空")
    String moduleName;
}
