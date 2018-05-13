package com.mooc.house.web.controller;

import com.google.common.base.Objects;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;
/**
 *创建用户工具类，对用户进行验证，后台表单验证
 */

public class UserHelper {
	/**
	 *
	 */
	public static ResultMsg validate(User user) {
		if (StringUtils.isBlank(user.getEmail())) {
			return ResultMsg.errorMsg("Email 有误");
		}
		if (StringUtils.isBlank(user.getEmail())) {
			return ResultMsg.errorMsg("Email 有误");
		}
		if (StringUtils.isBlank(user.getConfirmPasswd()) || StringUtils.isBlank(user.getPasswd())
				|| !user.getPasswd().equals(user.getConfirmPasswd())) {
			return ResultMsg.errorMsg("Email 有误");
		}
		if (user.getPasswd().length() < 6) {
			return ResultMsg.errorMsg("密码大于6位");
		}
		return ResultMsg.successMsg("");//验证成功返回空串
	}
	
	public static ResultMsg validateResetPassword(String key, String password, String confirmPassword) {
	    if (StringUtils.isBlank(key) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
	      return ResultMsg.errorMsg("参数有误");
	    }
	    if (!Objects.equal(password, confirmPassword)) {
	      return ResultMsg.errorMsg("密码必须与确认密码一致");
	    }
	    return ResultMsg.successMsg("");
	  }

}
