package com.esign.service.dfplatform.base;

import lombok.Data;

/**
 * 通用结果模型
 *
 * @author houlandong
 **/
@Data
public class DfplatformResult<T> {

    public int code;

    public String message;

    public T data;
}
