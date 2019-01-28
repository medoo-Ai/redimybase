package com.aispread.manager.form.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vim 2019/1/2 15:37
 *
 * @author Vim
 */
@Data
public class FormDTO extends FormDefaultDTO {

    /**
     * 表单字段
     */
    private List<FormField> formFields;
}
