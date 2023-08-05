package com.test.swissre;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TestUtil {

  public static InputStream mockResponse(String cryptoCurrency, String currency) {
    String PATH_PREFIX = "responses/";
    String fileName = PATH_PREFIX + cryptoCurrency.toLowerCase() + "_" + currency.toLowerCase() + ".json";

    try (InputStream input = TestUtil.class.getClassLoader().getResourceAsStream(fileName)) {
      if (Objects.isNull(input)) {
        throw new RuntimeException("File lookup failed!");
      }
      return new ByteArrayInputStream(convertIsToByteArr(input));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }


  public static byte[] convertIsToByteArr(InputStream is) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[4];

    while ((nRead = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    buffer.flush();
    return buffer.toByteArray();
  }
}
