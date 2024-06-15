package com.shaoxia.user.service.impl;

import com.shaoxia.common.utils.JwtUtils;
import com.shaoxia.common.utils.PasswordUtil;
import com.shaoxia.user.mapper.UserMapper;
import com.shaoxia.model.dto.req.LoginReq;
import com.shaoxia.model.dto.req.RegisterReq;
import com.shaoxia.model.pojo.User;
import com.shaoxia.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.shaoxia.common.constant.RedisKey.TOKEN;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-22 18:48
 */
@Service
@DubboService
public class UserServiceImpl implements UserService {
	private static final int MIN_PASSWORD_LEN = 6;
	@Resource
	private UserMapper userMapper;
	@Autowired
	private RedisTemplate redisTemplate;



	@Override
	public boolean checkValid(LoginReq loginReq) {
		String username = loginReq.getUsername();
		if (Objects.isNull(username) || "".equals(username)){
			return false;
		}
		String password = loginReq.getPassword();
		if (Objects.isNull(password) || "".equals(password)){
			return false;
		}
		return true;
	}

	@Override
	public boolean checkValid(RegisterReq registerReq) {
		String username = registerReq.getName();
		if (Objects.isNull(username) || "".equals(username)){
			return false;
		}
		String password = registerReq.getPassword();
		if (Objects.isNull(password) || "".equals(password)){
			return false;
		}
		return password.length() >= MIN_PASSWORD_LEN;
	}

	/**
	 * 判断是否可以登录
	 * @param user
	 * @param loginReq
	 * @return
	 */
	@Override
	public Long checkLogin(User user, LoginReq loginReq) {
		String stored = user.getPassword();
		String input = PasswordUtil.hashPassword(loginReq.getPassword());
		if (PasswordUtil.verifyPassword(input,stored)){
			return user.getId();
		}
		return -1l;
	}

	@Override
	public String getToken(User user) {
		HashMap<String, String> map = new HashMap<>();
		map.put("uid",user.getId().toString());
		map.put("name",user.getName());
		String token = JwtUtils.getToken(map);
		return token;
	}

	@Override
	public void saveTokenToRedis(String token,Long id) {
		redisTemplate.opsForValue().set(TOKEN+id.toString(),token,60*2, TimeUnit.MINUTES);
	}

	@Override
	public User hasUser(String name) {
		User user = userMapper.selectUserByName(name);
		if (Objects.nonNull(user)){
			return user;
		}
		return null;
	}

	@Override
	public User hasUserById(long id) {
		return userMapper.selectUserById(id);
	}


	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void register(RegisterReq registerReq) {
		String name = registerReq.getName();
		String stored = PasswordUtil.hashPassword(registerReq.getPassword());
		Long id = userMapper.addUser(name, stored);
		return;
	}

}
