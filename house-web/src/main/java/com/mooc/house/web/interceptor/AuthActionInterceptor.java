package com.mooc.house.web.interceptor;

import com.mooc.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
/**
 *该拦截器进行用户登录验证，进行鉴选流程
 */
@Component
public class AuthActionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user = UserContext.getUser();
		if (user == null) {
			String msg = URLEncoder.encode("请先登录","utf-8");
			//URLEncoder该操作是浏览器规范
			String target = URLEncoder.encode(request.getRequestURL().toString(),"utf-8");
			if ("GET".equalsIgnoreCase(request.getMethod())) {
				response.sendRedirect("/accounts/signin?errorMsg=" + msg + "&target="+target);
				return false;//修复bug,未登录要返回false
			}else {
				response.sendRedirect("/accounts/signin?errorMsg="+msg);
				return false;//修复bug,未登录要返回false
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
