package com.example.myrestfulservice.controller;

//HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.myrestfulservice.bean.User;
import com.example.myrestfulservice.exception.UserNotFoundException;
import com.example.myrestfulservice.service.UserDaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러입니다.")
public class UserController {

	private UserDaoService userDaoService;
	private MessageSource messageSource;

	public UserController(UserDaoService userDaoService, MessageSource messageSource) {
		this.userDaoService = userDaoService;
		this.messageSource = messageSource;
	}

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userDaoService.findAll();
	}

	@Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 이용해서 사용자 상세 정보 조회를 합니다.")
	@GetMapping("/users/{id}")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "USER NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR") })
	public EntityModel<User> retrieveUser(
			@Parameter(description = "사용자 ID", required = true, example = "1")
			@PathVariable(value = "id") int id) {
		User user = userDaoService.findOne(id);

		if (null == user) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

		// HATEOAS
		EntityModel entityModel = EntityModel.of(user);

		WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(linTo.withRel("all-users")); // all-users → http://localhost:8088/users
		return entityModel;
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userDaoService.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable(value = "id") int id) {
		User deletedUser = userDaoService.deleteById(id);

		if (null == deletedUser) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}

	}

	@GetMapping("/hello-world-internationalized")
	public String helloworldInternalized(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {

		return messageSource.getMessage("greeting.message", null, locale);
	}
}
