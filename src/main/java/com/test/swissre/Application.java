package com.test.swissre;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.service.OutputService;
import com.test.swissre.util.CryptoDetailsMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Application {

  private static final String APP_CONFIG = "application.properties";

  public static void main(String[] args) {
    OutputService outputService = new OutputService();
    CryptoDetailsMapper mapper = new CryptoDetailsMapper();

    try (InputStream input = Application.class.getClassLoader().getResourceAsStream(APP_CONFIG)) {
      Properties props = new Properties();
      props.load(input);

      Map<String, List<OutputRow>> currenciesWithCryptoDetails = mapper.map(args, props);
      currenciesWithCryptoDetails.forEach(outputService::print);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
