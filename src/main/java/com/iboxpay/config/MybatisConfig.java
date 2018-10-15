package com.iboxpay.config;

import java.io.IOException;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
@MapperScan(basePackages = "com.iboxpay.**.mapper", sqlSessionFactoryRef = "sqlSessionFactoryBean")
@EnableTransactionManagement
public class MybatisConfig {

  @Bean(name = "druidDataSource", initMethod = "init", destroyMethod = "close")
  @ConfigurationProperties(prefix = "spring.datasource.druid")
  public DruidDataSource druidDataSource() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean(name = "sqlSessionFactoryBean")
  public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("druidDataSource") DruidDataSource druidDataSource)
      throws IOException {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(druidDataSource);

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mybatis/**/*Mapper.xml"));
    sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
    return sqlSessionFactoryBean;
  }

  @Bean("transactionManager")
  public DataSourceTransactionManager dataSourceTransactionManager() {
    return new DataSourceTransactionManager();
  }

}
