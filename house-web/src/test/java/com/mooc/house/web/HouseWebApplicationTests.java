package com.mooc.house.web;

import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.model.Agency;
import com.mooc.house.common.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HouseWebApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	AgencyService agencyService;

	@Test
	public void contextLoads() {
		User user = new User();
		user.setName("123");
		User user1 = new User();
		user1.setName("asd");
		User user2 = new User();
		user2.setName("gfggg");
		List<User> userList = new ArrayList<>();
		userList.add(user);
		userList.add(user1);
		userList.add(user2);
		Agency agency = new Agency();
		agency.setName("aaaa");
		agency.setAddress("aaaa");
		agency.setEmail("aaaa");
		agency.setPhone("122313");
		Agency agency1 = new Agency();
		agency1.setName("bbbb");
		agency1.setAddress("bbbb");
		agency1.setEmail("bbbb");
		agency1.setPhone("122313");
		Agency agency2 = new Agency();
		agency2.setName("cccc");
		agency2.setAddress("cccc");
		agency2.setEmail("cccc");
		agency.setPhone("122313");
		List<Agency> agencyList = new ArrayList<>();
		agencyList.add(agency);
		agencyList.add(agency1);
		agencyList.add(agency2);

		for (Agency ag: agencyList) {
			agencyService.insertBatch(ag);

			for (User u: userList) {
				u.setAgencyId(ag.getId());
				userService.insertBatch(u);
			}
		}

	}

}
