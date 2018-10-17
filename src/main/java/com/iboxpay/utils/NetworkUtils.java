package com.iboxpay.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetworkUtils {

  private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

  private static String ipPattern = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

  private static String UNKOWN = "unknown";

  private static Pattern pattern = Pattern.compile("^(?:" + ipPattern + "\\.){3}" + ipPattern + "$");

  public static String getCallerIP(final HttpServletRequest req) {
    boolean found = false;
    String callerIP = req.getHeader("x-forwarded-for");;
    if (StringUtils.isNotBlank(callerIP)) {
      StringTokenizer tokenizer = new StringTokenizer(callerIP, ",");
      while (tokenizer.hasMoreTokens()) {
        callerIP = tokenizer.nextToken().trim();
        if (NetworkUtils.isIpv4Valid(callerIP) && !NetworkUtils.isIpv4Private(callerIP)) {
          found = true;
          break;
        }
      }
    }

    if (!found) {
      if (StringUtils.isNotBlank(req.getHeader("WL-Proxy-Client-IP"))
          && !UNKOWN.equalsIgnoreCase(req.getHeader("WL-Proxy-Client-IP"))) {
        callerIP = req.getHeader("WL-Proxy-Client-IP");
      } else if (StringUtils.isNotBlank(req.getHeader("HTTP_CLIENT_IP"))
          && !UNKOWN.equalsIgnoreCase(req.getHeader("HTTP_CLIENT_IP"))) {
        callerIP = req.getHeader("HTTP_CLIENT_IP");
      } else if (StringUtils.isNotBlank(req.getHeader("HTTP_X_FORWARDED_FOR"))
          && !UNKOWN.equalsIgnoreCase(req.getHeader("HTTP_X_FORWARDED_FOR"))) {
        callerIP = req.getHeader("HTTP_X_FORWARDED_FOR");
      } else {
        callerIP = req.getRemoteAddr();
      }
    }

    return callerIP;
  }

  public static String longToIpV4(final long longIp) {
    int octet3 = (int) ((longIp >> 24) % 256);
    int octet2 = (int) ((longIp >> 16) % 256);
    int octet1 = (int) ((longIp >> 8) % 256);
    int octet0 = (int) (longIp % 256);
    return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
  }

  public static long ipV4ToLong(final String ip) {
    String[] octets = ip.split("\\.");
    return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) + (Integer.parseInt(octets[2]) << 8)
        + Integer.parseInt(octets[3]);
  }

  /**
   * 判断ip地址是否为内网地址
   * @param ip
   * @return
   */
  public static boolean isIpv4Private(final String ip) {
    long longIp = NetworkUtils.ipV4ToLong(ip);
    return longIp >= NetworkUtils.ipV4ToLong("10.0.0.0") && longIp <= NetworkUtils.ipV4ToLong("10.255.255.255")
        || longIp >= NetworkUtils.ipV4ToLong("172.16.0.0") && longIp <= NetworkUtils.ipV4ToLong("172.31.255.255")
        || longIp >= NetworkUtils.ipV4ToLong("192.168.0.0") && longIp <= NetworkUtils.ipV4ToLong("192.168.255.255");
  }

  /**
   * 判断ip地址是否有效
   * @param ip
   * @return
   */
  public static boolean isIpv4Valid(final String ip) {
    return NetworkUtils.pattern.matcher(ip).matches();
  }

  /**
   * 获取本机IP
   */
  public static String getLocalIp() {
    String localIp = null;
    try {
      InetAddress address = InetAddress.getLocalHost();
      localIp = address.getHostAddress();
    } catch (UnknownHostException e) {
      logger.error("获取本机ip地址异常 :{}", e.getMessage(), e);
    }
    return localIp;
  }

  public static void main(String[] args) {
  }
}
