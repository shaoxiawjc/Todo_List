package com.shaoxia.user.mapper;

import com.shaoxia.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wjc28
 * @version 1.0
 * @description: 用户mapper层
 * @date 2024-05-22 20:24
 */
@Repository
public interface UserMapper {
	User selectUserByName(@Param("name") String name);

	Long addUser(@Param("name") String name, @Param("password") String password);

	User selectUserById(long id);
}
