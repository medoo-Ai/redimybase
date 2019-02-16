package com.aispread.manager.integrate.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Vim 2019/2/16 14:30
 *
 * @author Vim
 */
@Data
@ApiModel("系统集成信息MDOEL")
public class SystemIntegrateInfo implements Serializable {


    private String id;

    private String userName;

    private String name;

    private String url;

    private String systemId;
}
