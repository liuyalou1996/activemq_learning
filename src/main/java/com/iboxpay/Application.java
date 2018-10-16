package com.iboxpay;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import net.bull.javamelody.MonitoringFilter;

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

  /**
   * 注册javamelody应用监控过滤器
   */
  @Bean
  public FilterRegistrationBean<MonitoringFilter> monitoringFilter(ServletContext context) {
    FilterRegistrationBean<MonitoringFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new MonitoringFilter());
    filterRegistrationBean.addInitParameter("monitoring-path", "/javamelody");
    filterRegistrationBean.addInitParameter("authorized-users", "admin:admin");
    filterRegistrationBean.addUrlPatterns("/*");

    FilterRegistration filterRegistration = context.getFilterRegistration("javamelody");
    if (filterRegistration != null) {
      // 如果已注册MonitoringFilter,则不重复注册
      filterRegistrationBean.setEnabled(false);
      filterRegistration.setInitParameters(filterRegistrationBean.getInitParameters());
    }

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
