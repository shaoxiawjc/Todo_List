package com.shaoxia.common.constant;

public interface RedisKey {
	String REDIS_KEY_HEAD = "shaoxia:todo:";

	String TOKEN = REDIS_KEY_HEAD+"token:";
}
