package com.esign.service.dfplatform.BO;

import lombok.Data;

/**
 * @Author: huangtai
 * @Description: 请求场景参数
 * @Date: 2020/9/23 11:38
 */
@Data
public class DfpParamsBO {

    String paramKey;

    String paramType;

    String description;

    String defaultValue;
}
