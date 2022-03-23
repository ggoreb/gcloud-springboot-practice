package com.ggoreb.practice;

import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class DBTest {
  public static void main(String[] args) throws SQLException {
    // G U I - Swing / AWT
    // 인터페이스를 사용하는 이유와 요령 익히기 가능
    JFrame j = new JFrame("내 윈도우");
    JButton btn = new JButton("버튼입니다.");
    j.add(btn);
    j.setSize(300, 200);
    j.setVisible(true);
    
//    MultipartHttpServletRequest mRequest = null;
//    Enumeration<String> headers = mRequest.getHeaders("dd");
//    while(headers.hasMoreElements()) {
//      headers.nextElement();
//    }
//    
//    // 자바 컬렉션 프레임워크 (List Set)  /  Map
//    List list = null;
//    Iterator it = list.iterator();
//    
//    Iterator<String> iter = mRequest.getFileNames();
//    while(iter.hasNext()) {
//      String name = iter.next();
//    }
//    for(;;) {
//      boolean isNext = iter.hasNext();
//      if(!isNext) break; 
//      String name = iter.next();
//    }
//
//    // JDBC 
//    Connection con;
//    PreparedStatement stmt;
//    ResultSet rs = null;
//    while(rs.next()) {
//      String id = rs.getString("id");
//    }
//    
//    // Swing / AWT
//    
//    
//    HttpServletRequest request = null;
//    Enumeration<String> enu = request.getAttributeNames();
//    while(enu.hasMoreElements()) {
//      String item = enu.nextElement();
//    }
  }
}







