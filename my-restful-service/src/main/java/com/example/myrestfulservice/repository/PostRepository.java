package com.example.myrestfulservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myrestfulservice.bean.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
