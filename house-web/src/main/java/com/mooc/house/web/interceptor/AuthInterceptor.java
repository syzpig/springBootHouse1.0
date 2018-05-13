package com.mooc.house.web.interceptor;

import com.google.common.base.Joiner;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
/**
 * 创建用户拦截器
 *HandlerInterceptor spring提供拦截器接口
 */
@Component  //把spring拦截器实现成bean
public class AuthInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//把Parameter放入request用于获取信息，提示信息等等 这里统一做处理，就避免在controller中去实现
		Map<String, String[]> map = request.getParameterMap();
		map.forEach((k,v) -> {
			if (k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")) {
				request.setAttribute(k, Joiner.on(",").join(v));
			}
		});
	    String reqUri =	request.getRequestURI();
	    if (reqUri.startsWith("/static") || reqUri.startsWith("/error") ) {
			return true;
		}
	    HttpSession session = request.getSession(true);//为true是当session不存在时则自动创建
	    User user =  (User)session.getAttribute(CommonConstants.USER_ATTRIBUTE);
	    if (user != null) {//把user不等于空放入threadLocal中
			UserContext.setUser(user);
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
		UserContext.remove();
	}
	

}
