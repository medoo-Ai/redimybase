package com.aispread.manager.flowable.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


/**
 * 首页任务Page
 * Created by Vim 2019/1/13 17:21
 *
 * @author Vim
 */
@Data
public class TaskPage<T> extends Page<T> {

    /**
     * 任务数量
     */
    private Integer taskCount;
}
