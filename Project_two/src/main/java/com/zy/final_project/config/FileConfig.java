package com.zy.final_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * 解决发帖图片回显失败问题
 * 上传图片后 后端要重启才能显示图片
 */
public class FileConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //获取文件真实路径
    String images_path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\";
    //打印项目路径   F:\Project_two
    System.out.println(System.getProperty("user.dir"));
    //F:\Project_two\src\main\resources\static\images\   图片绝对路径
    System.out.println(images_path);
    registry.addResourceHandler("/images/**").addResourceLocations("file:"+images_path);
    String profilephoto_path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\profilephoto\\";
    registry.addResourceHandler("/profilephoto/**").addResourceLocations("file:"+profilephoto_path);
    String dynamic_path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\dynamic\\";
    registry.addResourceHandler("/dynamic/**").addResourceLocations("file:"+dynamic_path);
    String schoolphoto_path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\schoolphoto\\";
    registry.addResourceHandler("/schoolphoto/**").addResourceLocations("file:"+schoolphoto_path);
    String classesphoto_path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\classesphoto\\";
    registry.addResourceHandler("/classesphoto/**").addResourceLocations("file:"+classesphoto_path);
  }
}
