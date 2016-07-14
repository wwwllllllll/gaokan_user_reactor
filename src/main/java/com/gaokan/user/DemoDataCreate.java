package com.gaokan.user;

import java.util.ArrayList;
import java.util.List;

import com.gaokan.user.bean.Coupon;
import com.gaokan.user.bean.Vendor;

import io.vertx.core.json.Json;
import io.vertx.redis.RedisClient;

public class DemoDataCreate {
	public static final String vendorFeiniu = "13811111111";
	public static final String vendorStarbucks = "13822222222";
	static public void addDemoVendorCoupon(RedisClient redisClient) {
		Coupon coupon = new Coupon();
		coupon.setCouponId(vendorFeiniu + "01");
		coupon.setName("飞牛网.coupon1");
		coupon.setDescription("飞牛网的8折优惠券");
		coupon.setPicLink("/vendors/飞牛网/coupons/coupon1.jpg");
		String jsonStrCoupon = Json.encode(coupon);
		redisClient.hset(coupon.getClass().getName(), coupon.getCouponId(), jsonStrCoupon, r -> {
			if (r.succeeded()) {
				Vendor vendor = new Vendor();
				vendor.setVendorId(vendorFeiniu);
				vendor.setVendorName("飞牛网");
				vendor.setLogoLink("/vendors/飞牛网/logo/logo.jpg");
				List<Coupon> listCoupon = new ArrayList<Coupon>();
				listCoupon.add(coupon);
				vendor.setCoupons(listCoupon);
				String jsonStrVendor = Json.encode(vendor);
				redisClient.hset(vendor.getClass().getName(), vendor.getVendorId(), jsonStrVendor, s -> {
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
		coupon2.setCouponId(vendorStarbucks + "01");
		coupon2.setName("星巴克.coupon1");
		coupon2.setDescription("星巴克代金券");
		coupon2.setPicLink("/vendors/星巴克/coupons/coupon1.jpg");
		String jsonStrCoupon2 = Json.encode(coupon2);
		Coupon coupon3 = new Coupon();
		coupon3.setCouponId(vendorStarbucks + "02");
		coupon3.setName("星巴克.coupon2");
		coupon3.setDescription("星巴克优惠券");
		coupon3.setPicLink("/vendors/星巴克/coupons/coupon2.jpg");
		String jsonStrCoupon3 = Json.encode(coupon3);
		redisClient.hset(coupon2.getClass().getName(), coupon2.getCouponId(), jsonStrCoupon2, r -> {
			if (r.succeeded()) {
			} else {
				System.out.println("redis hset fail, " + r.cause());
			}
		});
		redisClient.hset(coupon3.getClass().getName(), coupon3.getCouponId(), jsonStrCoupon3, r -> {
			if (r.succeeded()) {
				Vendor vendor = new Vendor();
				vendor.setVendorId(vendorStarbucks);
				vendor.setVendorName("星巴克");
				vendor.setLogoLink("/vendors/星巴克/logo/logo.jpg");
				List<Coupon> listCoupon = new ArrayList<Coupon>();
				listCoupon.add(coupon2);
				listCoupon.add(coupon3);
				vendor.setCoupons(listCoupon);
				String jsonStrVendor = Json.encode(vendor);
				redisClient.hset(vendor.getClass().getName(), vendor.getVendorId(), jsonStrVendor, s -> {
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
