package com.gaokan.user.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gaokan.user.bean.UserInfo;
import com.gaokan.user.bean.UserSignInRequest;
import com.gaokan.user.bean.UserSignUpRequest;
import com.gaokan.user.bean.Vendor;
import com.gaokan.essay.bean.EssayData;
import com.gaokan.essay.bean.EssayRequest;
import com.gaokan.essay.bean.EssayResponse;
import com.gaokan.essay.parameter.UrlParameter;
import com.gaokan.user.bean.Coupon;
import com.gaokan.user.bean.Ip2CouponList;
import com.gaokan.user.bean.UserAddCouponRequest;
import com.gaokan.user.bean.UserCommonResponse;
import com.gaokan.user.bean.UserCouponListGetRequest;
import com.gaokan.user.bean.UserCouponListGetResponse;
import com.gaokan.user.bean.UserFollowVendorRequest;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;

public class ReactorHandler {
	private RedisClient redisClient;
	private String serverIp;

	public ReactorHandler(RedisClient redisClient, String serverIp) {
		super();
		this.redisClient = redisClient;
		this.serverIp = serverIp;
	}

	public void handleUserSignUp(RoutingContext routingContext) {
		HttpServerResponse serverResponse = routingContext.response();
		serverResponse.putHeader("content-type", "application/json");
		UserCommonResponse respBean = new UserCommonResponse();
		respBean.setResultCode(1);
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(r -> {
			try {
				UserSignUpRequest reqBean = Json.decodeValue(r.toString(), UserSignUpRequest.class);
				redisClient.hget(UserInfo.class.getName(), reqBean.getCellNum(), s -> {
					if (s.succeeded()) {
						if (s.result() == null) {
							UserInfo userInfo = new UserInfo();
							userInfo.setCellNum(reqBean.getCellNum());
							userInfo.setPassword(reqBean.getPassword());
							userInfo.setNickName(reqBean.getNickName());
							List<String> ipList = new ArrayList<String>();
							ipList.add(req.remoteAddress().host());
							userInfo.setIpAddr(ipList);
							String jsonStrUserInfo = Json.encode(userInfo);
							redisClient.hset(userInfo.getClass().getName(), userInfo.getCellNum(), jsonStrUserInfo,
									t -> {
								if (t.succeeded()) {
									respBean.setResultCode(0);
									respBean.setResult("注册成功!");
									String resp = Json.encode(respBean);
									serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
									serverResponse.write(resp).end();
								} else {
									System.out.println("Connection or Operation Failed " + t.cause());
									respBean.setResult("服务器忙,请稍候再试!");
									String resp = Json.encode(respBean);
									serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
									serverResponse.write(resp).end();
								}
							});
						} else {
							respBean.setResult("该手机号已经注册,请直接登录!");
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						}
					} else {
						System.out.println("redis client query fail!");
						respBean.setResult("服务器忙,请稍候再试!");
						String resp = Json.encode(respBean);
						serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
						serverResponse.write(resp).end();
					}
				});
			} catch (DecodeException e) {
				// TODO: handle exception
				e.printStackTrace();
				respBean.setResult("无法解析该注册请求内容!");
				String resp = Json.encode(respBean);
				serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
				serverResponse.write(resp).end();
			}
		});
	}

	public void handleUserSignIn(RoutingContext routingContext) {
		HttpServerResponse serverResponse = routingContext.response();
		serverResponse.putHeader("content-type", "application/json");
		UserCommonResponse respBean = new UserCommonResponse();
		respBean.setResultCode(1);
		HttpServerRequest req = routingContext.request();
		String ip = req.remoteAddress().host();
		req.bodyHandler(r -> {
			try {
				UserSignInRequest reqBean = Json.decodeValue(r.toString(), UserSignInRequest.class);
				redisClient.hget(UserInfo.class.getName(), reqBean.getCellNum(), s -> {
					if (s.succeeded()) {
						if (s.result() != null) {
							UserInfo userInfo = Json.decodeValue(s.result(), UserInfo.class);
							if (userInfo.getPassword().equals(reqBean.getPassword())) {
								respBean.setResultCode(0);
								respBean.setResult("登陆成功!");
								String resp = Json.encode(respBean);
								serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
								serverResponse.write(resp).end();
								redisClient.hget(Ip2CouponList.class.getName(), ip, t -> {
									if (t.succeeded()) {
										if (t.result() != null) {
											Ip2CouponList map = Json.decodeValue(t.result(), Ip2CouponList.class);
											userInfo.setCoupons(map.getCoupons());
											String jsonStrUserInfo = Json.encode(userInfo);
											redisClient.hset(userInfo.getClass().getName(), userInfo.getCellNum(), jsonStrUserInfo,
													u -> {
												if (!t.succeeded()) {
													System.out.println("redis client query fail!");
												}
											});
										}
									} else {
										System.out.println("redis client query fail!");
									}
								});
							} else {
								respBean.setResult("密码或手机号错误!");
								String resp = Json.encode(respBean);
								serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
								serverResponse.write(resp).end();
							}
						} else {
							respBean.setResult("该手机号还未注册!");
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						}
					} else {
						System.out.println("redis client query fail!");
						respBean.setResult("服务器忙,请稍候再试!");
						String resp = Json.encode(respBean);
						serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
						serverResponse.write(resp).end();
					}
				});
			} catch (DecodeException e) {
				// TODO: handle exception
				e.printStackTrace();
				respBean.setResult("无法解析该登陆请求内容!");
				String resp = Json.encode(respBean);
				serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
				serverResponse.write(resp).end();
			}
		});
	}

