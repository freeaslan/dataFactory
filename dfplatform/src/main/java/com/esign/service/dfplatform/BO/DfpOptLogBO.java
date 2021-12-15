package com.esign.service.dfplatform.BO;

import lombok.Data;

/**
 * @Author: huangtai
 * @Description: 日志操作模型
 * @Date: 2021/8/3 9:19
 */
@Data
public class DfpOptLogBO {

    int userId;

    String operateLog;

    int status;
}
