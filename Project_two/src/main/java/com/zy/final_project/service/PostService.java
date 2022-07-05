package com.zy.final_project.service;

import java.util.Map;

public interface PostService {
  Map<String, String> publicPost(Map<String, Object> map);
  Map<String, Object> getMySchool(Long userId);
}
