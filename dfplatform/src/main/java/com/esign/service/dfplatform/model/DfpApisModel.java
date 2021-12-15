package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description: 对应数据库表dfp_apis模型
 * @author: lingxi
 * @date: 2020/11/30 18:02
 **/
@Entity
@Data
@Table(name = "dfp_apis")
public class DfpApisModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String projectName;

    String serviceName;

    String url;

    String host;

    String method;

    String tag;

    String summary;

    String header;

    String requestBody;

    int creatorId;

    int modifierId;
}
