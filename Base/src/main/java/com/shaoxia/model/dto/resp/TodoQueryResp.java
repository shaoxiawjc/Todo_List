package com.shaoxia.model.dto.resp;

import com.shaoxia.model.dto.req.TodoReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-08 12:40
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TodoQueryResp {
	private Integer resultNum;
	private List<TodoReq> todos;
}
