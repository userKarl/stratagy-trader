package com.zd;

import com.zd.config.loginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@EnableTransactionManagement
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCaching
public class ZdHftWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZdHftWebApplication.class, args);


    }
    @Bean
    public FilterRegistrationBean filerRegist(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new loginFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public Filter loginFilter(){
        return new loginFilter();
    }
}
