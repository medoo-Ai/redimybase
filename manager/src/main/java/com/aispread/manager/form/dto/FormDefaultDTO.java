package com.aispread.manager.form.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 流程默认审批表单
 * Created by Vim 2019/1/10 19:39
 *
 * @author Vim
 */
@Data
public class FormDefaultDTO implements Serializable {


    /**
     * 意见
     */
    private String comment;

}
