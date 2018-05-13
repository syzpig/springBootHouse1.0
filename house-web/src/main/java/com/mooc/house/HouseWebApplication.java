package com.mooc.house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *该启动类必须放在所以文件上一层，因为springboot中包含@Component注解，其中包含@Component注解的类会自动被Spring扫描到生成Bean注册到spring容器中
 *在默认情况下只能扫描与控制器在同一个包下以及其子包下的@Component注解，以及能将指定注解的类自动注册为Bean的@Service@Controller和@ Repository
 */
@SpringBootApplication
@EnableAsync
public class HouseWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseWebApplication.class, args);
	}
}
