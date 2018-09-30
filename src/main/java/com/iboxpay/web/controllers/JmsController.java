package com.iboxpay.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JmsController {

  @Autowired
  private JmsTemplate jmsTemplate;

  @RequestMapping("sendMessage.action")
  @ResponseBody
  public Map<String, Object> sendMessage(String textMessage) {
    Map<String, Object> resp = new HashMap<>();
    if (StringUtils.isBlank(textMessage)) {
      resp.put("statusCode", 0);
      resp.put("msg", "消息不能为空!");
      return resp;
    }

    // 发送消息
    jmsTemplate.send(session -> {
      return session.createTextMessage(textMessage);
    });
    resp.put("statusCode", 1);
    resp.put("msg", "消息发送成功!");

    return resp;
  }

}
