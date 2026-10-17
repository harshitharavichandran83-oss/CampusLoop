package com.campusloop;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;
@Component public class MobileTokenFilter extends OncePerRequestFilter {
 private final JdbcTemplate db;
 public MobileTokenFilter(JdbcTemplate db){this.db=db;}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
  String auth=request.getHeader("Authorization");
  if(auth!=null&&auth.startsWith("Bearer ")){
   String token=auth.substring(7);
   List<Long> ids=db.query("SELECT mt.user_id FROM mobile_tokens mt JOIN users u ON u.id=mt.user_id WHERE mt.token=? AND LOWER(u.email) REGEXP '^[a-z0-9._%+-]+@kongu[.]edu$' AND u.suspended=FALSE",(rs,n)->rs.getLong(1),token);
   if(!ids.isEmpty()){HttpSession session=request.getSession(true);session.setAttribute("uid",ids.get(0));session.setAttribute("mobileToken",token);}else{db.update("DELETE FROM mobile_tokens WHERE token=?",token);HttpSession session=request.getSession(false);if(session!=null){session.removeAttribute("uid");session.removeAttribute("mobileToken");}}
  }
  chain.doFilter(request,response);
 }
}
