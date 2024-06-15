package com.shaoxia.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户注册响应
 * @date 2024-05-22 11:44
 */
@Data
@AllArgsConstructor
@NonNull
public class LoginResp implements Serializable {
	private String token;
}
