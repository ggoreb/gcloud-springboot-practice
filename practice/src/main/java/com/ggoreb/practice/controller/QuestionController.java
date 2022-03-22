package com.ggoreb.practice.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ggoreb.practice.model.Answer;
import com.ggoreb.practice.model.Question;
import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.AnswerRepository;
import com.ggoreb.practice.repository.QuestionRepository;

@Controller
public class QuestionController {
  @Autowired
  QuestionRepository questionRepository;

  @Autowired
  AnswerRepository answerRepository;

  @GetMapping("/question/update") // /question/update?id=1 100
  public String questionUpdate(Long id, Model model) {
    
    Optional<Question> opt = questionRepository.findById(id);
    // 조회한 질문이 존재한다면 (엉뚱한 id 조회 시 존재하지 않을 수 있음)
    if(opt.isPresent()) {
      model.addAttribute("question", opt.get());
    } else {
      // 메시지 출력 - 잘못된 요청입니다. 정상적인 접근이 아닙니다.
      // 나중에 작성 부탁드립니다.
    }
    // question_create -> question_update 복사
    return "question_update";
  }

  @PostMapping("/question/update")
  public String questionUpdatePost(
      @ModelAttribute Question question, Long id) {

    Optional<Question> opt = questionRepository.findById(id);
    if(opt.isPresent()) {
      // 사용자가 수정한 내용을 DB에서 조회한 질문에 대입하려고..
      Question dbQuestion = opt.get();
      dbQuestion.setSubject( question.getSubject() );
      dbQuestion.setContent( question.getContent() );
      questionRepository.save( dbQuestion );
    }

    return "redirect:/question/detail?id=" + id;
  }

  @PostMapping("/answer/create")
  public String answerCreate(String content, Long id, HttpSession session) {

    // 답변 테이블에 데이터 입력하기
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(new Date());

    // 세션에 넣은 값들은 모두 Object 형태로 변형
    // () 형변환을 사용하는 경우 2가지
    // 1. 기본 자료형 끼리 변환
    // 2. 상속 관계에서 변환
    User user = (User) session.getAttribute("user");
    answer.setUser(user);

    Question question = new Question();
    question.setId(id);

    answer.setQuestion(question);

    answerRepository.save(answer);

    return "redirect:/question/detail?id=" + id;
  }

  @GetMapping("/question/list")
//	@Transactional
  public String question(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
    Page<Question> p = questionRepository.findAll(PageRequest.of(page - 1, 10, Sort.Direction.DESC, "createDate"));

    List<Question> list = p.getContent();

    model.addAttribute("list", list);
    return "question_list";
  }

  @GetMapping("/question/create")
  public String questionCreate() {
    return "question_create";
  }

  @PostMapping("/question/create")
  public String questionCreatePost(@ModelAttribute Question question, HttpSession session) {
    User user = (User) session.getAttribute("user");

    question.setUser(user);
    question.setCreateDate(new Date());
    questionRepository.save(question);
    return "redirect:/question/list";
  }

  @GetMapping("/question/detail")
  public String questionDetail(Model model, Long id) {
    Optional<Question> opt = questionRepository.findById(id);
    if (opt.isPresent()) {
      model.addAttribute("question", opt.get());
    }

    List<Answer> opt2 = answerRepository.findByQuestion(opt.get());
    model.addAttribute("answerList", opt2);

    return "question_detail";
  }
}
