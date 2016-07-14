package com.gaokan.user;

import com.gaokan.user.bean.Coupon;
import com.gaokan.user.bean.Vendor;
import com.gaokan.essay.bean.Essay;

import io.vertx.redis.RedisClient;

public class InitFetchDataEntryNumber {
	static public void fetchVendorEntryNumber(RedisClient redisClient) {
		redisClient.hlen(Vendor.class.getName(), r -> {
			if (r.succeeded()) {
				ReactorVerticle.vendorNumber = r.result();
			} else {
				System.out.println("redis client query fail!");
			}
		});
	}
	
	static public void fetchCouponEntryNumber(RedisClient redisClient) {
		redisClient.hlen(Coupon.class.getName(), r -> {
			if (r.succeeded()) {
				ReactorVerticle.couponNumber = r.result();
			} else {
				System.out.println("redis client query fail!");
			}
		});
	}
	
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
