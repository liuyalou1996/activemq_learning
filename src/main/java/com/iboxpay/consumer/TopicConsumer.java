package com.iboxpay.consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 订阅/发布模式接收端
 */
public class TopicConsumer {

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    Connection conn = factory.createConnection();
    // 开启连接
    conn.start();
    Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Destination des = session.createTopic("myTopic");
    MessageConsumer consumer = session.createConsumer(des);
    consumer.setMessageListener(message -> {
      if (message instanceof TextMessage) {
        TextMessage tm = TextMessage.class.cast(message);
        try {
          System.out.println(tm.getText());
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });
    // 程序等待
    System.in.read();
    consumer.close();
    session.close();
    conn.close();
  }
}
