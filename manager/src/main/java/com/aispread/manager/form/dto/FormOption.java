package com.aispread.manager.form.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Vim 2019/1/3 10:06
 *
 * @author Vim
 */
@Data
public class FormOption implements Serializable {

    private String label;
    private String value;
}
