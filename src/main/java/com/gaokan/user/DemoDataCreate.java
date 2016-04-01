package com.gaokan.user;

import java.util.ArrayList;
import java.util.List;

import com.gaokan.user.bean.Coupon;
import com.gaokan.user.bean.Vendor;

import io.vertx.core.json.Json;
import io.vertx.redis.RedisClient;

public class DemoDataCreate {
	static public void addDemoVendorCoupon(RedisClient redisClient) {
		Coupon coupon = new Coupon();
		coupon.generateId();
		coupon.setName("feiniuwang.coupon1");
		coupon.setDescription("飞牛网的8折优惠券");
		coupon.setPicLink("/vendors/feiniuwang/coupons/coupon1.jpg");
		String jsonStrCoupon = Json.encode(coupon);
		redisClient.hset(coupon.getClass().getName(), String.valueOf(coupon.getId()), jsonStrCoupon, r -> {
			if (r.succeeded()) {
				Vendor vendor = new Vendor();
				vendor.generateId();
				vendor.setVendorName("飞牛网");
				vendor.setLogoLink("/vendors/feiniuwang/logo/feiniuwang.jpg");
				List<Coupon> listCoupon = new ArrayList<Coupon>();
				listCoupon.add(coupon);
				vendor.setCoupons(listCoupon);
				String jsonStrVendor = Json.encode(vendor);
				redisClient.hset(vendor.getClass().getName(), String.valueOf(vendor.getId()), jsonStrVendor, s -> {
					if (r.succeeded()) {
						System.out.println("redis hset demo data ok");
					} else {
						System.out.println("redis hset fail, " + r.cause());
					}
				});
			} else {
				System.out.println("redis hset fail, " + r.cause());
			}
		});
	}
}
