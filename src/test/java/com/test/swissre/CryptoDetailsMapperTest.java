package com.test.swissre;

import static com.test.swissre.TestUtil.mockResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.service.RestClient;
import com.test.swissre.util.CryptoDetailsMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CryptoDetailsMapperTest {

  private static final String BASE_URL = "https://min-api.cryptocompare.com/data/price";
  private static final String DEFAULT_CURRENCY = "EUR";
  private static final String INPUT_LOCATION = "bobs_crypto.txt";

  @Test
  public void testMappingNoInput() throws IOException {
    Properties cryptoProps = Mockito.mock(Properties.class);
    RestClient restClient = Mockito.mock(RestClient.class);

    prepareDefaultTestCase(cryptoProps, restClient);

    CryptoDetailsMapper cryptoDetailsMapper = new CryptoDetailsMapper(restClient);

    Map<String, List<OutputRow>> map = cryptoDetailsMapper.map(new String[]{}, cryptoProps);

    assertEquals(1, map.size());
    List<OutputRow> defaultCurrencyRates = map.get(DEFAULT_CURRENCY);

    assertEquals(3, defaultCurrencyRates.size());
    assertEquals(new BigDecimal("26466.25"), defaultCurrencyRates.get(0).getPrice());
    assertEquals(new BigDecimal("1675.12"), defaultCurrencyRates.get(1).getPrice());
    assertEquals(new BigDecimal("0.5663"), defaultCurrencyRates.get(2).getPrice());
  }

  @Test
  public void testMappingOneElementInput() throws IOException {
    Properties cryptoProps = Mockito.mock(Properties.class);
    RestClient restClient = Mockito.mock(RestClient.class);

    prepareDefaultTestCase(cryptoProps, restClient);

    mockRestClientByCryptCurr(restClient, "BTC","JPY");
    mockRestClientByCryptCurr(restClient, "ETH","JPY");
    mockRestClientByCryptCurr(restClient, "XRP","JPY");

    CryptoDetailsMapper cryptoDetailsMapper = new CryptoDetailsMapper(restClient);

    Map<String, List<OutputRow>> map = cryptoDetailsMapper.map(new String[]{"JPY"}, cryptoProps);

    assertEquals(2, map.size());
    List<OutputRow> eurCurrencyRates = map.get(DEFAULT_CURRENCY);

    assertEquals(3, eurCurrencyRates.size());
    assertEquals(new BigDecimal("26466.25"), eurCurrencyRates.get(0).getPrice());
    assertEquals(new BigDecimal("1675.12"), eurCurrencyRates.get(1).getPrice());
    assertEquals(new BigDecimal("0.5663"), eurCurrencyRates.get(2).getPrice());

    List<OutputRow> jpyCurrencyRates = map.get("JPY");

    assertEquals(3, jpyCurrencyRates.size());
    assertEquals(new BigDecimal("4131937.89"), jpyCurrencyRates.get(0).getPrice());
    assertEquals(new BigDecimal("261054"), jpyCurrencyRates.get(1).getPrice());
    assertEquals(new BigDecimal("90.18"), jpyCurrencyRates.get(2).getPrice());
  }

  private static void prepareDefaultTestCase(Properties cryptoProps, RestClient restClient) throws IOException {
    Mockito.when(cryptoProps.getProperty("endpoint.baseurl")).thenReturn(BASE_URL);
    Mockito.when(cryptoProps.getProperty("input.location")).thenReturn(INPUT_LOCATION);
    Mockito.when(cryptoProps.getProperty("default.currency")).thenReturn(DEFAULT_CURRENCY);

    mockRestClientByCryptCurr(restClient, "BTC");
    mockRestClientByCryptCurr(restClient, "ETH");
    mockRestClientByCryptCurr(restClient, "XRP");
  }

  private static void mockRestClientByCryptCurr(RestClient restClient, String cryptCurr)
      throws IOException {
    mockRestClientByCryptCurr(restClient, cryptCurr, DEFAULT_CURRENCY);
  }

  private static void mockRestClientByCryptCurr(RestClient restClient, String cryptCurr,
      String currency) throws IOException {
    HttpURLConnection mockConn = Mockito.mock(HttpURLConnection.class);

    Mockito.when(mockConn.getResponseCode()).thenReturn(200);
    Mockito.when(mockConn.getInputStream()).thenReturn(mockResponse(cryptCurr, currency));
    Mockito.when(restClient.call(BASE_URL, cryptCurr, currency)).thenCallRealMethod();
    Mockito.when(restClient.openConnection(BASE_URL, cryptCurr, currency)).thenReturn(mockConn);
  }
}