package com.esign.service.dfplatform.enums;

/**
 * 异常枚举
 *
 * @author huangtai
 */
public enum ErrorCodeEnum {

    OTHERE("未知异常,请看日志", 1000),
    IOE("IOException---读写出现异常", 1001),
    INTE("interruptedexception--中断异常", 1002),
    BCHECKE("BaseCheckException--基本校验异常", 1003),
    FILEE("文件异常", 1005),
    NULLE("空指针异常", 1007);

    private final String description;

    private final int code;

    /**
     * @param description
     * @param code
     */
    ErrorCodeEnum(String description, int code) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
