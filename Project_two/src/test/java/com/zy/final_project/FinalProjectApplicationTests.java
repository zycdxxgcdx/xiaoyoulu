package com.zy.final_project;

import com.zy.final_project.dao.BookDao;
import com.zy.final_project.dao.PublicPostDao;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.zy.final_project.dao")
class FinalProjectApplicationTests {
    @Autowired
    private BookDao repository;

    @Autowired
    private PublicPostDao publicPostDao;

    @Test
    void findAllPagess(){
        System.out.println(publicPostDao.findAllPages());
    }

//    @Test
//    void findAllPagesss(){
//        System.out.println(publicPostRepository.findAll());
//    }

    @Test
    void contextLoads() {
    }

    @Test
    void findAll(){
        System.out.println(repository.findAll(1,6));
    }

    @Test
    void findAllPages(){
        System.out.println(repository.findAllPages());
    }

//    @Test
//    void save(){
//        Book book = new Book();
//        book.setName("Spring boot");
//        book.setAuthor("张三");
//        Book book1 = dao.save(book);
//        System.out.println(book1);
//    }
//
//    @Test
//    void findById(){
//        Book book = dao.findById(1).get(); // findById返回的是Option对象，get方法拿到book对象
//        System.out.println(book);
//    }
//
//    @Test
//    void udpate(){
//        Book book = new Book();
//        book.setId(20);
//        book.setName("测试测试");
//        Book boo1 = dao.save(book);
//        System.out.println(boo1);
//    }
//
//    @Test
//    void delete(){
//        dao.deleteById(20);
//    }

}
