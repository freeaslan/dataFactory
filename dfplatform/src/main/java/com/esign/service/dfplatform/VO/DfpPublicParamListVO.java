package com.esign.service.dfplatform.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 分页公共参数视图
 * @Date: 2021/6/8 9:19
 */
@Data
public class DfpPublicParamListVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpPublicParamVO> dfpPublicParamVOS;
}
