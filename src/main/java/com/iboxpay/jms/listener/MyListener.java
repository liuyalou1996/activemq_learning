package com.iboxpay.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyListener implements MessageListener {

  private static Logger logger = LoggerFactory.getLogger(MyListener.class);

  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        TextMessage textMessage = TextMessage.class.cast(message);
        System.out.println(textMessage.getText());
      } catch (JMSException e) {
        logger.error("接收消息失败:{}", e.getMessage(), e);
      }
    }
  }

}
