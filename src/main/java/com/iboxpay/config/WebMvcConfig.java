package com.iboxpay.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.removeIf(converter -> {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        return true;
      }

      return false;
    });

    FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
    List<MediaType> mediaTypes = new ArrayList<>();
    mediaTypes.add(MediaType.TEXT_HTML);
    mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    mediaTypes.add(new MediaType("application", "*+json", Charset.forName("UTF-8")));
    fastJsonConverter.setSupportedMediaTypes(mediaTypes);
    converters.add(fastJsonConverter);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 错误页只能在默认的classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/制定
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    // 开启内容裁决注意配置一定要配置相关错误页面，否则内容裁决解析器会更改状态码，javamelody用户认证会失效
    // registry.enableContentNegotiation(new MappingJackson2JsonView());
    registry.jsp("/WEB-INF/pages/", ".jsp");
  }
}
