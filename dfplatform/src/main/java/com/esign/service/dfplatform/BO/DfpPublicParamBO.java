package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 公共参数业务模型
 * @author: huangtai
 * @date: 2021/06/08 18:02
 **/
@Data
public class DfpPublicParamBO {

    int id;

    @NotBlank(message = "参数名称不能为空")
    String paramKey;

    @NotBlank(message = "读取参数来源不能为空")
    String paramClassType;

    @NotBlank(message = "读取参数方法不能为空")
    String paramClassMethod;

    @NotBlank(message = "读取参数类包路径不能为空")
    String paramClassPath;

    String paramClassParamsData;

    @Range(min = 0, max = 1, message = "是否在其它参数执行后执行，0表示是，1表示否")
    int afterOtherParam;

    @Range(min = 0, message = "用户id应该大于0")
    int userId=0;

    String actualValue;

    @NotBlank(message = "包名或类名不能为空")
    String jarName;

    @Range(min = 0, max = 1, message = "是否需要body加密，0表示是，1表示否")
    int isNeedBody;
}
