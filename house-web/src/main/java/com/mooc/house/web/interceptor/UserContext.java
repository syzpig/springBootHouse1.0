package com.mooc.house.web.interceptor;

import com.mooc.house.common.model.User;
/**
 *创建ThreadLocal  吧用户放入其中是为了业务可以直接获取对象进行业务处理，然后在AuthActionInterceptor对象中进行登录验证
 */
public class UserContext {
	private static final ThreadLocal<User> USER_HODLER = new ThreadLocal<>();

	//吧用户放入ThreadLocal中
	public static void setUser(User user){
		USER_HODLER.set(user);
	}
	//业务处理完及时清理ThreadLocal清楚用户 每次请求都不一样
	public static void remove(){
		USER_HODLER.remove();
	}
	//获取用户对象
	public static User getUser(){
		return USER_HODLER.get();
	}
}
