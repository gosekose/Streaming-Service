package com.server.streaming.config;

import com.server.streaming.controller.filter.LoginSessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegisterConfig {

    @Bean
    public FilterRegistrationBean<LoginSessionFilter> loginSessionFilter() {
        FilterRegistrationBean<LoginSessionFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LoginSessionFilter());
        registrationBean.addUrlPatterns("/member-service/login/**");

        return registrationBean;
    }

}
