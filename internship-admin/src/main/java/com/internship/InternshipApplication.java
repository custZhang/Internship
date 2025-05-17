package com.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author internship
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class InternshipApplication
{
    public static void main(String[] args)
    {
        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication.run(InternshipApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  实习管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
