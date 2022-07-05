package com.zy.final_project.controller;

import org.apache.commons.lang.StringUtils;
import com.zy.final_project.utils.SensitiveWordsUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/word")
public class testController {
  @GetMapping("/check/{content}")
  public String register(@PathVariable("content") String content){
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    String title = filter.replaceSensitiveWord(StringUtils.deleteWhitespace(content),1,"*");
    if(title.matches("[*]*")){
      return "不能发送";
    }
    return title;
  }
}
