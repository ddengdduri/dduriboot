package com.example.myrestfulservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.example.myrestfulservice.bean.Post;
import com.example.myrestfulservice.bean.User;
import com.example.myrestfulservice.repository.PostRepository;
import com.example.myrestfulservice.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {

	private UserRepository userRepository;

	private PostRepository postRepository;

	public UserJPAController(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	// /jpa/users
	@GetMapping("/users")
	public ResponseEntity<Map<String, Object>> retrieveAllUsers() {
		List<User> users = userRepository.findAll();
		int count = users.size();

		Map<String, Object> response = new HashMap<>();
		response.put("count", count);
		response.put("users", users);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity retrieveUserById(@PathVariable(value = "id") int id) {
		Optional<User> user = userRepository.findById(id); // user값이 존재 할 수도 있고 없을 수도 있음 Optional - NullPointerException  방지

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("id -" + id);
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

	@GetMapping("/users/{id}/posts")
	public List<Post> retrieveAllPostByUser(@PathVariable(value = "id") int id) {
		Optional<User> user = userRepository.findById(id); // user값이 존재 할 수도 있고 없을 수도 있음 Optional - NullPointerException  방지

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("id -" + id);
		}

		return user.get().getPosts();
	}

	@PostMapping("/users/{id}/posts")
	public ResponseEntity<Post> createPost(@PathVariable(value = "id") int id, @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("id -" + id);
		}

		post.setUser(user.get());

		Post savedPost = postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
}

/*
 * 리소스의 변경을 가져오는 DELETE, CREATE같은 UPDATE 작업을 사용하기 위해서는 SecurityFilterChain을
 * Bean으로 등록해야 오류없이 처리 가능
 */
