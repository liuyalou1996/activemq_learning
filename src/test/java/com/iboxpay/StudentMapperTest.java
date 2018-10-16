package com.iboxpay;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.iboxpay.entity.Student;
import com.iboxpay.mybatis.mapper.StudentMapper;
import com.iboxpay.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StudentMapperTest {

  @Autowired
  private StudentMapper studentMapper;

  @Test
  public void getAllStudentsTest() {
    List<Map<String, Object>> list = studentMapper.getAllStudents(1, 6);
    System.out.println(JsonUtils.toPrettyJsonStringWithNullValue(list));
  }

  @Test
  public void getStudentById() {
    Student student = new Student();
    student.setId(1);
    Map<String, Object> map = studentMapper.getStudentById(student);
    System.out.println(map);
  }

}
