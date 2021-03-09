package net.bytecms.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@MapperScan(basePackages ={"net.bytecms.*.mapper"})
@ComponentScan(basePackages = {
    "net.bytecms.core.*",
    "net.bytecms.security.*",
    "net.bytecms.web.*",
    "net.bytecms.system.*",
    "net.bytecms.service.*",
    "net.bytecms.freemark.*",
    "net.bytecms.addons.*"
})
public class ThinkItCMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkItCMSApplication.class, args);
    }

}
