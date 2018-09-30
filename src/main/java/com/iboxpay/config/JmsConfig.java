package com.iboxpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jms")
public class JmsConfig {

  /**
   * broker地址
   */
  private String brokerUrl;

  /**
   * 并发连接消费者数
   */
  private Integer concurrentConsumers;

  /**
   * 最大并发连接消费者数
   */
  private Integer maxConcurrentConsumers;

  /**
   * 目的地
   */
  private String destinationName;

  /**
   * 消息接收时长
   */
  private Integer receiveTimeout;

  public String getBrokerUrl() {
    return brokerUrl;
  }

  public Integer getConcurrentConsumers() {
    return concurrentConsumers;
  }

  public Integer getMaxConcurrentConsumers() {
    return maxConcurrentConsumers;
  }

  public String getDestinationName() {
    return destinationName;
  }

  public Integer getReceiveTimeout() {
    return receiveTimeout;
  }

  public void setBrokerUrl(String brokerUrl) {
    this.brokerUrl = brokerUrl;
  }

  public void setConcurrentConsumers(Integer concurrentConsumers) {
    this.concurrentConsumers = concurrentConsumers;
  }

  public void setMaxConcurrentConsumers(Integer maxConcurrentConsumers) {
    this.maxConcurrentConsumers = maxConcurrentConsumers;
  }

  public void setDestinationName(String destinationName) {
    this.destinationName = destinationName;
  }

  public void setReceiveTimeout(Integer receiveTimeout) {
    this.receiveTimeout = receiveTimeout;
  }

  @Override
  public String toString() {
    return "JmsConfig [brokerUrl=" + brokerUrl + ", concurrentConsumers=" + concurrentConsumers
        + ", maxConcurrentConsumers=" + maxConcurrentConsumers + ", destinationName=" + destinationName
        + ", receiveTimeout=" + receiveTimeout + "]";
  }

}
