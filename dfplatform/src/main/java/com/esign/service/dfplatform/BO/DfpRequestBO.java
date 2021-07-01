package com.esign.service.dfplatform.BO;

import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 请求模型
 * @Date: 2020/9/24 15:26
 */
@Data
public class DfpRequestBO {

    int sceneId;

    List<DfpRequestBaseBO> apiRequests;
}
