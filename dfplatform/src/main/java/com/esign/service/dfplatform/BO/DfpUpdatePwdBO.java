package com.esign.service.dfplatform.BO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: huangtai
 * @Description: 用户业务模型
 * @Date: 2021/12/16 9:19
 */
@Data
public class DfpUpdatePwdBO {

    @NotBlank(message = "用户名称不能为空")
    String username;

    String password;

    @NotBlank(message = "新密码不能为空")
    String newPassword;

    @NotBlank(message = "新密码二次输入不能为空")
    String newPasswordAgain;

    boolean isupdate;
}
