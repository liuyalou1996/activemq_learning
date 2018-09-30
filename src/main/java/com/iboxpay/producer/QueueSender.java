package com.iboxpay.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 点对点模式发送端
 */
public class QueueSender {

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    Connection conn = factory.createConnection();
    // 开启连接
    conn.start();
    Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Destination des = session.createQueue("sampleQueue");
    MessageProducer producer = session.createProducer(des);
    // 默认为persistent，当activemq关闭时，队列数据将会被保存。当为non_persistent时，activemq关闭时，队列数据将会被清空。
    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    producer.send(session.createTextMessage("Hello World!"));
    producer.close();
    session.close();
    conn.close();
  }
}
