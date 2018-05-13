package com.mooc.house.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 *处理设置拦截器顺序 对拦截器顺序管控与mapper对应的路径
 * WebMvcConfigurerAdapter 集成该类可以有选择的修改mvc行为，里面开放的接口可以进行相应实现，实心springmvc高度定制化
 */
@Configuration
public class WebMvcConf extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthActionInterceptor authActionInterceptor;
	
	@Autowired
	private AuthInterceptor authInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		//excludePathPatterns排除不拦截  addPathPatterns添加拦截
		//下面会先执行authInterceptor在执行authActionInterceptor。
		 registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
		    registry
		        .addInterceptor(authActionInterceptor).addPathPatterns("/house/toAdd")
		        .addPathPatterns("/accounts/profile").addPathPatterns("/accounts/profileSubmit")
		        .addPathPatterns("/house/bookmarked").addPathPatterns("/house/del")
		        .addPathPatterns("/house/ownlist").addPathPatterns("/house/add")
		        .addPathPatterns("/house/toAdd").addPathPatterns("/agency/agentMsg")
		        .addPathPatterns("/comment/leaveComment").addPathPatterns("/comment/leaveBlogComment");
		    super.addInterceptors(registry);//把拦截器注入到spring容器中
	}

	
}
