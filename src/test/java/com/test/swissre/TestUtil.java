package com.test.swissre;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil {

  public static InputStream mockResponse(String cryptoCurrency, String currency) {
    String fileName = cryptoCurrency + "_" + currency + ".json";

    try (InputStream input = TestUtil.class.getClassLoader().getResourceAsStream(fileName)) {
      return input;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
