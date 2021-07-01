package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: yunge
 * @Description: 对应数据库表dfp_user模型
 * @Date: 2021/5/6 9:19
 */
@Entity
@Data
@Table(name = "dfp_user")
public class DfpUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String username;

    String password;
}
