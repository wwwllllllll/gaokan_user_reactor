package com.gaokan.user;

import com.gaokan.essay.bean.Essay;

import io.vertx.redis.RedisClient;

public class InitFetchDataEntryNumber {
	static public void fetchEssayEntryNumber(RedisClient redisClient) {
		redisClient.hlen(Essay.class.getName(), r -> {
			if (r.succeeded()) {
				ReactorVerticle.essayNumber = r.result();
			} else {
				System.out.println("redis client query fail!");
			}
		});
	}
}
