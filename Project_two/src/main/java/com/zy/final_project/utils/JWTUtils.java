package com.zy.final_project.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
  private static final String TOKEN = "!1@2#3$4%5^6&7&8*9()";
  /**
   * 生成token header.playload,sing
   *
   * @param map //传入payload
   * @return 返回token
   */
  public static String getToken(Map<String, String> map) {
    JWTCreator.Builder builder = JWT.create();
    //playload
    map.forEach((k, v) -> {
      builder.withClaim(k, v);
    });  //lamda表达式
    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.DATE, 3); //过期时间 默认3天过期
//    instance.add(Calendar.MINUTE, 1); //过期时间 默认3天过期
    builder.withExpiresAt(instance.getTime());
    return builder.sign(Algorithm.HMAC256(TOKEN)).toString();
  }

  /**
   * 验证token
   * 获取token中payload
   * @param token
   * @return
   */
  public static DecodedJWT verify(String token) {
    return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
  }
}
