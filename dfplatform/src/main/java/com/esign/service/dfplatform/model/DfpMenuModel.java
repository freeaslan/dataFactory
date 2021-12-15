package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: yunge
 * @Description: 对应数据库表dfp_menu模型
 * @Date: 2021/5/6 9:19
 */
@Entity
@Data
@Table(name = "dfp_menu")
public class DfpMenuModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int parentId;

    String name;

    int type;

    String icon;

    int creatorId;

    int modifierId;
}
