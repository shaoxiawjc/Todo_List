package com.shaoxia.model.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@Data
public class User implements Serializable {
    private Long id;

    private String name;

    private String password;

    private Date createdTime;

    private Date updatedTime;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}