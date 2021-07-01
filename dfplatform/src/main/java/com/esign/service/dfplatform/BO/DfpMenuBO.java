package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @Author: yunge
 * @Description: 菜单模型
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpMenuBO {

    int id;

    int parentId;

    @NotBlank(message = "菜单名称不能为空")
    String name;

    @Range(min = 0, max = 2, message = "菜单类型不存在")
    int type;

    String icon;
}
