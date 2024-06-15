package com.shaoxia.todo.service.impl;

import com.shaoxia.common.exception.BusinessException;
import com.shaoxia.common.exception.ErrorCode;
import com.shaoxia.todo.mapper.TodoMapper;
import com.shaoxia.model.dto.req.TodoAddReq;
import com.shaoxia.model.dto.req.TodoQueryReq;
import com.shaoxia.model.dto.req.TodoReq;
import com.shaoxia.model.dto.resp.TodoQueryResp;
import com.shaoxia.model.pojo.Todo;
import com.shaoxia.service.TodoService;
import com.shaoxia.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-30 17:12
 */
@Service
@DubboService
public class TodoServiceImpl implements TodoService {
	private static Integer no_completed = 0;
	private static Integer have_completed = 1;

	@Autowired
	private TodoMapper todoMapper;
	@DubboReference
	private UserService userService;

	@Override
	public boolean checkHasUser(long uid) {
		return Objects.nonNull(userService.hasUserById(uid));
	}

	@Override
	public Todo checkTodoValidAndChange(TodoReq todo) {

		if (Objects.isNull(todo) )return null;
		String id = todo.getId();
		if (Objects.isNull(id)) return null;
		long todoId;
		try {
			todoId = Long.parseLong(id);
			if (todoId <= 0L) return null;
		}catch (NumberFormatException e){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"id参数非法");
		}
		Integer isCompleted = todo.getIsCompleted();
		if (Objects.isNull(isCompleted) || !(isCompleted == 1 || isCompleted == 0)) return null;

		String title = todo.getTitle();
		String content = todo.getContent();
		if (StringUtils.isAnyBlank(title,content)) return null;
		if (containsIllegalCharacter(title)) return null;
		if (containEmoji(content)) return null;

