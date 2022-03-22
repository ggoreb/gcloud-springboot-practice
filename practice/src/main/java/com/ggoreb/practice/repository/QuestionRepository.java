package com.ggoreb.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ggoreb.practice.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  
//  @Query(nativeQuery = true, value = "select * from question")
//  List<Map<String, Object>> list();
}
