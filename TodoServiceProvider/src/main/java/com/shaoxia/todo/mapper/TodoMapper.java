package com.shaoxia.todo.mapper;

import com.shaoxia.model.pojo.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-30 17:06
 */
@Repository
public interface TodoMapper {
	void insertOneTodo(Todo todo);

	int delOneTodo(@Param("id") Long id,@Param("uid") Long uid);

	int delBatchTodo(@Param("ids") List<Long> ids,@Param("uid") Long uid);

	int update(Todo todo,@Param("uid") Long uid);

	int completeOneTodo(@Param("id") long todoId,@Param("uid") Long uid);

	void completeBatchTodo(List<Long> ids,@Param("uid") Long uid);

	List<Integer> selectTodoIdsByIsCompleted(@Param("isCompletedStatus") Integer noCompleted,
											 @Param("uid") Long uid,
											 @Param("offset") Integer offset,
											 @Param("limit") Integer pageSize);

	List<Todo> selectTodosByIds(@Param("ids") List<Integer> ids);

	List<Integer> selectAllTodoIds(@Param("uid") Long uid,
								   @Param("offset")int offset,
								   @Param("limit")Integer pageSize);

	List<Integer> selectIdsByTitleLike(@Param("name") String name,
									   @Param("uid") Long uid,
									   @Param("offset") int offset,
									   @Param("limit") Integer pageSize);

	List<Integer> selectIdsByTitleLikeAndIsCompleted(@Param("name") String name,
													 @Param("status") Integer status,
													 @Param("uid") Long uid,
													 @Param("offset") int offset,
													 @Param("limit") Integer pageSize);

}
