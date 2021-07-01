package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description: 对应数据库表dfp_public_param模型
 * @author: huangtai
 * @date: 2021/06/08 18:02
 **/
@Entity
@Data
@Table(name = "dfp_public_param")
public class DfpPublicParamModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String paramKey;

    String paramClassType;

    String paramClassMethod;

    String paramClassPath;

    String paramClassParamsData;

    String jarName;

    int isNeedBody;

    int afterOtherParam;

    int creatorId;

    int modifierId;
}
