package com.zy.final_project.filter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.final_project.utils.JWTUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class JwtTokenInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    HashMap<String, Object> map = new HashMap<>();
    //获取请求头中的令牌
    String token = request.getHeader("token");
    System.out.println(token);
    try {
      JWTUtils.verify(token); //验证令牌
      return true;
    }catch (SignatureVerificationException e){
      e.printStackTrace();
      map.put("msg","无效签名!");
    }catch (TokenExpiredException e){
      e.printStackTrace();
      map.put("msg","token过期");
    }catch (AlgorithmMismatchException e){
      e.printStackTrace();
      map.put("msg","token算法不一致");
    }catch (Exception e){
      e.printStackTrace();
      map.put("msg","token无效");
    }
    map.put("state",false);
    //将map转为json jackson
    String json = new ObjectMapper().writeValueAsString(map);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().println(json);
    return false;
  }
}
