package com.zy.final_project.utils;

import java.util.Random;

public class SaltUtils {
  /**
   * 生成salt的静态方法
   * 可以直接通过类名.方法 直接使用
   * @param n
   * @return
   */
  public static String getSalt(int n){
    char[] chars = "ABCDEFGHIJKLNMOPQRSTUVWXYZabcdefghijklnmopqrstuvwxyz!@#$%^&*()".toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++){
      char aChar = chars[new Random().nextInt(chars.length)];
      sb.append(aChar);
    }
    return sb.toString();
  }
}
