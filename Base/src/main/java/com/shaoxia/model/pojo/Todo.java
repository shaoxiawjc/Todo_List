package com.shaoxia.model.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @TableName todo
 */
@Data
@Builder
public class Todo implements Serializable {
    private Long id;

    private Long uid;

    private String title;

    private String content;

    private Integer isCompleted;

    private Date createdTime;

    private Date updatedTime;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}