package net.bytecms.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
public class ByteCMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ByteCMSApplication.class, args);
    }

}
