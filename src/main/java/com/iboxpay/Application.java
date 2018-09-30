package com.iboxpay;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  @Bean
  public FilterRegistrationBean<OrderedCharacterEncodingFilter> orderedCharactEncodingFilter(ServletContext context) {
    FilterRegistrationBean<OrderedCharacterEncodingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new OrderedCharacterEncodingFilter());
    filterRegistrationBean.addInitParameter("encoding", "UTF-8");
    filterRegistrationBean.addInitParameter("forceEncoding", "true");
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
    filterRegistrationBean.setName("characterEncodingFilter");
    // 相同优先级则用户自定义的先注册
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return filterRegistrationBean;
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
