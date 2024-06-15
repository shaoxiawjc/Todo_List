package com.shaoxia.user.controller;

import com.shaoxia.common.exception.BusinessException;
import com.shaoxia.common.exception.ErrorCode;
import com.shaoxia.model.dto.BaseResponse;
import com.shaoxia.model.dto.ResultUtils;
import com.shaoxia.model.dto.req.LoginReq;
import com.shaoxia.model.dto.req.RegisterReq;
import com.shaoxia.model.dto.resp.LoginResp;
import com.shaoxia.model.pojo.User;
import com.shaoxia.ratelimit.annotation.RateLimit;
import com.shaoxia.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户控制层
 * @date 2024-05-22 19:54
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	@Resource
	private UserService userService;

	@PostMapping("/login")
	@RateLimit(key = "shaoxia:todo:user:login",maxRequests = 5)
	public BaseResponse<LoginResp> login(@RequestParam("username") String username,
										 @RequestParam("password") String password,
										 HttpServletRequest request) {
		LoginReq loginReq = new LoginReq(username,password);
		// 检查参数是否合法
		if (!userService.checkValid(loginReq)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
		}

		// 检查是否有用户
		String inputName = loginReq.getUsername();
		User user = userService.hasUser(inputName);
		if (Objects.isNull(user)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}

		// 检查密码是否正确
		Long id = userService.checkLogin(user, loginReq);
		if (id<=-1){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
		}
		String token = userService.getToken(user);
		userService.saveTokenToRedis(token,id);
		return ResultUtils.success(new LoginResp(token));
	}

	@PostMapping("/register")
	@RateLimit(key = "shaoxia:todo:user:register",maxRequests = 5)
	public BaseResponse<Boolean> reg(@RequestBody RegisterReq registerReq){
		System.out.println(registerReq);
		// 检查参数格式
		if (!userService.checkValid(registerReq)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
		}

		// 查看用户是否存在
		String name = registerReq.getName();
		User user = userService.hasUser(name);
		if (Objects.nonNull(user)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名已存在");
		}
		userService.register(registerReq);
		return ResultUtils.success(Boolean.TRUE);
	}
}
