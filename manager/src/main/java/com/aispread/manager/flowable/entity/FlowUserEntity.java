package com.aispread.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

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
 * @since 2018-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_user")
public class FlowUserEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户类型(0:发起人,1:用户组,2:其他节点,3:来自表单,4:上级领导,5:普通用户)
     */
    @TableField("type")
    private Integer type;

    /**
     * 值
     */
    @TableField("value")
    private String value;

    /**
     * 计算符号(0:与,1:或,2:非)
     */
    @TableField("symbol")
    private Integer symbol;


    /**
     * 节点ID
     */
    @TableField("node_id")
    private String nodeId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class Type {
        //TODO 目前流程只需要 上级领导,指定职位,流程发起人

        public static final int 流程发起人 = 0;
        public static final int 用户组 = 1;
        public static final int 其他节点 = 2;
        public static final int 来自表单 = 3;
        public static final int 上级领导 = 4;
        public static final int 普通用户 = 5;
        public static final int 指定职位 = 6;
    }
}
