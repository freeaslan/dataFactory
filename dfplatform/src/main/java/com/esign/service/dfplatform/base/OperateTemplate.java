package com.esign.service.dfplatform.base;

import com.esign.service.dfplatform.enums.ErrorCodeEnum;
import com.esign.service.dfplatform.exception.BaseCheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Description:业务操作模板
 * @author: 侯兰东
 * @date: 2021.06.07
 */
@Service
@Slf4j
public class OperateTemplate {

    @Autowired
    private TransactionOperations transactionTemplate;

    /**
     * 需要事务的业务操作
     *
     * @param operateCallBack
     * @param defenderResult
     */
    public void operateWithTransaction(OperateCallBack operateCallBack,
                                       DfplatformResult defenderResult) {

        operate(operateCallBack, defenderResult, true);
    }

    /**
     * 不需要事务的业务操作
     *
     * @param operateCallBack
     * @param defenderResult
     */
    public void operateWithOutTransaction(OperateCallBack operateCallBack,
                                          DfplatformResult defenderResult) {

        operate(operateCallBack, defenderResult, false);
    }

    /**
     * 通用操作摸板
     *
     * @param operateCallBack
     * @param defenderResult
     * @param withTransaction 是否需要事务
     */
    public void operate(final OperateCallBack operateCallBack,
                        DfplatformResult defenderResult, Boolean withTransaction) {

        try {

            // 前置校验
            log.info("开始参数校验");
            operateCallBack.doCheck();

            // 参数组装
            log.info("开始参数组装");
            operateCallBack.doPacker();

            //需要事务则进行事务操作
            if (withTransaction) {
                transactionTemplate.execute(new TransactionCallback() {

                    @Override
                    public Object doInTransaction(final TransactionStatus status) {

                        // 开启事务进行业务操作
                        try {

                            log.info("开始业务操作");
                            operateCallBack.doOperate();

                        } catch (Exception e) {
                            //出现异常则进行回滚
                            status.setRollbackOnly();
                            setException(e, defenderResult);
                        }

                        return null;
                    }
                });
            } else {

                // 无事务业务操作
                log.info("开始业务操作");
                operateCallBack.doOperate();
            }

            // 无异常，结果码设置未0，表示成功
            defenderResult.setCode(defenderResult.getCode());
        } catch (Exception e) {

            //设置异常信息
            setException(e, defenderResult);
        }
    }

    /**
     * 异常处理
     *
     * @param e
     * @param defenderResult
     */
    private void setException(Exception e, DfplatformResult defenderResult) {

        defenderResult.setData(null);

        if (e instanceof FileNotFoundException) {

            log.error("FileNotFoundException" + e);
            defenderResult.setCode(ErrorCodeEnum.FILEE.getCode());
            defenderResult.setMessage(ErrorCodeEnum.FILEE.getDescription());
        } else if (e instanceof IOException) {

            log.error("IOException---读写出现异常", e);
            defenderResult.setCode(ErrorCodeEnum.IOE.getCode());
            defenderResult.setMessage(ErrorCodeEnum.IOE.getDescription());
        } else if (e instanceof InterruptedException) {

            log.error("interruptedexception--中断异常", e);
            defenderResult.setCode(ErrorCodeEnum.INTE.getCode());
            defenderResult.setMessage(ErrorCodeEnum.INTE.getDescription());
        } else if (e instanceof BaseCheckException) {

            log.error("BaseCheckException--基本校验异常", e);
            defenderResult.setCode(ErrorCodeEnum.BCHECKE.getCode());
            defenderResult.setMessage(e.getMessage());
        } else if (e instanceof NullPointerException) {

            log.error("NullPointerException--空指针异常", e);
            defenderResult.setCode(ErrorCodeEnum.NULLE.getCode());
            defenderResult.setMessage(ErrorCodeEnum.NULLE.getDescription());
        } else {

            log.error("业务操作出现异常，异常信息为：", e);
            defenderResult.setCode(ErrorCodeEnum.OTHERE.getCode());
            defenderResult.setMessage(ErrorCodeEnum.OTHERE.getDescription());
        }
    }
}