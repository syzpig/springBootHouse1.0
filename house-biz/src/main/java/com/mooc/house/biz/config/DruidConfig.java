package com.mooc.house.biz.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//为druid创建Configuration 配置druid数据源连接池

/**
 * @Configuration 讲解
 * 从定义来看， @Configuration 注解本质上还是 @Component，因此 <context:component-scan/> 或者 @ComponentScan 都能处理@Configuration 注解的类
 * 加载过程：Spring 容器在启动时，会加载默认的一些 PostPRocessor，其中就有 ConfigurationClassPostProcessor，
 * 这个后置处理程序专门处理带有 @Configuration 注解的类，这个程序会在 bean 定义加载完成后，
 * 在 bean 初始化前进行处理。主要处理的过程就是使用 cglib 动态代理增强类，而且是对其中带有 @Bean 注解的方法进行处理。
 */
@Configuration
public class DruidConfig {

    // TODO: 2018/5/9 作用
    /**
     *@ConfigurationProperties 这个注解，可以使属性文件中的值和类中的属性对应起来
     * 简单来说读取配置文件，与下@bean中属性一一绑定，配合使用，也可以单独写个实体与其一起使用。
     *
     * 这里是将外部的配置文件与DruidDataSource中的数据进行绑定 将配置文件数据绑定到他的属性值中
     */
    @ConfigurationProperties(prefix = "spring.druid")
    //pring 为了满足开发者在执行某方法之前或者在结束某个任务之前需要操作的一些业务，则提供了init-method和destroy-method  这两个属性，这两个属性需要加载在bean节点中。
    //在bean初始化之后要执行的初始化方法，以及在bean销毁时执行的方法。
    //springboot起动之后DruidDataSource也要跟随启动，所以指向init方法 容器销毁时关闭数据源指定close方法
    @Bean(initMethod = "init",destroyMethod = "close")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Lists.newArrayList(statFilter()));//在DruidDataSource中设置druid filter，就是把慢日志功能加入到连接池中
        return dataSource;
    }

    /**
     * Druid内置提供一个StatFilter，用于统计监控信息。
     * 能将缓慢的日志输出到日志文件中
     *配置能打印出慢日志的druid的filter 这是druid自己定义的filter 用于打印慢日志
     */
    @Bean
    public Filter statFilter(){
        StatFilter filter = new StatFilter();
        filter.setSlowSqlMillis(5000);//指定慢时间，多长时间才属于慢sql  才会识别什么时间属于慢sql
        filter.setLogSlowSql(true);//是否打印慢日志，也就是慢sql（就是sql执行时间）
        filter.setMergeSql(true);//是否合并慢日志
        return filter;
    }

  /* 用于Spring boot中注册Servlet
     当使用Spring-Boot时，嵌入式Servlet容器通过扫描注解的方式注册Servlet、Filter和Servlet规范的所有监听器（如HttpSessionListener监听器）。
    Spring boot 的主 Servlet 为 DispatcherServlet，其默认的url-pattern为“/”。也许我们在应用中还需要定义更多的Servlet，该如何使用SpringBoot来完成呢？
    在spring boot中添加自己的Servlet有两种方法，代码注册Servlet和注解自动注册（Filter和Listener也是如此）。
    一、代码注册通过ServletRegistrationBean、 FilterRegistrationBean 和 ServletListenerRegistrationBean 获得控制。
    也可以通过实现 ServletContextInitializer 接口直接注册。
    二、在 SpringBootApplication 上使用@ServletComponentScan 注解后，Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
   */
  //这里添加druid监控 分析mysql执行时间，分时执行时间，执行分布，等等  在web浏览器可以查看，这是druid优势之一
  @Bean
  public ServletRegistrationBean servletRegistrationBean(){
      //UrlMappings 此配置节的作用就是往Web程序中添加URL的映射，从而达到用户访问映射后的URL(如/Page/AAA)也能访问到源URL(如/Page/PageAAA.aspx)的效果。这也是URL映射本来的作用。
      return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");//默认固定路径
  }

}
