package com.aispread.manager.form.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("t_form")
public class FormEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

    @TableField("creator")
    private String creator;

    @TableField("field_num")
    private Integer fieldNum;

    @TableField("name")
    private String name;

    @TableField("original_html")
    private String originalHtml;

    @TableField("parse_html")
    private String parseHtml;

    @TableField("type")
    private String type;

    private String json;

    private String formKey;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
