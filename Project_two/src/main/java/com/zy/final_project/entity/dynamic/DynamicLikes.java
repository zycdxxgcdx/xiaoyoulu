package com.zy.final_project.entity.dynamic;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicLikes implements Serializable {
  private Integer id;
  private String userId;
  private String dynamicId;
  private Integer status;
//  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//  @JsonSerialize(using = LocalDateTimeSerializer.class)
//  private LocalDateTime dateTime;
}
