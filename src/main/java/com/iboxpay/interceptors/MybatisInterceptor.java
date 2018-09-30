package com.iboxpay.interceptors;

import java.util.Arrays;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
    RowBounds.class, ResultHandler.class }))
public class MybatisInterceptor implements Interceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(MybatisInterceptor.class);

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    String methodName = invocation.getMethod().getName();
    Object[] args = invocation.getArgs();
    LOGGER.info("************开始拦截{}方法,参数为:{}************", methodName, Arrays.toString(args));
    Object obj = invocation.proceed();
    LOGGER.info("************方法返回值为:{}***********", obj);
    return obj;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }

}