	public void handleUserFollowVendor(RoutingContext routingContext) {
		HttpServerResponse serverResponse = routingContext.response();
		serverResponse.putHeader("content-type", "application/json");
		UserCommonResponse respBean = new UserCommonResponse();
		respBean.setResultCode(1);
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(r -> {
			try {
				UserFollowVendorRequest reqBean = Json.decodeValue(r.toString(), UserFollowVendorRequest.class);
				redisClient.hget(UserInfo.class.getName(), reqBean.getUserCellNum(), s -> {
					if (s.succeeded()) {
						if (s.result() != null) {
							UserInfo userInfo = Json.decodeValue(s.result(), UserInfo.class);
							redisClient.hget(Vendor.class.getName(), String.valueOf(reqBean.getVendorId()), t -> {
								if (t.succeeded()) {
									if (t.result() != null) {
										List<Long> list = userInfo.getFollowVendors();
										if (list != null && list.contains(Long.valueOf(reqBean.getVendorId()))) {
											respBean.setResult("已经关注过该商家!");
											String resp = Json.encode(respBean);
											serverResponse.putHeader("content-length",
													String.valueOf(resp.getBytes().length));
											serverResponse.write(resp).end();
										} else {
											if (list == null) {
												list = new ArrayList<Long>();
											}
											list.add(Long.valueOf(reqBean.getVendorId()));
											userInfo.setFollowVendors(list);
											String jsonStrUserInfo = Json.encode(userInfo);
											redisClient.hset(userInfo.getClass().getName(), userInfo.getCellNum(),
													jsonStrUserInfo, u -> {
												if (u.succeeded()) {
													respBean.setResultCode(0);
													respBean.setResult("关注该商家成功!");
													String resp = Json.encode(respBean);
													serverResponse.putHeader("content-length",
															String.valueOf(resp.getBytes().length));
													serverResponse.write(resp).end();
												} else {
													System.out.println("Connection or Operation Failed " + t.cause());
													respBean.setResult("服务器忙,请稍候再试!");
													String resp = Json.encode(respBean);
													serverResponse.putHeader("content-length",
															String.valueOf(resp.getBytes().length));
													serverResponse.write(resp).end();
												}
											});
										}
									} else {
										respBean.setResult("该商家不存在或已退出平台!");
										String resp = Json.encode(respBean);
										serverResponse.putHeader("content-length",
												String.valueOf(resp.getBytes().length));
										serverResponse.write(resp).end();
									}
								} else {
									System.out.println("redis client query fail!");
									respBean.setResult("服务器忙,请稍候再试!");
									String resp = Json.encode(respBean);
									serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
									serverResponse.write(resp).end();
								}
							});
						} else {
							respBean.setResult("该手机帐号异常!");
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						}
					} else {
						System.out.println("redis client query fail!");
						respBean.setResult("服务器忙,请稍候再试!");
						String resp = Json.encode(respBean);
						serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
						serverResponse.write(resp).end();
					}
				});
			} catch (DecodeException e) {
				// TODO: handle exception
				respBean.setResult("无法解析该关注请求内容!");
				String resp = Json.encode(respBean);
				serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
				serverResponse.write(resp).end();
			}
		});
	}

