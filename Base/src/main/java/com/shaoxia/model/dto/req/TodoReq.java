package com.shaoxia.model.dto.req;

import com.shaoxia.model.pojo.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-31 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoReq {
	private String id;
	private String title;
	private String content;
	private Integer isCompleted;

	public static TodoReq todoToReq(Todo todo){
		return TodoReq.builder()
				.id(todo.getId().toString())
				.title(todo.getTitle())
				.content(todo.getContent())
				.isCompleted(todo.getIsCompleted())
				.build();
	}
}
