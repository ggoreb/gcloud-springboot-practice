package com.ggoreb.practice.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.ggoreb.practice.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@WebListener
public class HttpSessionCheckingListener implements HttpSessionListener {
  private final Map<String, HttpSession> sessionMap = new HashMap<>();
  
  @Override
  public void sessionCreated(HttpSessionEvent event) {
    log.info("sessionCreated");
    
    HttpSession session = event.getSession();
    String sessionId = session.getId();
    sessionMap.put(sessionId, session);
    
    log.info("create session id : {}", sessionId);
    log.info("create session size : {}", sessionMap.size());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    log.info("sessionDestroyed");
    
    HttpSession session = event.getSession();
    String sessionId = session.getId();
    sessionMap.remove(sessionId);
    
    log.info("destroy session id : {}", sessionId);
    log.info("destroy session size : {}", sessionMap.size());
  }

  public boolean isLogin(String email) {
    return sessionMap.containsKey(email);
  }
  
  public void addUser(String email, HttpSession session) {
    sessionMap.put(email, session);
  }
}