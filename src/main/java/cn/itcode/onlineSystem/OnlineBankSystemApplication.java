package cn.itcode.onlineSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.itcode.onlineSystem.dao")
@SpringBootApplication
public class OnlineBankSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineBankSystemApplication.class, args);
    }
}
