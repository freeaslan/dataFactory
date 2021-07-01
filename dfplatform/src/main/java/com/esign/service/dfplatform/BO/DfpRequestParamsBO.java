package com.esign.service.dfplatform.BO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: huangtai
 * @Description: 请求参数模型
 * @Date: 2020/9/21 16:09
 */
@Data
public class DfpRequestParamsBO {

    @NotBlank(message = "请求参数key不能为空")
    String paramKey;

    String paramValue = "";
}
