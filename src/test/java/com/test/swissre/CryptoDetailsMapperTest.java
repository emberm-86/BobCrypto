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

    HttpURLConnection mockHttpURLConnection = Mockito.mock(HttpURLConnection.class);
    Mockito.when(mockHttpURLConnection.getResponseCode()).thenReturn(200);
    Mockito.when(mockHttpURLConnection.getInputStream())
        .thenReturn(mockResponse("BTC", DEFAULT_CURRENCY));

    Mockito.when(restClient.call(BASE_URL, "BTC", DEFAULT_CURRENCY))
        .thenCallRealMethod();
    Mockito.when(restClient.openConnection(BASE_URL, "BTC", DEFAULT_CURRENCY))
        .thenReturn(mockHttpURLConnection);

    Map<String, List<OutputRow>> map = cryptoDetailsMapper.map(new String[]{}, cryptoProps);

    assertEquals(1, map.size());
    List<OutputRow> defaultCurrencyRates = map.get(DEFAULT_CURRENCY);
    assertEquals(3, defaultCurrencyRates.size());
    assertEquals(new BigDecimal("26499.77"), defaultCurrencyRates.get(0).getPrice());
  }
}
