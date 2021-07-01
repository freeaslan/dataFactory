package com.esign.service.dfplatform.VO;

import lombok.Data;

import java.util.List;

/**
 * @Author: yunge
 * @Description:菜单模型
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpMenuVO extends DfpMenuModelVO {

    List<DfpMenuModelVO> list;
}
