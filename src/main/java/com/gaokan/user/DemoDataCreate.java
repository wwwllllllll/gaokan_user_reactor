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
		
		Coupon coupon2 = new Coupon();
		coupon2.generateId();
		coupon2.setName("starbucks.coupon1");
		coupon2.setDescription("星巴克代金券");
		coupon2.setPicLink("/vendors/starbucks/coupons/coupon1.jpg");
		String jsonStrCoupon2 = Json.encode(coupon2);
		Coupon coupon3 = new Coupon();
		coupon3.generateId();
		coupon3.setName("starbucks.coupon2");
		coupon3.setDescription("星巴克优惠券");
		coupon3.setPicLink("/vendors/starbucks/coupons/coupon2.jpg");
		String jsonStrCoupon3 = Json.encode(coupon3);
		redisClient.hset(coupon2.getClass().getName(), String.valueOf(coupon2.getId()), jsonStrCoupon2, r -> {
			if (r.succeeded()) {
			} else {
				System.out.println("redis hset fail, " + r.cause());
			}
		});
		redisClient.hset(coupon3.getClass().getName(), String.valueOf(coupon3.getId()), jsonStrCoupon3, r -> {
			if (r.succeeded()) {
				Vendor vendor = new Vendor();
				vendor.generateId();
				vendor.setVendorName("星巴克");
				vendor.setLogoLink("/vendors/starbucks/logo/starbucks.jpg");
				List<Coupon> listCoupon = new ArrayList<Coupon>();
				listCoupon.add(coupon2);
				listCoupon.add(coupon3);
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
