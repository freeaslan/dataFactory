package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @Description: 参数模型
 * @author: lingxi
 * @date: 2020/11/15 22:32
 **/
@Data
public class DfpEnvParamsBO {

    int id;

    @Range(min = 1, message = "环境类型不正确")
    int envId;

    @NotBlank(message = "所属项目名称不能为空")
    String projectName;

    @NotBlank(message = "服务器地址不能为空")
    String host;

    @NotBlank(message = "服务名称不能为空")
    String serviceName;

    Map<String, String> header;

    int userId;
}
