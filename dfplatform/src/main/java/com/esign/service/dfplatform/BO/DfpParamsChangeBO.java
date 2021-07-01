package com.esign.service.dfplatform.BO;

import lombok.Data;

/**
 * @Author: huangtai
 * @Description: 参数修改模型
 * @Date: 2020/9/15 13:45
 */
@Data
public class DfpParamsChangeBO {

    private int id;

    private int sceneId;

    private int apiId;

    private int replaceType;

    private String paramKey;

    private String paramType;

    private String description;

    private String defaultValue;

    private Object paramClass;
}
