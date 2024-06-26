package com.example.myrestfulservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.myrestfulservice.bean.User;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();
	private static int usersCount = 3;

	static {
		users.add(new User(1, "SeungYeon", new Date(),"test1","111111-111111"));
		users.add(new User(2, "DDuri", new Date(),"test2","222222-222222"));
		users.add(new User(3, "Kwon", new Date(),"test3","333333-333333"));
	}

	public List<User> findAll() {
		return users;
	}

	public User save(User user) {
		if (null == user.getId()) {
			user.setId(++usersCount);
		}

		users.add(user);
		return user;
	}

	public User findOne(int id) {
		for (User user: users) {
			if (id == user.getId()) {
				return user;
			}
		}
		return null;
	}

	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();

		while(iterator.hasNext()) {
			User user = iterator.next();

			if(user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
}
