package com.mooc.house.biz.service;

import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  @Autowired
  private UserMapper userMapper;

  public List<User> getUsers() {
    return userMapper.selectUsers();
  }

}
