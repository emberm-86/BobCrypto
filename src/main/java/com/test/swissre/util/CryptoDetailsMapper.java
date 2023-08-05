package com.test.swissre.util;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.domain.PortfolioItem;
import com.test.swissre.domain.Response;
import com.test.swissre.service.RestClient;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CryptoDetailsMapper {
  RestClient restClient;

  public Map<String, List<OutputRow>> map(String[] args, Properties props) throws IOException {
    String endpointUrl = props.getProperty("endpoint.baseurl");
    String portfolioLocation = props.getProperty("input.location");
    String defaultCurrency = props.getProperty("default.currency");

    List<PortfolioItem> portfolio = getPortfolio(portfolioLocation);
    List<String> currencies = getCurrencies(args, defaultCurrency);

    return currencies.stream().collect(Collectors.toMap(Function.identity(), currency ->
        createOutputRow(endpointUrl, portfolio, currency), (e1, e2) -> e1, LinkedHashMap::new));
  }

  private List<PortfolioItem> getPortfolio(String portfolioLocation) {
    return PortfolioParser.parse(portfolioLocation);
  }

  private List<String> getCurrencies(String[] args, String defaultCurrency) {
    List<String> currencies = Arrays.stream(args).collect(Collectors.toList());
    if (!currencies.contains(defaultCurrency)) {
      currencies.add(0, defaultCurrency);
    }
    return currencies;
  }

  private List<OutputRow> createOutputRow(String endpointUrl, List<PortfolioItem> portfolio,
      String currency) {
    return portfolio.stream().map(portfolioItem ->
        getPrice(endpointUrl, currency, portfolioItem)).collect(Collectors.toList());
  }

  private OutputRow getPrice(String endpointUrl, String currency, PortfolioItem portfolioItem) {
    String cryptoCurrency = portfolioItem.getCryptoCurrency();
    Response response = restClient.call(endpointUrl, cryptoCurrency, currency);
    BigDecimal price = Objects.nonNull(response) ? response.getPrice() : new BigDecimal("0.00");
    return new OutputRow(portfolioItem, price);
  }
}