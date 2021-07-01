package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 参数模型
 * @author: lingxi
 * @date: 2020/11/15 22:32
 **/
@Data
public class DfpQueryEnvParamsBO {

    int envId;

    String projectName;

    String serviceName;

    @NotBlank(message = "父菜单名称不能为空")
    String parentName;

    @Range(min = 0, message = "当前页码不能小于0")
    int pageIndex;

    @Range(min = 1, message = "总页数不能小于0")
    int pageSize;
}
