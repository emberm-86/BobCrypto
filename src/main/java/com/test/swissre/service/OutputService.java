package com.test.swissre.service;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.domain.PortfolioItem;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class OutputService {

  public void printHeader(String currency, int maxPriceStrLen, int maxQuantityStrLen) {
    String title = "Details of your portfolio in " + currency;
    printLineWithSep("\n" + title, title.length());
    printLineWithSep("", title.length());

    String shiftQuantityStr = getShiftedStr(maxPriceStrLen, 5);
    String shiftSumStr = getShiftedStr(maxQuantityStrLen, 9);

    System.out.printf("%-16s " + shiftQuantityStr + " " + shiftSumStr + " %s", "CryptoCurrency",
        "Price", "Quantity", "Sum");
    System.out.println();
  }

  private static String getShiftedStr(int maxPriceStrLen, int extraShift) {
    int shiftQuantity = maxPriceStrLen + extraShift;
    return "%-" + shiftQuantity + "s";
  }

  private static void printLineWithSep(String text, int length) {
    IntStream.range(0, length).forEach(i -> System.out.print("-"));
    System.out.println(text);
  }

  public void print(String currency, Map<String, List<OutputRow>> outputRows) {
    int maxPriceStrLen = maxPriceStrLen(outputRows);
    int maxQuantityStrLen = maxQuantityStrLen(outputRows);

    printHeader(currency, maxPriceStrLen, maxQuantityStrLen);
    List<OutputRow> outputRowByCurr = outputRows.get(currency);
    outputRowByCurr.forEach(outputRow ->
        printOutputRow(outputRow, maxPriceStrLen, maxQuantityStrLen));
  }

  private void printOutputRow(OutputRow outputRow, int maxPriceStrLen, int maxQuantityStrLen) {
    PortfolioItem portfolioItem = outputRow.getPortfolioItem();
    BigDecimal price = outputRow.getPrice();
    String cryptoCurrency = portfolioItem.getCryptoCurrency();
    BigDecimal quantity = new BigDecimal(String.valueOf(portfolioItem.getQuantity()));

    String shiftQuantityStr = getShiftedStr(maxPriceStrLen, 5);
    String shiftSumStr = getShiftedStr(maxQuantityStrLen, 9);

    System.out.printf("%-16s " + shiftQuantityStr + " " + shiftSumStr +" %s", cryptoCurrency, price, quantity,
        price.multiply(quantity));
    System.out.println();
  }

  private int maxPriceStrLen(Map<String, List<OutputRow>> outputRows) {
    return outputRows.values().stream()
        .flatMap(Collection::stream)
        .map(OutputRow::getPrice)
        .map(BigDecimal::toString)
        .map(String::length)
        .max(Integer::compareTo).orElse("Price".length());
  }

  private int maxQuantityStrLen(Map<String, List<OutputRow>> outputRows) {
    return outputRows.values().stream()
        .flatMap(Collection::stream)
        .map(outputRow -> outputRow.getPortfolioItem().getQuantity())
        .map(String::valueOf)
        .map(String::length)
        .max(Integer::compareTo).orElse("Quantity".length());
  }
}
