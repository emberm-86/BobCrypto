package com.test.swissre.service;

import static com.test.swissre.util.ParameterStringBuilder.getParamsString;

import com.test.swissre.domain.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

  private RestClient() {
  }

  public static Response call(String baseUrl, String cryptoCurrency, String currency) {
    try {
      HttpURLConnection connection = openConnection(baseUrl, cryptoCurrency, currency);

      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("Content-Type", "application/json");

      if (connection.getResponseCode() != 200) {
        throw new RuntimeException("Calling endpoint failed:" + connection.getResponseCode());
      }

      InputStreamReader in = new InputStreamReader(connection.getInputStream());
      BufferedReader br = new BufferedReader(in);
      String result;
      Response response = null;

      if ((result = br.readLine()) != null) {
        result = result.substring(1, result.length() - 1);
        String[] responseArr = result.split(":");
        response = new Response(responseArr[0], new BigDecimal(responseArr[1]));
      }

      connection.disconnect();
      br.close();
      in.close();

      return response;

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static HttpURLConnection openConnection(String baseUrl, String cryptoCurrency,
      String currency) throws IOException {

    Map<String, String> parameters = new HashMap<>();
    parameters.put("fsym", cryptoCurrency);
    parameters.put("tsyms", currency);

    URL targetURL = new URL(baseUrl + "?" + getParamsString(parameters));
    return (HttpURLConnection) targetURL.openConnection();
  }
}
