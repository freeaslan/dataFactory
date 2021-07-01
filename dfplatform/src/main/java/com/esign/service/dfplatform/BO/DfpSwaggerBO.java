package com.esign.service.dfplatform.BO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: huangtai
 * @Description: swagger业务模型
 * @Date: 2021/6/15 11:03
 */
@Data
public class DfpSwaggerBO {

    @NotBlank(message = "项目名称不能为空")
    String projectName;

    @NotBlank(message = "服务名称不能为空")
    String serviceName;

    @NotBlank(message = "swagger url不能为空")
    String swaggerLink;
}
