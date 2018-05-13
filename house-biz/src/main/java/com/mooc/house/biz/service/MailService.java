package com.mooc.house.biz.service;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 *发送邮件。采用springboot提供启步依赖，进行异步发送
 */
@Service
public class MailService {

  @Autowired
  private JavaMailSender mailSender;//启步依赖发送邮箱bean

  @Value("${spring.mail.username}")
  private String from;


  @Value("${domain.name}")
  private String domainName;


  @Autowired
  private UserMapper userMapper;

/**
 *使用本地缓存  创建缓存
 */
  private final Cache<String, String>  registerCache =
      CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES)
          .removalListener(new RemovalListener<String, String>() {//注册后多长时间还未激活，则进行删除，防止下次注册唯一件冲突
            @Override
            public void onRemoval(RemovalNotification<String, String> notification) {
              String email = notification.getValue();
              User user = new User();
              user.setEmail(email);
              List<User> targetUser = userMapper.selectUsersByQuery(user);
              if (!targetUser.isEmpty() && Objects.equal(targetUser.get(0).getEnable(), 0)) {
                userMapper.delete(email);// 代码优化: 在删除前首先判断用户是否已经被激活，对于未激活的用户进行移除操作
              }

            }
          }).build();
  
  
  private final Cache<String, String> resetCache =  CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES).build();

  /**
   *发送有点
   */
  @Async
  public void sendMail(String title, String url, String email) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setSubject(title);
    message.setTo(email);
    message.setText(url);
    mailSender.send(message);
  }

  /**
   * 1.缓存key-email的关系
   * 2.借助spring mail 发送邮件
   * 3.借助异步框架进行异步操作
   * 
   * @param email
   */
  @Async//异步框架springboot已经引入进来了  注解式是spring将任务丢入线程池然后在启动来加入启动依赖去进行启动
  public void registerNotify(String email) {
    String randomKey = RandomStringUtils.randomAlphabetic(10);
    registerCache.put(randomKey, email);//把key和email进行存入缓存。用于判别激活成功与否 使用本地缓存
    String url = "http://" + domainName + "/accounts/verify?key=" + randomKey;
    sendMail("房产平台激活邮件", url, email);
  }
  
  /**
   * 发送重置密码邮件
   * 
   * @param email
   */
  @Async
  public void resetNotify(String email) {
    String randomKey = RandomStringUtils.randomAlphanumeric(10);
    resetCache.put(randomKey, email);
    String content = "http://" + domainName + "/accounts/reset?key=" + randomKey;
    sendMail("房产平台密码重置邮件", content, email);
  }

  public String getResetEmail(String key){
    return resetCache.getIfPresent(key);
  }
  
  public void invalidateRestKey(String key){
    resetCache.invalidate(key);
  }

  /**
   *用户激活后，验证是否合法，后改变用户状态
   */
  public boolean enable(String key) {
    String email = registerCache.getIfPresent(key);
    if (StringUtils.isBlank(email)) {
      return false;
    }
    User updateUser = new User();
    updateUser.setEmail(email);
    updateUser.setEnable(1);
    userMapper.update(updateUser);
    registerCache.invalidate(key);
    return true;
  }



}
