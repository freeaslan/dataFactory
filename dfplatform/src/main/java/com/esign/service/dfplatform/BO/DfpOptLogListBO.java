package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: huangtai
 * @Description: 查询操作日志
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpOptLogListBO {

    String username;

    String operateLog;

    @Range(min = 0, message = "当前页码不能小于0")
    int pageIndex;

    @Range(min = 1, message = "总页数不能小于0")
    int pageSize;
}
