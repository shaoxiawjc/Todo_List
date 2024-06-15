package com.shaoxia.service;


import com.shaoxia.model.dto.req.TodoAddReq;
import com.shaoxia.model.dto.req.TodoQueryReq;
import com.shaoxia.model.dto.req.TodoReq;
import com.shaoxia.model.dto.resp.TodoQueryResp;
import com.shaoxia.model.pojo.Todo;

import java.util.List;

public interface TodoService {
	boolean checkHasUser(long uid);
	Todo checkTodoValidAndChange(TodoReq todo);

	Todo checkAddTodoValidAndChange(TodoAddReq todoAddReq);

	boolean checkIdValid(String id);

	List<Long> checkAndParseBatchId(List<String> ids);

	Todo insertTodo(Todo todo);

	int delOneTodo(long id,Long uid);

	int delBatchTodo(List<Long> ids,Long uid);


	int updateOneTodo(Todo todo,Long uid);

	int completeOneTodo(long todoId,Long uid);

	void completeBatchTodo(List<Long> ids,Long uid);

	boolean checkQueryReqValid(TodoQueryReq todoQueryReq);

	TodoQueryResp selectNoCompleted(Integer page, Integer pageSize, Long uid);

	TodoQueryResp selectHaveCompleted(Integer page, Integer pageSize, Long uid);

	TodoQueryResp selectAll(Integer page, Integer pageSize, Long uid);


	TodoQueryResp queryByLike(String name, Integer choice, Integer page, Integer pageSize, Long uid);
}
