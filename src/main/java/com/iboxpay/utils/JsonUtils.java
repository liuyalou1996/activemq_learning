package com.iboxpay.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

  public static String toJsonString(Object obj) {
    return toJsonString(obj, false, false);
  }

  public static String toJsonStringWithNullValue(Object obj) {
    return toJsonString(obj, true, false);
  }

  public static String toPrettyJsonString(Object obj) {
    return toJsonString(obj, false, true);
  }

  public static String toPrettyJsonStringWithNullValue(Object obj) {
    return toJsonString(obj, true, true);
  }

  private static String toJsonString(Object obj, boolean isNullValueAllowed, boolean prettyFormat) {
    if (obj == null) {
      return null;
    }

    if (isNullValueAllowed) {
      if (prettyFormat) {
        return JSON.toJSONString(obj, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.PrettyFormat);
      }

      return JSON.toJSONString(obj, SerializerFeature.WRITE_MAP_NULL_FEATURES);
    } else {
      if (prettyFormat) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
      }

      return JSON.toJSONString(obj);
    }
  }

  /**
   * 将json字符串转换为javaBean
   * 
   * @param jsonStr
   *            json字符串
   * @param clazz
   *            运行时对象
   * @return
   */
  public static <T> T toJavaBean(String jsonStr, Class<T> clazz) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseObject(jsonStr, clazz);
  }

  /**
   * 字符串转换为list
   * 
   * @param jsonStr
   *            json字符串
   * @param clazz
   *            运行时对象
   * @return
   */
  public static <T> List<T> toList(String jsonStr, Class<T> clazz) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseArray(jsonStr, clazz);
  }

  /**
   * 将json字符串转换为map
   * 
   * @param jsonStr
   *            json字符串
   * @return
   */
  public static Map<String, Object> toMap(String jsonStr) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
  }

  /**
   * 将javaBean转换为map
   * 
   * @param obj
   *            转换的对象
   * @return
   */
  public static Map<String, Object> javaBeanToMap(Object obj) {
    if (obj == null) {
      return null;
    }

    return toMap(toJsonString(obj));
  }

  /**
   * 将map转换为javaBean
   * 
   * @param map
   *            map实例
   * @param clazz
   *            运行时对象
   * @return
   */
  public static <T> T mapToJavaBean(Map<String, ? extends Object> map, Class<T> clazz) {
    if (CollectionUtils.isEmpty(map)) {
      return null;
    }

    String jsonStr = JSON.toJSONString(map);
    return JSON.parseObject(jsonStr, clazz);
  }

}
