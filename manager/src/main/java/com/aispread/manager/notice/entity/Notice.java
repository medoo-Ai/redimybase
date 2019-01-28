package com.aispread.manager.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2019-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_notice")
public class Notice extends IdEntity<String> {

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;


    /**
     * 用户ID
     */
     @TableField("user_id")
    private String userId;

    /**
     * 消息标题
     */
     @TableField("message_title")
    private String messageTitle;

    /**
     * 消息内容
     */
     @TableField("message_content")
    private String messageContent;

    /**
     * 状态(0:未读,1:已读)
     */
     @TableField("read_flag")
    private Integer readFlag;

    /**
     * 创建者ID
     */
     @TableField("create_user")
    private String createUser;

    /**
     * 创建时间
     */
     @TableField("create_time")
    private LocalDateTime createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
