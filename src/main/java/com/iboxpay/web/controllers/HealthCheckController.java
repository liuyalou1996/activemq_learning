package com.iboxpay.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HealthCheckController {

  @RequestMapping("/getUserInfo.htm")
  public ModelAndView getUserInfo(String name, HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("name", "刘亚楼");
    mv.addObject("age", 20);
    mv.setViewName("userInfo");
    return mv;
  }

  @RequestMapping("/getUserList.htm")
  public ModelAndView getUserList(ModelAndView mv) {
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> one = new HashMap<>();
    Map<String, Object> two = new HashMap<>();
    one.put("name", "liuyalou");
    one.put("age", 20);
    two.put("name", "liuqian");
    two.put("age", 20);
    list.add(one);
    list.add(two);
    mv.addObject("userList", list);
    mv.setViewName("userList");
    return mv;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/getFilters.json")
  @ResponseBody
  public Map<String, FilterRegistration> getFilters(HttpServletRequest request) {
    ServletContext context = request.getServletContext();
    return (Map<String, FilterRegistration>) context.getFilterRegistrations();
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/getServlets.json")
  @ResponseBody
  public Map<String, ServletRegistration> getServlets(HttpServletRequest request) {
    ServletContext context = request.getServletContext();
    return (Map<String, ServletRegistration>) context.getServletRegistrations();
  }
}
