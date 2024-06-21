package com.shaoxia.todo.controller;

import com.shaoxia.common.exception.BusinessException;
import com.shaoxia.common.exception.ErrorCode;
import com.shaoxia.common.utils.JwtUtils;
import com.shaoxia.model.dto.BaseResponse;
import com.shaoxia.model.dto.ResultUtils;
import com.shaoxia.model.dto.req.TodoAddReq;
import com.shaoxia.model.dto.req.TodoQueryReq;
import com.shaoxia.model.dto.req.TodoReq;
import com.shaoxia.model.dto.resp.TodoQueryResp;
import com.shaoxia.model.pojo.Todo;
import com.shaoxia.ratelimit.annotation.RateLimit;
import com.shaoxia.service.TodoService;
import com.shaoxia.todo.service.impl.TodoServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author wjc28
 * @version 1.0
 * @description: 待办控制层
 * @date 2024-05-30 17:10
 */
@RestController
@CrossOrigin
@RequestMapping("/todo")
public class TodoController {
	@Resource
	private TodoService todoService;

	private static Long getUidFromReq(HttpServletRequest request){
		String uidS;
		long uid;
		try{
			uidS = JwtUtils.getUid(request);
			System.out.println(uidS);
			uid = Long.parseLong(uidS);
		}catch (Exception e){
			throw new BusinessException(ErrorCode.NO_AUTH,"token异常");
		}
		return uid;
	}

	/**
	 * 添加一条待办
	 * @param todoAddReq
	 * @param request
	 * @return
	 */
	@PutMapping("/add")
	@RateLimit(key = "shaoxia:todo:todo:addOneTodo")
	public BaseResponse<TodoReq> addOneTodo(@RequestBody TodoAddReq todoAddReq, HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}

		Todo todo = todoService.checkAddTodoValidAndChange(todoAddReq);
		if (Objects.isNull(todo)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
		}
		todo.setUid(uid);
		return ResultUtils.success(TodoReq.todoToReq(todoService.insertTodo(todo)));
	}


	/**
	 * 查询待办
	 * @param todoQueryReq
	 * @param request
	 * @return
	 */
	@GetMapping("/query")
	@RateLimit(key = "shaoxia:todo:todo:queryTodo",maxRequests = 10)
	public BaseResponse<TodoQueryResp> queryTodo(@RequestBody TodoQueryReq todoQueryReq,
												 HttpServletRequest request){
		if (!todoService.checkQueryReqValid(todoQueryReq)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不合法");
		}
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		Integer choice = todoQueryReq.getChoice();
		Integer page = todoQueryReq.getPage();
		Integer pageSize = todoQueryReq.getPageSize();
		TodoQueryResp resp = new TodoQueryResp();
		if (choice == 0){
			resp = todoService.selectNoCompleted(page,pageSize,uid);
		} else if (choice == 1) {
			resp = todoService.selectHaveCompleted(page,pageSize,uid);
		}else if (choice == 2){
			resp = todoService.selectAll(page,pageSize,uid);
		}

		return ResultUtils.success(resp);
	}

	/**
	 * 根据标题名称查询待办
	 * @param name
	 * @param todoQueryReq
	 * @param request
	 * @return
	 */
	@GetMapping("/query/like")
	@RateLimit(key = "shaoxia:todo:todo:queryByLike",maxRequests = 10)
	public BaseResponse<TodoQueryResp> queryByLike(@RequestParam("name") String name,
													@RequestBody TodoQueryReq todoQueryReq,
													HttpServletRequest request) {
		System.out.println(name+todoQueryReq);
		if (TodoServiceImpl.containsIllegalCharacter(name) || (!todoService.checkQueryReqValid(todoQueryReq))) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不合法");
		}
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		Integer choice = todoQueryReq.getChoice();
		Integer page = todoQueryReq.getPage();
		Integer pageSize = todoQueryReq.getPageSize();
		TodoQueryResp resp = todoService.queryByLike(name,choice,page,pageSize,uid);
		return ResultUtils.success(resp);
	}

	/**
	 * 删除一条待办
	 * @param id
	 * @param request
	 * @return
	 */
	@DeleteMapping("/del")
	@RateLimit(key = "shaoxia:todo:todo:delOneTodo",maxRequests = 10)
	public BaseResponse<Boolean> delOneTodo(@RequestParam("id") String id,HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		if (!todoService.checkIdValid(id)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数非法");
		}
		long todoId = Long.parseLong(id);
		int i = todoService.delOneTodo(todoId,uid);

		if (i < 1) {
			throw new BusinessException(ErrorCode.NULL_ERROR,"不存在此待办");
		}
		return ResultUtils.success(true);
	}

	/**
	 * 删除多条待办
	 * @param idList
	 * @param request
	 * @return
	 */
	@DeleteMapping("/dels")
	@RateLimit(key = "shaoxia:todo:todo:delBatchTodo")
	public BaseResponse<Void> delBatchTodo(@RequestParam("ids") List<String> idList,HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		List<Long> ids = todoService.checkAndParseBatchId(idList);
		todoService.delBatchTodo(ids,uid);
		return ResultUtils.success(null);
	}

	/**
	 * 更新待办信息
	 * @param todoReq
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	@RateLimit(key = "shaoxia:todo:todo:updateOneTodo")
	public BaseResponse<TodoReq> updateOneTodo(@RequestBody TodoReq todoReq,HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		Todo todo = todoService.checkTodoValidAndChange(todoReq);
		if (Objects.isNull(todo)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
		}
		todoService.updateOneTodo(todo,uid);
		return ResultUtils.success(todoReq);
	}


	/**
	 * 完成一条待办
	 * @param id
	 * @param request
	 * @return
	 */
	@DeleteMapping("/complete")
	@RateLimit(key = "shaoxia:todo:todo:completeOneTodo",maxRequests = 10)
	public BaseResponse<Boolean> completeOneTodo(@RequestParam("id") String id,HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		System.out.println("传入的id为"+id);
		if (!todoService.checkIdValid(id)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数非法");
		}
		long todoId = Long.parseLong(id);
		int i = todoService.completeOneTodo(todoId,uid);

		if (i < 1) {
			throw new BusinessException(ErrorCode.NULL_ERROR,"不存在此待办");
		}
		return ResultUtils.success(true);
	}

	/**
	 * 完成多条待办
	 * @param idList
	 * @param request
	 * @return
	 */
	@DeleteMapping("/completes")
	@RateLimit(key = "shaoxia:todo:todo:completeBatchTodo")
	public BaseResponse<Void> completeBatchTodo(@RequestParam("ids") List<String> idList,HttpServletRequest request){
		Long uid = getUidFromReq(request);
		if (!todoService.checkHasUser(uid)){
			throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
		}
		List<Long> ids = todoService.checkAndParseBatchId(idList);
		todoService.completeBatchTodo(ids,uid);
		return ResultUtils.success(null);
	}

}
