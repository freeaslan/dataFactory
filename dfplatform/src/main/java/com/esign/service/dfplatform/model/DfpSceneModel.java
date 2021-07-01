package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: huangtai
 * @Description: 对应数据库表dfp_scene模型
 * @Date: 2020/9/15 13:45
 */
@Entity
@Data
@Table(name = "dfp_scene")
public class DfpSceneModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String projectName;

    String sceneName;

    int moduleId;

    int modifierId;

    int creatorId;
}