		return Todo.builder().id(todoId)
				.title(title)
				.content(content)
				.isCompleted(isCompleted)
				.build();
	}

	@Override
	public Todo checkAddTodoValidAndChange(TodoAddReq todoAddReq) {
		if (Objects.isNull(todoAddReq) )return null;
		Integer isCompleted = todoAddReq.getIsCompleted();
		if (Objects.isNull(isCompleted) || !(isCompleted == 1 || isCompleted == 0)) return null;

		String title = todoAddReq.getTitle();
		String content = todoAddReq.getContent();

		if (StringUtils.isAnyBlank(title,content)) return null;

		if (containsIllegalCharacter(title)) return null;
		if (containEmoji(content)) return null;

		return Todo.builder()
				.title(title)
				.content(content)
				.isCompleted(isCompleted)
				.build();
	}

	@Override
	public boolean checkIdValid(String id) {

		if (Objects.isNull(id)) return false;

		try {
			long todoId = Long.parseLong(id);
			if (todoId <= 0) return false;
		}catch (NumberFormatException e){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"id参数非法");
		}
		return true;
	}


	@Override
	public List<Long> checkAndParseBatchId(List<String> ids) {
		boolean allMatch = ids.stream().allMatch(this::checkIdValid);
		if (!allMatch) return null;
		return ids.stream().map(Long::parseLong).collect(Collectors.toList());
	}

	@Override
	public Todo insertTodo(Todo todo) {
		todoMapper.insertOneTodo(todo);
		return todo;
	}

	@Override
	public int delOneTodo(long id,Long uid) {
		return todoMapper.delOneTodo(id,uid);
	}

	@Override
	public int delBatchTodo(List<Long> ids,Long uid) {
		return todoMapper.delBatchTodo(ids,uid);
	}

	@Override
	public int updateOneTodo(Todo todo, Long uid) {
		return todoMapper.update(todo,uid);
	}

	@Override
	public int completeOneTodo(long todoId,Long uid) {
		return todoMapper.completeOneTodo(todoId,uid);
	}

	@Override
	public void completeBatchTodo(List<Long> ids,Long uid) {
		todoMapper.completeBatchTodo(ids,uid);
	}

	@Override
	public boolean checkQueryReqValid(TodoQueryReq todoQueryReq) {
		if (Objects.isNull(todoQueryReq)) return false;

		Integer choice = todoQueryReq.getChoice();
		if (choice<= -1 || choice >=3) return false;

		Integer page = todoQueryReq.getPage();
		Integer pageSize = todoQueryReq.getPageSize();
		if (page <= 0 || pageSize <= 0) return false;

		return true;
	}

	@Override
	public TodoQueryResp selectNoCompleted(Integer page, Integer pageSize, Long uid) {
		int offset = (page - 1) * pageSize;
		List<Integer> ids = todoMapper.selectTodoIdsByIsCompleted(no_completed,uid,offset,pageSize);
		System.out.println("ids: "+ids);
		if (ids.isEmpty()){
			return null;
		}
		List<Todo> todos = todoMapper.selectTodosByIds(ids);
		return todosToReps(todos);
	}

	@Override
	public TodoQueryResp selectHaveCompleted(Integer page, Integer pageSize, Long uid) {
		int offset = (page - 1) * pageSize;
		List<Integer> ids = todoMapper.selectTodoIdsByIsCompleted(have_completed,uid,offset,pageSize);
		System.out.println("ids: "+ids);
		if (ids.isEmpty()){
			return null;
		}
		List<Todo> todos = todoMapper.selectTodosByIds(ids);
		return todosToReps(todos);
	}

	@Override
	public TodoQueryResp selectAll(Integer page, Integer pageSize, Long uid) {
		int offset = (page - 1) * pageSize;
		List<Integer> ids = todoMapper.selectAllTodoIds(uid,offset,pageSize);
		System.out.println("ids: "+ids);
		if (ids.isEmpty()){
			return null;
		}
		List<Todo> todos = todoMapper.selectTodosByIds(ids);
		return todosToReps(todos);
	}

	@Override
	public TodoQueryResp queryByLike(String name, Integer choice, Integer page, Integer pageSize, Long uid) {
		int offset = (page - 1) * pageSize;
		List<Integer> ids;
		if (choice == 2){
			ids = todoMapper.selectIdsByTitleLike(name,uid,offset,pageSize);
		}else{
			ids = todoMapper.selectIdsByTitleLikeAndIsCompleted(name,choice,uid,offset,pageSize);
		}
		System.out.println("ids: "+ids);
		if (ids.isEmpty()){
			return null;
		}
		List<Todo> todos = todoMapper.selectTodosByIds(ids);
		return todosToReps(todos);
	}


	public static TodoQueryResp todosToReps(List<Todo> todos){
		List<TodoReq> todoReqs = todos.stream().map(
				todo -> {
					return TodoReq.builder()
							.id(todo.getId().toString())
							.title(todo.getTitle())
							.content(todo.getContent())
							.isCompleted(todo.getIsCompleted())
							.build();
				}
		).collect(Collectors.toList());
		return new TodoQueryResp(todoReqs.size(),todoReqs);
	}


	public static boolean containsIllegalCharacter(String str) {
		// 定义非法字符的正则表达式
		String regex = "[^a-zA-Z0-9\u4e00-\u9fa5]|[\\\\p{So}\\\\p{Cn}]|[\\\\uD800-\\\\uDBFF][\\\\uDC00-\\\\uDFFF]";
		// 创建 Pattern 对象
		Pattern pattern = Pattern.compile(regex);
		// 创建 Matcher 对象
		Matcher matcher = pattern.matcher(str);
		// 使用 Matcher 查找是否有非法字符
		return matcher.find();
	}

	public static boolean containEmoji(String str){
		// 定义非法字符的正则表达式
		String regex = "[\\\\p{So}\\\\p{Cn}]|[\\\\uD800-\\\\uDBFF][\\\\uDC00-\\\\uDFFF]";
		// 创建 Pattern 对象
		Pattern pattern = Pattern.compile(regex);
		// 创建 Matcher 对象
		Matcher matcher = pattern.matcher(str);
		// 使用 Matcher 查找是否有非法字符
		return matcher.find();
	}
}
