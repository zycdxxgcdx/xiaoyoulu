package com.zy.final_project.entity.dynamic;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicLikeNums implements Serializable{
  private Integer id;
  private String dynamicId;
  private Integer likenum;
}
