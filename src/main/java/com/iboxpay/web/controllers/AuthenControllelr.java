package com.iboxpay.web.controllers;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenControllelr {

  @RequestMapping("/errTest")
  public void handleErrorRequest(HttpServletResponse response) throws IOException {
    response.setHeader("authentication", "basic");
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权!");
  }

  @RequestMapping("/authorize.action")
  public ResponseEntity<String> handleAuthentication(
      @RequestHeader(value = "Authorization", required = false) String authInfo, HttpServletResponse resp) {

    if (StringUtils.isNotBlank(authInfo) && authInfo.startsWith("Basic ")) {
      byte[] bytes = Base64.decodeBase64(authInfo.substring("Basic ".length()));
      String decodedStr = new String(bytes, 0, bytes.length, Charset.forName("UTF-8"));
      String[] indentityInfo = decodedStr.split(":");
      if ("admin".equals(indentityInfo[0]) && "admin".equals(indentityInfo[1])) {
        return new ResponseEntity<String>("授权成功，可进行访问", null, HttpStatus.OK);
      }
    }

    HttpHeaders respHeader = new HttpHeaders();
    respHeader.set("WWW-Authenticate", "Basic realm=\"javamelody\"");
    return new ResponseEntity<String>("用户未授权，无法访问", respHeader, HttpStatus.UNAUTHORIZED);
  }
}
