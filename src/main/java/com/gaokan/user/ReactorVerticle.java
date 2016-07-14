package com.gaokan.user;

import com.gaokan.user.handler.ReactorHandler;
import com.gaokan.user.parameter.UrlParameter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

public class ReactorVerticle extends AbstractVerticle {
	private RedisClient redisClient;
	private ReactorHandler userHandler;
	static public long vendorNumber = 0;
	static public long couponNumber = 0;
	static public long essayNumber = 0;
	
	@Override
	public void start(Future<Void> fut) {
		String redisHost = config().getString("redis.host", "localhost");
		int redisPort = config().getInteger("redis.port", 6379);
		String serverIp = config().getString("server.ip", "localhost");
		// Create the redis client
		redisClient = RedisClient.create(vertx, new RedisOptions().setHost(redisHost).setPort(redisPort));
		userHandler = new ReactorHandler(redisClient, serverIp);
		
		//demo data create
		//DemoDataCreate.addDemoVendorCoupon(redisClient);
		
		//Initially fetch database entry number (vendor, coupon, essay)
		InitFetchDataEntryNumber.fetchVendorEntryNumber(redisClient);
		InitFetchDataEntryNumber.fetchCouponEntryNumber(redisClient);
		InitFetchDataEntryNumber.fetchEssayEntryNumber(redisClient);
		
		// Create a router object.
		Router router = Router.router(vertx);
		router.post(UrlParameter.userSignUpUrl).handler(userHandler::handleUserSignUp);
		router.post(UrlParameter.userSignInUrl).handler(userHandler::handleUserSignIn);
		router.post(UrlParameter.userFollowVendorUrl).handler(userHandler::handleUserFollowVendor);
		router.post(UrlParameter.userAddCouponUrl).handler(userHandler::handleUserAddCoupon);
		router.post(UrlParameter.userCouponListGetUrl).handler(userHandler::handleUserCouponListGet);
		router.post(UrlParameter.vendorCouponListGetUrl).handler(userHandler::handleVendorCouponListGet);
		router.post(UrlParameter.userPostEssayUrl).handler(userHandler::handleUserPostEssay);
		router.post(UrlParameter.userPostEssayPicUrl).handler(userHandler::handleUserPostEssayPic);
		router.post(UrlParameter.userEssayListGetUrl).handler(userHandler::handleUserEssayListGet);
		router.post(UrlParameter.vendorAddCouponUrl).handler(userHandler::handleVendorAddCoupon);
		router.route(UrlParameter.wechatDynamicPageGetUrl).handler(userHandler::handlewechatDynamicPageGet);
		router.route("/vendors/*").handler(StaticHandler.create("vendors"));
		router.route("/download/*").handler(StaticHandler.create("download"));
		router.route("/test/*").handler(StaticHandler.create("test"));
		router.post("/formlogo").handler(userHandler::handleFormLogoUpload);
		router.post("/formessaypic").handler(userHandler::handleFormEssayPicUpload);
		
		HttpServerOptions options = new HttpServerOptions().setReuseAddress(true);
		vertx.createHttpServer(options).requestHandler(router::accept).listen(
				// Retrieve the port from the configuration,
				// default to 8080.
				config().getInteger("user.port", 8080), result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
	}
}