package com.esign.service.dfplatform.base;

/**
 * @Description:统一业务操作
 * @author: 侯兰东
 * @date: 2021.06.07
 */
public interface OperateCallBack {

    /**
     * 前置校验
     */
    void doCheck();

    /**
     * 参数组装
     */
    void doPacker();

    /**
     * 业务操作
     *
     * @throws Exception
     */
    void doOperate() throws Exception;
}
