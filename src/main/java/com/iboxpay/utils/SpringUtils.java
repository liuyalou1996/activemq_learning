package com.iboxpay.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

  private static ApplicationContext context = null;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  public static Object getBean(String beanName) {
    return context.getBean(beanName);
  }

  public static <T> T getBean(String beanName, Class<T> requiredType) {
    return context.getBean(beanName, requiredType);
  }

  public static void main(String[] args) {
    System.out.println(JsonUtils.toJsonString("sdf"));
  }

}
