package com.iboxpay.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.iboxpay.entity.Student;

public interface StudentMapper {

  public List<Map<String, Object>> getAllStudents(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

  public Map<String, Object> getStudentById(@Param("student") Student student);

  public Map<String, Object> getStudentByName(Map<String, Object> map);
}
