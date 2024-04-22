package com.example.myrestfulservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	@ManyToOne(fetch = FetchType.LAZY) 
	@JsonIgnore
	private User user;

}
