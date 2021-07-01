package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpEnvParamsModel;
import lombok.Data;

import java.util.List;

/**
 * @Description:环境参数VO模型
 * @author: lingxi
 * @date: 2020/11/15 23:10
 **/
@Data
public class DfpEnvParamsVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpEnvParamsModelVO> dfpEnvParamsModelList;
}
