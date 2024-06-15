package com.shaoxia.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-22 11:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq implements Serializable {
	private String name;
	private String password;
}
