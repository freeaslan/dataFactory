package com.esign.service.dfplatform.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: yunge
 * @Description: 所有菜单视图
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpAllMenuListVO {

    int pageIndex;

    int pageSize;

    long totalNum;

    List<DfpMenuModelVO> menuList;
}
