package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: huangtai
 * @Description: 对应数据库表dfp_module模型
 * @Date: 2020/9/15 13:40
 */
@Entity
@Data
@Table(name = "dfp_module")
public class DfpModuleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String projectName;

    String moduleName;

    int creatorId;
}
