package com.shaoxia.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-22 11:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq implements Serializable {
	private String username;
	private String password;
}
