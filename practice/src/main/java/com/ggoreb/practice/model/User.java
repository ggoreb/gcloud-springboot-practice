package com.ggoreb.practice.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String email;
	@JsonIgnore 
	String pwd;
	String name;
	
	// ManyToOne 으로 연결되어 있으면 mappedBy 사용
	// 양방향
	// JUnit에서는 EAGER 옵션 또는 @Transactional 사용
	// Thymeleaf에서는 EAGER 옵션 사용하지 않아도 동작
	@OneToMany(mappedBy = "user")
	@JsonIgnore
//	List<Question> questions; // 가능하지만 권장X, 나중에 오류 발생 가능성
	List<Question> questions = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	List<Answer> answers = new ArrayList<>();
}










