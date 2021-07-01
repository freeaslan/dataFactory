package com.esign.service.dfplatform.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 场景列表视图
 * @Date: 2020/9/14 10:21
 */
@Data
public class DfpSceneListVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpSceneVO> dfpSceneVOS;
}
