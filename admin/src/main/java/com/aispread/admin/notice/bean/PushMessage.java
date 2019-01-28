package com.aispread.admin.notice.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vim 2019/1/27 16:57
 *
 * @author Vim
 */
@Data
public class PushMessage implements Serializable {

    /**
     * 消息主体
     */
    private List<Object> data;


    public void addMessage(Object object) {
        data.add(object);
    }

    public PushMessage(Object... message) {
        data = new ArrayList<>();
        data.addAll(Arrays.asList(message));
    }
}
