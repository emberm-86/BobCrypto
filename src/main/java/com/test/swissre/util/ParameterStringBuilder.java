package com.test.swissre.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder {

  public static String getParamsString(Map<String, String> params)
      throws UnsupportedEncodingException {

    StringBuilder result = new StringBuilder();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      append(result, entry.getKey(), "=");
      append(result, entry.getValue(), "&");
    }

    String resultString = result.toString();

    if (!resultString.isEmpty()) {
      resultString = resultString.substring(0, resultString.length() - 1);
    }
    return resultString;
  }

  private static void append(StringBuilder result, String entry, String str)
      throws UnsupportedEncodingException {
    result.append(URLEncoder.encode(entry, "UTF-8"));
    result.append(str);
  }
}
