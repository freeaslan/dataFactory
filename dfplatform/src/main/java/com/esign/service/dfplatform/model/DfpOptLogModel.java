package com.esign.service.dfplatform.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: huangtai
 * @Description: 对应数据库表dfp_opt_log模型
 * @Date: 2021/8/3 9:19
 */
@Entity
@Data
@Table(name = "dfp_opt_log")
public class DfpOptLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userName;

    String operateLog;

    Date createTime;

    int status;
}
