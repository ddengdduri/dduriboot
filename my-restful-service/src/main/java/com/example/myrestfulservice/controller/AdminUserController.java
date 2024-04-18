package com.example.myrestfulservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myrestfulservice.bean.AdminUser;
import com.example.myrestfulservice.bean.AdminUserV2;
import com.example.myrestfulservice.bean.User;
import com.example.myrestfulservice.exception.UserNotFoundException;
import com.example.myrestfulservice.service.UserDaoService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

	private UserDaoService userDaoService;

	public AdminUserController(UserDaoService userDaoService) {
		this.userDaoService = userDaoService;
	}

	@GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")
	public MappingJacksonValue retrieveUser(@PathVariable(value = "id") int id) {
		User user = userDaoService.findOne(id);
		AdminUser adminUser = new AdminUser();
		if (null == user) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		} else {
			BeanUtils.copyProperties(user, adminUser);
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

		MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
		mapping.setFilters(filters);

		return mapping;
	}

	@GetMapping(value = "/users", produces = "application/vnd.company.appv2+json")
	public MappingJacksonValue retrieveAllUser() {
		List<User> users = userDaoService.findAll();
		List<AdminUser> adminUsers = new ArrayList<>();
		AdminUser adminUser = null;

		for (User user : users) {
			adminUser = new AdminUser();
			BeanUtils.copyProperties(user, adminUser);

			adminUsers.add(adminUser);
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

		MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
		mapping.setFilters(filters);

		return mapping;
	}

	@GetMapping("/users/{id}")
	public MappingJacksonValue retrieveUser4AdminV2(@PathVariable(value = "id") int id) {
		User user = userDaoService.findOne(id);
		AdminUserV2 adminUser = new AdminUserV2();
		if (null == user) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		} else {
			BeanUtils.copyProperties(user, adminUser);
			adminUser.setGrade("VIP");
		}

		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate",
				"grade");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

		MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
		mapping.setFilters(filters);

		return mapping;
	}
}
