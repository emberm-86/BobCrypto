package com.test.swissre;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.domain.PortfolioItem;
import com.test.swissre.domain.Response;
import com.test.swissre.service.OutputService;
import com.test.swissre.service.RestClient;
import com.test.swissre.util.PortfolioParser;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {

  private static final String APP_CONFIG = "application.properties";

  public static void main(String[] args) {
    OutputService outputService = new OutputService();

    try (InputStream input = Main.class.getClassLoader().getResourceAsStream(APP_CONFIG)) {
      Properties props = new Properties();
      props.load(input);

      String endpointUrl = props.getProperty("endpoint.baseurl");
      String portfolioLocation = props.getProperty("input.location");
      final String defaultCurrency = props.getProperty("default.currency");

      List<PortfolioItem> portfolio = PortfolioParser.parse(portfolioLocation);
      List<String> currencies = Arrays.stream(args).collect(Collectors.toList());
      currencies.add(0, defaultCurrency);

      Map<String, List<OutputRow>> currenciesWithCryptoDetails = new LinkedHashMap<>();

      currencies.forEach( currency -> {
        portfolio.forEach(portfolioItem -> getPrice(endpointUrl, currency, portfolioItem));

        List<OutputRow> outputRows = portfolio.stream()
            .map(portfolioItem -> getPrice(endpointUrl, currency, portfolioItem)).collect(
                Collectors.toList());

        currenciesWithCryptoDetails.put(currency, outputRows);
      });

      currenciesWithCryptoDetails.forEach(outputService::print);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static OutputRow getPrice(String endpointUrl, String currency, PortfolioItem portfolioItem) {
    String cryptoCurrency = portfolioItem.getCryptoCurrency();
    Response response = RestClient.call(endpointUrl, cryptoCurrency, currency);
    BigDecimal price = response != null ? response.getPrice() : new BigDecimal("0.00");
    return new OutputRow(portfolioItem, price);
  }
}
