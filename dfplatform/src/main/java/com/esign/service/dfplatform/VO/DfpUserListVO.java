package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpUserModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: yunge
 * @Description: 分页用户视图
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpUserListVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpUserModel> userList;
}
