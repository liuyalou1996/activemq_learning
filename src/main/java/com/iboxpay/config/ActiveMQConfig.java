package com.iboxpay.config;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.iboxpay.jms.listener.MyListener;

@Configuration
public class ActiveMQConfig {

  @Autowired
  private MyListener myListener;

  /**
   * ActiveMQ连接池
   * @return
   */
  @Bean(name = "pooledConnectionFactory", destroyMethod = "stop")
  public PooledConnectionFactory pooledConnectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL("tcp://127.0.0.1:61616");
    connectionFactory.setSendTimeout(0);
    // 消息压缩，能提高发送效率，但会增加CPU的消耗性能
    connectionFactory.setUseCompression(true);
    // 异步发送，可能会导致消息丢失
    connectionFactory.setUseAsyncSend(true);
    // 信任所有消息包
    connectionFactory.setTrustAllPackages(true);
    // 预获取策略，默认从队列中预获取1000条消息
    ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
    prefetchPolicy.setQueuePrefetch(1000);
    connectionFactory.setPrefetchPolicy(prefetchPolicy);

    return new PooledConnectionFactory(connectionFactory);
  }

  /**
   * 发送或接收目的地
   * @return
   */
  @Bean("activeMQQueue")
  public ActiveMQQueue activeMQQueue() {
    return new ActiveMQQueue("spring_jms_test");
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
    // 设置消息的存活时间，默认为0，表示用不过期
    jmsTemplate.setTimeToLive(0);
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
    container.setConcurrentConsumers(5);
    // 最大并发消费者数量
    container.setMaxConcurrentConsumers(10);
    // 指定任务执行器运行监听器线程
    container.setTaskExecutor(threadPoolTaskExecutor);
    // 接收时长
    container.setReceiveTimeout(0);
    return container;
  }

  /**
   * 线程池任务执行器配置
   * @return
   */
  @Bean("threadPoolTaskExecutor")
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(5);
    threadPoolTaskExecutor.setMaxPoolSize(10);
    threadPoolTaskExecutor.setKeepAliveSeconds(300);
    threadPoolTaskExecutor.setQueueCapacity(1000);
    threadPoolTaskExecutor.setKeepAliveSeconds(300);
    threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    return threadPoolTaskExecutor;
  }

}
