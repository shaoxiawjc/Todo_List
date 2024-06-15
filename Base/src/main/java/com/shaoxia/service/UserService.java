package com.shaoxia.service;

import com.shaoxia.model.dto.req.LoginReq;
import com.shaoxia.model.dto.req.RegisterReq;
import com.shaoxia.model.pojo.User;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户业务
 * @date 2024-05-22 11:37
 */
public interface UserService {
	boolean checkValid(LoginReq loginReq);

	boolean checkValid(RegisterReq registerReq);
	Long checkLogin(User user,LoginReq loginReq);
	String getToken(User user);

	void saveTokenToRedis(String token,Long id);


	User hasUser(String name);

	User hasUserById(long id);

	boolean logout();

	void register(RegisterReq registerReq);
}
