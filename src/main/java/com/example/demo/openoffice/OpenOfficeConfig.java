package com.example.demo.openoffice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author DJF
 * @version 0.1.0
 * @Description 配置类
 * @create 2021-05-07 15:02
 * @since 0.1.0
 **/
@Configuration
public class OpenOfficeConfig {


    @Bean(initMethod = "init",destroyMethod = "destroy")
    public OpenOfficeConverter openOfficeConverter(){
        return new OpenOfficeConverter();
    }

}
