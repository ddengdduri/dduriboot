package com.example.myrestfulservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.myrestfulservice.bean.User;
import com.example.myrestfulservice.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {

	private UserRepository userRepository;

	public UserJPAController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// /jpa/users
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}

	
	@GetMapping("/users/{id}")
	public ResponseEntity retrieveUserById(@PathVariable(value = "id") int id) {
		Optional<User> user = userRepository.findById(id); // user값이 존재 할 수도 있고 없을 수도 있음 Optional - NullPointerException 방지

		if(!user.isPresent()) {
			throw new UsernameNotFoundException("id -"+id);
		}

		EntityModel entityModel = EntityModel.of(user.get()); // optional에서 실제 user값을 뺴와야 해서 .get() 써야한다.

		WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(linTo.withRel("all-users")); // all-users → http://localhost:8088/users

		return ResponseEntity.ok(entityModel);
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable(value = "id") int id) {
		userRepository.deleteById(id);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
}
