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

    Mockito.when(cryptoProps.getProperty("endpoint.baseurl")).thenReturn(BASE_URL);
    Mockito.when(cryptoProps.getProperty("input.location")).thenReturn(INPUT_LOCATION);
    Mockito.when(cryptoProps.getProperty("default.currency")).thenReturn(DEFAULT_CURRENCY);

    RestClient restClient = Mockito.mock(RestClient.class);
    CryptoDetailsMapper cryptoDetailsMapper = new CryptoDetailsMapper(restClient);

    mockRestClientByCryptCurr(restClient, "BTC");
    mockRestClientByCryptCurr(restClient, "ETH");
    mockRestClientByCryptCurr(restClient, "XRP");

    Map<String, List<OutputRow>> map = cryptoDetailsMapper.map(new String[]{}, cryptoProps);

    assertEquals(1, map.size());
    List<OutputRow> defaultCurrencyRates = map.get(DEFAULT_CURRENCY);

    assertEquals(3, defaultCurrencyRates.size());
    assertEquals(new BigDecimal("26466.25"), defaultCurrencyRates.get(0).getPrice());
    assertEquals(new BigDecimal("1675.12"), defaultCurrencyRates.get(1).getPrice());
    assertEquals(new BigDecimal("0.5663"), defaultCurrencyRates.get(2).getPrice());
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