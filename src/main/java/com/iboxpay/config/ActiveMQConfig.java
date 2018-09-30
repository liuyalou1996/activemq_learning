package com.iboxpay.config;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.iboxpay.jms.listener.MyListener;

@Configuration
@PropertySource("classpath:jms.properties")
public class ActiveMQConfig {

  @Autowired
  private JmsConfig jmsConfig;

  @Autowired
  private MyListener myListener;

  /**
   * ActiveMQ连接池
   * @return
   */
  @Bean(name = "pooledConnectionFactory", destroyMethod = "stop")
  public PooledConnectionFactory pooledConnectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL(jmsConfig.getBrokerUrl());
    return new PooledConnectionFactory(connectionFactory);
  }

  /**
   * 发送或接收目的地
   * @return
   */
  @Bean("activeMQQueue")
  public ActiveMQQueue activeMQQueue() {
    return new ActiveMQQueue(jmsConfig.getDestinationName());
  }

  /**
   * JmsTemplate消息发送模板类配置
   * @param connectionFactory
   * @param activeMQQueue
   * @return
   */
  @Bean
  public JmsTemplate jmsTemplate(@Qualifier("pooledConnectionFactory") PooledConnectionFactory connectionFactory,
      @Qualifier("activeMQQueue") ActiveMQQueue activeMQQueue) {
    JmsTemplate jmsTemplate = new JmsTemplate();
    // 指定连接工厂
    jmsTemplate.setConnectionFactory(connectionFactory);
    // 开启事务
    jmsTemplate.setSessionTransacted(true);
    // 指定确认模式，默认为自动确认
    jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    // 指定发送目的地
    jmsTemplate.setDefaultDestination(activeMQQueue);
    return jmsTemplate;
  }

  /**
   * jms事务管理器,创建session时需打开事务
   * @param connectionFactory
   * @return
   */
  @Bean(name = "jmsTransactionManager")
  public JmsTransactionManager jmsTransactionManager(
      @Qualifier("pooledConnectionFactory") PooledConnectionFactory connectionFactory) {
    return new JmsTransactionManager(connectionFactory);
  }

  /**
   * 消息监听器容器,用来接收来自指定目的地的消息
   * @param connectionFactory
   * @param jmsTransactionManager
   * @param threadPoolTaskExecutor
   * @param activeMQQueue
   * @return
   */
  @Bean
  public DefaultMessageListenerContainer defaultMessageListenerContainer(
      @Qualifier("pooledConnectionFactory") PooledConnectionFactory connectionFactory,
      @Qualifier("jmsTransactionManager") JmsTransactionManager jmsTransactionManager,
      @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor,
      @Qualifier("activeMQQueue") ActiveMQQueue activeMQQueue) {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    // 开启事务
    container.setSessionTransacted(true);
    container.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    container.setDestination(activeMQQueue);
    // 指定事务管理器
    container.setTransactionManager(jmsTransactionManager);

    // 指定消息监听器
    container.setMessageListener(myListener);
    // 并发消费者数量
    container.setConcurrentConsumers(jmsConfig.getConcurrentConsumers());
    // 最大并发消费者数量
    container.setMaxConcurrentConsumers(jmsConfig.getMaxConcurrentConsumers());
    // 指定任务执行器运行监听线程
    container.setTaskExecutor(threadPoolTaskExecutor);
    // 接收时长
    container.setReceiveTimeout(jmsConfig.getReceiveTimeout());
    return container;
  }

  /**
   * 线程池任务执行器配置
   * @return
   */
  @Bean("threadPoolTaskExecutor")
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(20);
    threadPoolTaskExecutor.setMaxPoolSize(100);
    threadPoolTaskExecutor.setKeepAliveSeconds(300);
    threadPoolTaskExecutor.setQueueCapacity(1000);
    threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    return threadPoolTaskExecutor;
  }

}
