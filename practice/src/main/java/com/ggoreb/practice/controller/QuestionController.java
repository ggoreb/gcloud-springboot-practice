package com.ggoreb.practice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ggoreb.practice.model.Answer;
import com.ggoreb.practice.model.FileAtch;
import com.ggoreb.practice.model.Question;
import com.ggoreb.practice.model.User;
import com.ggoreb.practice.repository.AnswerRepository;
import com.ggoreb.practice.repository.FileAtchRepository;
import com.ggoreb.practice.repository.QuestionRepository;

@Controller
public class QuestionController {
  @Autowired
  QuestionRepository questionRepository;

  @Autowired
  AnswerRepository answerRepository;

  @Autowired
  FileAtchRepository fileAtchRepository;

  @GetMapping("/download")
  public ResponseEntity<Resource> download(
      @ModelAttribute Question question) throws Exception {
    
    List<FileAtch> fList = 
        fileAtchRepository.findByQuestion(question);
    
    String fileName = fList.get(0).getSaveFileName();
    File file = new File("c:/study/" + fileName);
    
    InputStreamResource resource = 
        new InputStreamResource(new FileInputStream(file));
    
//    HttpServletResponse res = null;
//    ServletOutputStream out = res.getOutputStream();
//    out.write("Hello");
    
    return ResponseEntity.ok()
     .header("content-disposition", "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
     .contentLength(file.length())
     .contentType(
         MediaType.parseMediaType("application/octet-stream"))
//           MediaType.parseMediaType("image/jpeg"))
//           MediaType.parseMediaType("application/pdf"))
     .body(resource);
  }

  @GetMapping("/question/create")
  public String questionCreate() {
    return "question_create";
  }

  @PostMapping("/question/create")
  public String questionCreatePost(@ModelAttribute Question question, HttpSession session,
      MultipartHttpServletRequest mRequest) {
    /* ?????? ?????? DB ?????? */
    User user = (User) session.getAttribute("user");
    question.setUser(user);
    question.setCreateDate(new Date());
    questionRepository.save(question);

    /* ???????????? ?????? ??? DB ?????? */
    Iterator<String> iter = mRequest.getFileNames();
    while (iter.hasNext()) {
      String inputName = iter.next();
      List<MultipartFile> mFiles = mRequest.getFiles(inputName);
      for (MultipartFile mFile : mFiles) {
        String oName = mFile.getOriginalFilename();
        if (oName == null || oName.equals("")) {
          break;
        }

        /* ???????????? ?????? - ????????? ?????? */
        File f = new File("c:/study/" + oName);
        String sName = "";

        if (f.isFile()) { // ????????? ????????????????
          String fileName = oName.substring(0, oName.lastIndexOf("."));
          String fileExt = oName.substring(oName.lastIndexOf("."));
          sName = fileName + System.currentTimeMillis() + fileExt;
        } else {
          sName = oName;
        }

        try {
          FileAtch fa = new FileAtch();
          fa.setOriginalFileName(oName);
          fa.setSaveFileName(sName);
          fa.setQuestion(question);
          fileAtchRepository.save(fa);

          mFile.transferTo(new File("c:/study/" + oName));
        } catch (IllegalStateException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return "redirect:/question/list";
  }

  @GetMapping("/etc")
  public String question(Model model, // View Template?????? ???????????? ???????????? ?????????
      @ModelAttribute User user, // ?????? ??????????????? ????????? ?????? ????????? (DTO, VO)
      @RequestParam String id, // ?????? ??????????????? 1?????? ??????????????? ?????? ?????????
      String name // @RequestParam??? ??????
  ) {
    return "etc";
  }

  @GetMapping("/question/delete")
  public String questionDelete(Long id) {
//    answerRepository.delete..

    questionRepository.deleteById(id);

    return "redirect:/question/list";
  }

  @GetMapping("/question/update") // /question/update?id=1 100
  public String questionUpdate(Long id, Model model) {
    Optional<Question> opt = questionRepository.findById(id);
    // ????????? ????????? ??????????????? (????????? id ?????? ??? ???????????? ?????? ??? ??????)
    if (opt.isPresent()) {
      model.addAttribute("question", opt.get());
    } else {
      // ????????? ?????? - ????????? ???????????????. ???????????? ????????? ????????????.
      // ????????? ?????? ??????????????????.
    }
    // question_create -> question_update ??????
    return "question_update";
  }

  @PostMapping("/question/update")
  public String questionUpdatePost(@ModelAttribute Question question, Long id) {

    Optional<Question> opt = questionRepository.findById(id);
    if (opt.isPresent()) {
      // ???????????? ????????? ????????? DB?????? ????????? ????????? ???????????????..
      Question dbQuestion = opt.get();
      dbQuestion.setSubject(question.getSubject());
      dbQuestion.setContent(question.getContent());
      questionRepository.save(dbQuestion);
    }

    return "redirect:/question/detail?id=" + id;
  }

  @PostMapping("/answer/create")
  public String answerCreate(String content, Long id, HttpSession session) {

    // ?????? ???????????? ????????? ????????????
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(new Date());

    // ????????? ?????? ????????? ?????? Object ????????? ??????
    // () ???????????? ???????????? ?????? 2??????
    // 1. ?????? ????????? ?????? ??????
    // 2. ?????? ???????????? ??????
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
