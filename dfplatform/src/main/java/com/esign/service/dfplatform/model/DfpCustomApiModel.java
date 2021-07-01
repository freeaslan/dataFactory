package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: huangtai
 * @Description: 对应数据库表dfp_custom_api模型
 * @Date: 2020/9/18 17:26
 */
@Entity
@Data
@Table(name = "dfp_custom_api")
public class DfpCustomApiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int sceneId;

    String url;

    String host;

    String method;

    String projectName;
}
