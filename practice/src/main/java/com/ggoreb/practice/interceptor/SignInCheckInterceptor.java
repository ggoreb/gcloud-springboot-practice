package com.ggoreb.practice.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ggoreb.practice.model.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignInCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.debug("preHandle");
    HttpSession session = request.getSession();
    
    System.out.println( request.getRequestURL() );
    System.out.println( request.getRequestURI() );
    System.out.println( request.getParameter("returnURL") );
    Enumeration<String> e = request.getParameterNames();
    while(e.hasMoreElements()) {
      String name = e.nextElement();
      System.out.println( request.getParameter(name) );
    }
    
    User user = (User) session.getAttribute("user");
    if (user == null) {
      response.sendRedirect("/signin");
      return false;
    }
    return true;

  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    log.debug("postHandle");
  }

}
