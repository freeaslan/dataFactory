package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpUpdatePwdBO;
import com.esign.service.dfplatform.BO.DfpUserBO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpUserDAO;
import com.esign.service.dfplatform.model.DfpUserModel;
import com.esign.service.dfplatform.util.DfplaformUtil;
import com.esign.service.dfplatform.util.ObjectConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yunge
 * @Description: 用户服务
 * @Date: 2021/5/6 9:19
 */
@Service
public class DfpUserService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpUserDAO dfpUserDAO;

    /**
     * 用户登录
     *
     * @param dfpUserBO
     * @return
     */
    public DfplatformResult<DfpUserModel> userLogin(DfpUserBO dfpUserBO) {

        //定义返回结果
        DfplatformResult<DfpUserModel> defaultResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpUserBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //根据用户名和密码登录 获取用户信息
                DfpUserModel dfpUserModel = dfpUserDAO.findByUsername(dfpUserBO.getUsername());
                DfplaformUtil.isNotNull(dfpUserModel, "用户不存在");
                DfplaformUtil.state(dfpUserBO.getPassword().equals(dfpUserModel.getPassword()), "密码输入不正确");
                defaultResult.setData(dfpUserModel);
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 注册用户
     *
     * @param dfpUserBO
     * @return
     */
    public DfplatformResult<DfpUserModel> createNewUser(DfpUserBO dfpUserBO) {

        //定义结果
        DfplatformResult<DfpUserModel> defaultResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpUserBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                DfpUserModel checkUserModel = dfpUserDAO.findByUsername(dfpUserBO.getUsername());
                DfplaformUtil.isNull(checkUserModel, "注册用户已经存在");

                DfpUserModel dfpUserModel = new DfpUserModel();
                ObjectConverterUtil.convert(dfpUserBO, dfpUserModel);
                dfpUserModel = dfpUserDAO.save(dfpUserModel);

                //设置结果
                defaultResult.setData(dfpUserModel);
                defaultResult.setMessage("注册成功");
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    public DfplatformResult<DfpUserModel> getUser(String username) {

        DfplatformResult<DfpUserModel> defenderResult = new DfplatformResult<>();


        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotEmpty(username, "用户名称不能为空");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                DfpUserModel dfpUserModel = dfpUserDAO.findByUsername(username);
                DfplaformUtil.isNotNull(dfpUserModel, "用户不存在");
                defenderResult.setData(dfpUserModel);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 修改密码或者忘记密码
     *
     * @param dfpUpdatePwdBO
     * @return
     */
    public DfplatformResult<Integer> updatePassword(DfpUpdatePwdBO dfpUpdatePwdBO) {

        //变量定义
        DfplatformResult<Integer> dfplatformResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {


                DfplaformUtil.isNotNull(dfpUpdatePwdBO, "请求对象不能为空");
                DfplaformUtil.isNotEmpty(dfpUpdatePwdBO.getNewPassword(), "新密码不能为空");
                DfplaformUtil.isNotEmpty(dfpUpdatePwdBO.getNewPasswordAgain(), "二次确认密码不能为空");
                DfplaformUtil.state(dfpUpdatePwdBO.getNewPassword().equals(dfpUpdatePwdBO.getNewPasswordAgain()), "两次输入的密码不一致,请重新输入");
                DfpUserModel dfpUserModel = dfpUserDAO.findByUsername(dfpUpdatePwdBO.getUsername());
                DfplaformUtil.isNotNull(dfpUserModel, "用户不存在");
                if (dfpUpdatePwdBO.isIsupdate()) {
                    DfplaformUtil.isNotEmpty(dfpUpdatePwdBO.getPassword(), "旧密码不能为空");
                    DfplaformUtil.state(dfpUserModel.getPassword().equals(dfpUpdatePwdBO.getPassword()), "密码输入错误");
                }

                dfpUserModel.setPassword(dfpUpdatePwdBO.getNewPassword());
                dfpUserDAO.save(dfpUserModel);
                dfplatformResult.setData(dfpUserModel.getId());
            }
        }, dfplatformResult);
        return dfplatformResult;
    }
}
