package com.iboxpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
public class MybatisConfig {

  @Bean(initMethod = "init", destroyMethod = "close")
  @ConfigurationProperties(prefix = "spring.datasource.druid")
  public DruidDataSource druidDataSource() {
    return DruidDataSourceBuilder.create().build();
  }

}
