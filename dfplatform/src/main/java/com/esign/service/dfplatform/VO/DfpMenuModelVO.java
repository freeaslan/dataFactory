package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpMenuModel;
import lombok.Data;

/**
 * @Description: 菜单模型VO
 * @author: 侯兰东
 * @date: 2021.06.14
 */
@Data
public class DfpMenuModelVO extends DfpMenuModel {

    String parentName;

    int status=1;
}
