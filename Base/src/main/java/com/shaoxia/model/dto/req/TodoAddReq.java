package com.shaoxia.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-05 11:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoAddReq {
	private String title;
	private String content;
	private Integer isCompleted;
}
