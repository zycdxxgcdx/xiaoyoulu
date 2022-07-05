package com.zy.final_project.entity;

import lombok.Data;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//@Entity   JPA注解
@Data
public class Book {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //设置自增
    private Integer id;
    private String name;
    private String author;
    private String publish;
    private Integer pages ;
    private float price;
}