	public void handleUserAddCoupon(RoutingContext routingContext) {
		HttpServerResponse serverResponse = routingContext.response();
		serverResponse.putHeader("content-type", "application/json");
		UserCommonResponse respBean = new UserCommonResponse();
		respBean.setResultCode(1);
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(r -> {
			try {
				UserAddCouponRequest reqBean = Json.decodeValue(r.toString(), UserAddCouponRequest.class);
				redisClient.hget(UserInfo.class.getName(), reqBean.getUserCellNum(), s -> {
					if (s.succeeded()) {
						if (s.result() != null) {
							UserInfo userInfo = Json.decodeValue(s.result(), UserInfo.class);
							redisClient.hget(Coupon.class.getName(), String.valueOf(reqBean.getCouponId()), t -> {
								if (t.succeeded()) {
									if (t.result() != null) {
										Coupon coupon = Json.decodeValue(t.result(), Coupon.class);
										List<Coupon> list = userInfo.getCoupons();
										if (list != null && list.contains(coupon)) {
											respBean.setResult("已经收藏过该优惠券!");
											String resp = Json.encode(respBean);
											serverResponse.putHeader("content-length",
													String.valueOf(resp.getBytes().length));
											serverResponse.write(resp).end();
										} else {
											if (list == null) {
												list = new ArrayList<Coupon>();
											}
											list.add(coupon);
											userInfo.setCoupons(list);
											String jsonStrUserInfo = Json.encode(userInfo);
											redisClient.hset(userInfo.getClass().getName(), userInfo.getCellNum(),
													jsonStrUserInfo, u -> {
												if (u.succeeded()) {
													respBean.setResultCode(0);
													respBean.setResult("收藏该优惠券成功!");
													String resp = Json.encode(respBean);
													serverResponse.putHeader("content-length",
															String.valueOf(resp.getBytes().length));
													serverResponse.write(resp).end();
												} else {
													System.out.println("Connection or Operation Failed " + t.cause());
													respBean.setResult("服务器忙,请稍候再试!");
													String resp = Json.encode(respBean);
													serverResponse.putHeader("content-length",
															String.valueOf(resp.getBytes().length));
													serverResponse.write(resp).end();
												}
											});
										}
									} else {
										respBean.setResult("该优惠券不存在或已退出平台!");
										String resp = Json.encode(respBean);
										serverResponse.putHeader("content-length",
												String.valueOf(resp.getBytes().length));
										serverResponse.write(resp).end();
									}
								} else {
									System.out.println("redis client query fail!");
									respBean.setResult("服务器忙,请稍候再试!");
									String resp = Json.encode(respBean);
									serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
									serverResponse.write(resp).end();
								}
							});
						} else {
							respBean.setResult("该手机帐号异常!");
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						}
					} else {
						System.out.println("redis client query fail!");
						respBean.setResult("服务器忙,请稍候再试!");
						String resp = Json.encode(respBean);
						serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
						serverResponse.write(resp).end();
					}
				});
			} catch (DecodeException e) {
				// TODO: handle exception
				e.printStackTrace();
				respBean.setResult("无法解析该收藏请求内容!");
				String resp = Json.encode(respBean);
				serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
				serverResponse.write(resp).end();
			}
		});
	}

	public void handleUserCouponListGet(RoutingContext routingContext) {
		HttpServerResponse serverResponse = routingContext.response();
		serverResponse.putHeader("content-type", "application/json");
		UserCouponListGetResponse respBean = new UserCouponListGetResponse();
		respBean.setResultCode(1);
		HttpServerRequest req = routingContext.request();
		req.bodyHandler(r -> {
			try {
				UserCouponListGetRequest reqBean = Json.decodeValue(r.toString(), UserCouponListGetRequest.class);
				redisClient.hget(UserInfo.class.getName(), reqBean.getCellNum(), s -> {
					if (s.succeeded()) {
						if (s.result() != null) {
							UserInfo userInfo = Json.decodeValue(s.result(), UserInfo.class);
							respBean.setResultCode(0);
							respBean.setResult("获取用和收藏优惠券列表成功!");
							respBean.setCoupons(userInfo.getCoupons());
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						} else {
							respBean.setResult("该手机帐号异常!");
							String resp = Json.encode(respBean);
							serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
							serverResponse.write(resp).end();
						}
					} else {
						System.out.println("redis client query fail!");
						respBean.setResult("服务器忙,请稍候再试!");
						String resp = Json.encode(respBean);
						serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
						serverResponse.write(resp).end();
					}
				});
			} catch (DecodeException e) {
				// TODO: handle exception
				e.printStackTrace();
				respBean.setResult("无法解析该请求内容!");
				String resp = Json.encode(respBean);
				serverResponse.putHeader("content-length", String.valueOf(resp.getBytes().length));
				serverResponse.write(resp).end();
			}
		});
	}

