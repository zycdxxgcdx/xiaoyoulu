package com.zy.final_project.dao;

import com.zy.final_project.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
@Mapper
public interface BookDao {
    public int findAllPages();
//    public List<Book> findAll(Integer page,Integer size);
//    public List<PublicPost> findAll(Integer page, Integer size);
    public List<String> findAll(Integer page, Integer size);
    public Book findById(Integer id);
    public void save(Book book);
    public void update(Book book);
    public void deleteById(Integer id);
}
