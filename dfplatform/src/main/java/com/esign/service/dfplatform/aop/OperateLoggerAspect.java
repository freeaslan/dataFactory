package com.esign.service.dfplatform.aop;

import com.esign.service.dfplatform.BO.DfpOptLogBO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpUserModel;
import com.esign.service.dfplatform.service.DfpOptLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: huangtai
 * @Description: 保存操作日志记录切面
 * @Date: 2021/8/3 16:41
 */
@Aspect
@Component
@Slf4j
public class OperateLoggerAspect {

    @Autowired
    private DfpOptLogService dfpOptLogService;

    // 定义一个切入点
    @Pointcut("@annotation(com.esign.service.dfplatform.aop.OperateLogger)")
    private void operateLoggerAspect() {
    }

    @AfterReturning(pointcut = "operateLoggerAspect() && @annotation(operateLogger)", returning = "ret")
    public void recordOperateLog(JoinPoint joinPoint, OperateLogger operateLogger, Object ret) {

        DfpOptLogBO dfpOptLogBO = new DfpOptLogBO();
        dfpOptLogBO.setOperateLog(operateLogger.operate());
        dfpOptLogBO.setUserId(getUserId(joinPoint.getArgs(), ret));

        //根据返回结果code值，判定操作结果
        if (((DfplatformResult) ret).getCode() == 0) {
            dfpOptLogBO.setStatus(0);
        } else {
            dfpOptLogBO.setStatus(1);
        }
        dfpOptLogService.addOptLog(dfpOptLogBO);
    }

    /**
     * 获取用户Id
     *
     * @param args
     * @param ret
     * @return
     */
    private int getUserId(Object[] args, Object ret) {

        int userId = 0;
        try {

            DfplatformResult result = (DfplatformResult) ret;
            if (result.getData() instanceof DfpUserModel) {
                //如果返回结果是用户模型，则从用户模型获取用户ID
                userId = ((DfpUserModel) result.getData()).getId();
            } else {
                //默认第一个参数为userId
                if (args[0] instanceof Integer) {

                    userId = Integer.valueOf(args[0].toString());
                } else {

                    //默认取第一个对象中的userId
                    Method method = args[0].getClass().getMethod("getUserId", new Class[]{});
                    Object value = method.invoke(args[0], new Object[]{});
                    userId = Integer.valueOf(value.toString());
                }
            }
        } catch (Exception e) {

            log.error("保存操作日志切面获取userId失败");
        }
        return userId;
    }
}