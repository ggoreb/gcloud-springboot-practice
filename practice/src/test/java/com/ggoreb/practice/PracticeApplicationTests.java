package com.ggoreb.practice;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.UserRepository;

@SpringBootTest
class PracticeApplicationTests {
  @Autowired
  UserRepository userRepository;
  
	@Test @Transactional
	void contextLoads() {
	  List<User> users = userRepository.findAll();
	}

	@Test @Transactional
	void contextLoads2() {
	  List<User> users = userRepository.findAll();
	}
	
}
