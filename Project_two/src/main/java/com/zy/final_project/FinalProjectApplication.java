package com.zy.final_project;

import com.zy.final_project.websocket.WebSocketService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.zy.final_project.dao")
@EnableCaching
public class FinalProjectApplication {

    public static void main(String[] args) {
//        SpringApplication.run(FinalProjectApplication.class, args);
        SpringApplication springApplication = new SpringApplication(FinalProjectApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        //解决WebSocket不能注入的问题
        WebSocketService.setApplicationContext(configurableApplicationContext);
    }

}
