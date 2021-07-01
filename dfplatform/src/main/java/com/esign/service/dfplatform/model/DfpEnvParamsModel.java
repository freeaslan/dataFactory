package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description:  对应数据库表dfp_env_paramsm模型
 * @author: lingxi
 * @date: 2020/11/15 22:33
 **/
@Entity
@Data
@Table(name = "dfp_env_params")
public class DfpEnvParamsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String projectName;

    int envId;

    String header;

    String host;

    String serviceName;
}
