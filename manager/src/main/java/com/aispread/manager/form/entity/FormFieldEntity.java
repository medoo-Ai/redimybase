package com.aispread.manager.form.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author vim
 * @since 2019-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_form_field")
public class FormFieldEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 是否用于流程
     */
    @TableField("flow")
    private String flow;

    /**
     * 表单ID
     */
    @TableField("form_id")
    private String formId;

    /**
     * 字段类型
     */
    @TableField("type")
    private String type;

    /**
     * 内容类型
     */
    @TableField("content_type")
    private String contentType;

    @TableField("is_log")
    private Integer isLog;

    /**
     * 字段属性名
     */
    @TableField("field_name")
    private String fieldName;

    /**
     * 字段名
     */
    @TableField("name")
    private String name;

    /**
     * 标签
     */
    @TableField("label")
    private String label;

    @TableField("adding")
    private Boolean adding;

    /**
     * 是否必填(0:否,1:是)
     */
    @TableField("required")
    private Boolean required;

    /**
     * 必填提示
     */
    @TableField("requiredMessage")
    private String requiredMessage;

    /**
     * 栅栏
     */
    @TableField("span")
    private Integer span;

    /**
     * 选项
     */
    @TableField("options")
    private String options;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
