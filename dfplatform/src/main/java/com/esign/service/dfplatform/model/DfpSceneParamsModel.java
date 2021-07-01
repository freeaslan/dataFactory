package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: huangtai
 * @Description: 对应数据库表dfp_scene_params模型
 * @Date: 2020/9/15 13:45
 */
@Entity
@Data
@Table(name = "dfp_scene_params")
public class DfpSceneParamsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int sceneId;

    int apiId;

    String paramKey;

    String paramType;

    String description;

    String defaultValue;
}
