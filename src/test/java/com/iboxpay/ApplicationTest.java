package com.iboxpay;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

  @Autowired
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Autowired
  private DruidDataSource druidDataSource;

  @Test
  public void getThreadPoolTaskExecutor() {
    System.out.println(threadPoolTaskExecutor);
  }

  @Test
  public void getDruidDataSourceTest() throws SQLException {
    String jsonStr = JSON.toJSONString(DruidStatManagerFacade.getInstance().getDataSourceStatDataList(),
        SerializerFeature.PrettyFormat);
    System.err.println(jsonStr);
  }
}