	public void handlewechatDynamicPageGet(RoutingContext routingContext) {
		HttpServerRequest req = routingContext.request();
		String cellNum = req.getParam("cellNum");
		String essayType = req.getParam("essayType");
		String essayId = req.getParam("essayId");
		String ip = req.remoteAddress().host();
		HttpServerResponse serverResponse = routingContext.response();

		redisClient.hget(UserInfo.class.getName(), cellNum, r -> {
			if (r.succeeded()) {
				if (r.result() != null) {
					UserInfo userInfo = Json.decodeValue(r.result(), UserInfo.class);
					HttpClient client = routingContext.vertx().createHttpClient();
					HttpClientRequest request = client.postAbs(UrlParameter.essayGetUrl, response -> {
						System.out.println("Received response with status code " + response.statusCode());
						response.bodyHandler(buffer -> {
							String jsonResp = buffer.toString().replaceAll("\\\\n", "<br/>\\\\n");
							EssayResponse essayResp = Json.decodeValue(jsonResp, EssayResponse.class);
							String essayData = essayResp.getData().getEssayData();
							String dynamicPage = "<body>\n<head><meta charset=\"utf-8\" /></head>\n" + "<center><h1>"
									+ essayResp.getData().getEssayTitle() + "</h1></center>\n" + "<font size=6>"
									+ essayData + "</br>\n";
							if (userInfo.getCoupons() != null) {
								Iterator<Coupon> it = userInfo.getCoupons().iterator();
								while (it.hasNext()) {
									Coupon coupon = it.next();
									dynamicPage += coupon.getDescription();
									dynamicPage += "</br>\n" + "<img src=\"" + coupon.getPicLink() + "\">";
								}
							}
							dynamicPage += "</br>\n</font>" + "<a href=\"" + com.gaokan.user.parameter.UrlParameter.appDownloadUrl
									+ "\">高看一眼,新型社交应用!写段子神器!点本链接下载...</a>" + "</body>";
							serverResponse.putHeader("content-type", "text/html").end(dynamicPage);

							redisClient.hget(Ip2CouponList.class.getName(), ip, s -> {
								if (s.succeeded()) {
									if (s.result() == null) {
										Ip2CouponList map = new Ip2CouponList();
										map.setIp(ip);
										List<Coupon> list = new ArrayList<Coupon>();
										boolean changed = list.addAll(userInfo.getCoupons());
										map.setCoupons(list);
										String jsonStrMap = Json.encode(map);
										if (changed) {
											redisClient.hset(Ip2CouponList.class.getName(), ip, jsonStrMap, t-> {
												if (!t.succeeded()) {
													System.out.println("redis client set fail!");
												}
											});
										}
									} else {
										Ip2CouponList map = Json.decodeValue(s.result(), Ip2CouponList.class);
										if (userInfo.getCoupons() != null) {
											Iterator<Coupon> iter = userInfo.getCoupons().iterator();
											boolean changed = false;
											while (iter.hasNext()) {
												if (!map.getCoupons().contains(iter.next())) {
													map.getCoupons().add(iter.next());
													changed = true;
												}
											}
											if (changed) {
												String jsonStrMap = Json.encode(map);
												redisClient.hset(Ip2CouponList.class.getName(), ip, jsonStrMap, t-> {
													if (!t.succeeded()) {
														System.out.println("redis client set fail!");
													}
												});
											}
										}
									}
								} else {
									System.out.println("redis client query fail!");
								}
							});
						});
					});
					// Now do stuff with the request
					request.putHeader("content-type", "application/json");
					EssayRequest essayReq = new EssayRequest();
					essayReq.setUserId(cellNum);
					essayReq.setEssayType(Integer.valueOf(essayType));
					essayReq.setEssayId(Integer.valueOf(essayId));
					//essayReq.setUserId("111111");
					//essayReq.setEssayType(1);
					//essayReq.setEssayId(100001);
					String jsonReq = Json.encodePrettily(essayReq);
					request.putHeader("content-length", String.valueOf(jsonReq.getBytes().length));
					request.write(jsonReq);
					request.end();
				} else {
					String dynamicPage = "原分享文章已删除!";
					serverResponse.putHeader("content-type", "text/html").end(dynamicPage);
				}
			} else {
				System.out.println("redis client query fail!");
				String dynamicPage = "服务器忙,请稍候再试!";
				serverResponse.putHeader("content-type", "text/html").end(dynamicPage);
			}
		});
	}
}
