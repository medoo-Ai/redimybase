package com.aispread.manager.form.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表单字段属性
 * Created by Vim 2019/1/3 10:06
 *
 * @author Vim
 */
@Data
public class FormField implements Serializable {

    /**
     * 控件类型
     */
    private String type;
    /**
     * 控件名称
     */
    private String name;


    /**
     * 控件绑定属性名
     */
    private String fieldName;

    /**
     * 标签
     */
    private String label;

    private boolean adding;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 是否必填提醒消息
     */
    private String requiredMessage;


    /**q
     * 栅格
     */
    private Integer span;


    /**
     * 选项
     */
    private List<FormOption> options;

    /**
     * 值
     */
    private String value;

    private Integer optionRowShow;
}
