package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description: 对应数据库表dfp_env_enum模型
 * @author: huangtai
 * @date: 2021/06/08 18:02
 **/
@Entity
@Data
@Table(name = "dfp_env_enum")
public class DfpEnvEnumModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String envName;
}
