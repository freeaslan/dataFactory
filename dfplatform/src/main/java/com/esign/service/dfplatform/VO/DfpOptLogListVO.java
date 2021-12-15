package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpOptLogModel;
import com.esign.service.dfplatform.model.DfpUserModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 操作日志分页视图
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpOptLogListVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpOptLogModel> dfpOptLogModels;
}
