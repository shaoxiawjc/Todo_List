package com.shaoxia.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-08 12:42
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TodoQueryReq implements Serializable {
	/**
	 * 0 代表未完成 1代表完成 2代表所有
	 */
	private Integer choice;

	private Integer page;

	private Integer pageSize;
}
