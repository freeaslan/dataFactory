package com.esign.service.dfplatform.BO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: yunge
 * @Description: 用户业务模型
 * @Date: 2021/5/6 9:19
 */
@Data
public class DfpUserBO {

    @NotBlank(message = "用户名称不能为空")
    String username;

    @NotBlank(message = "用户密码不能为空")
    String password;
}
