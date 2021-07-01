package com.esign.service.dfplatform.BO;

import com.esign.service.dfplatform.util.client.HttpClientRequest;
import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: http请求模型
 * @Date: 2020/9/14 16:38
 */
@Data
public class DfpHttpRequestBO {

    int apiId;

    int apiOrder;

    String requestDescript;

    String requestOtherName;

    HttpClientRequest httpClientRequest;

    List<DfpParamsBO> dfpParamsBOS;

    List<DfpGetFromResponseBO> dfpGetFromResponseBOS;
}
