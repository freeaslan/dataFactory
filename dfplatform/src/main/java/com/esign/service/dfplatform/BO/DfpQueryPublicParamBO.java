package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @Description: 查询公共参数业务模型
 * @author: huangtai
 * @date: 2021/06/08 18:02
 **/
@Data
public class DfpQueryPublicParamBO {

    @Range(min = 0, message = "当前页码不能小于0")
    int pageIndex;

    @Range(min = 1, message = "总页数不能小于0")
    int pageSize;

    String paramKey;

    String paramClassPath;

    int userId;
}
