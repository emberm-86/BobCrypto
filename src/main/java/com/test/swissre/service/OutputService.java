package com.test.swissre.service;

import com.test.swissre.domain.OutputRow;
import com.test.swissre.domain.PortfolioItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public class OutputService {

  public void printHeader(String currency, int maxPriceStrLen, int maxQuantityStrLen) {
    String title = "Details of your portfolio in " + currency;
    IntStream.range(0, title.length()).forEach(i -> System.out.print("-"));
    System.out.println("\n" + title);
    IntStream.range(0, title.length()).forEach(i -> System.out.print("-"));
    System.out.println();

    int shiftQuantity = maxPriceStrLen + 5;
    String shiftQuantityStr = "%-" + shiftQuantity + "s";

    int shiftSum = maxQuantityStrLen + 9;
    String shiftSumStr = "%-" + shiftSum + "s";

    System.out.printf("%-16s " + shiftQuantityStr + " " + shiftSumStr + " %s", "CryptoCurrency",
        "Price", "Quantity", "Sum");
    System.out.println();
  }

  public void print(String currency, List<OutputRow> outputRows) {
    int maxPriceStrLen = maxPriceStrLen(outputRows);
    int maxQuantityStrLen = maxQuantityStrLen(outputRows);

    printHeader(currency, maxPriceStrLen, maxQuantityStrLen);
    outputRows.forEach(outputRow -> printOutputRow(outputRow, maxPriceStrLen, maxQuantityStrLen));
  }

  private static void printOutputRow(OutputRow outputRow, int maxPriceStrLen,
      int maxQuantityStrLen) {
    PortfolioItem portfolioItem = outputRow.getPortfolioItem();
    BigDecimal price = outputRow.getPrice();
    String cryptoCurrency = portfolioItem.getCryptoCurrency();
    BigDecimal quantity = new BigDecimal(String.valueOf(portfolioItem.getQuantity()));

    int shiftQuantity = maxPriceStrLen + 5;
    String shiftQuantityStr = "%-" + shiftQuantity + "s";

    int shiftSum = maxQuantityStrLen + 9;
    String shiftSumStr = "%-" + shiftSum + "s";

    System.out.printf("%-16s " + shiftQuantityStr + " " + shiftSumStr +" %s", cryptoCurrency, price, quantity,
        price.multiply(quantity));
    System.out.println();
  }

  private int maxPriceStrLen(List<OutputRow> outputRows) {
    return outputRows.stream()
        .map(OutputRow::getPrice)
        .map(BigDecimal::toString)
        .map(String::length)
        .max(Integer::compareTo).orElse("Price".length());
  }

  private int maxQuantityStrLen(List<OutputRow> outputRows) {
    return outputRows.stream()
        .map(outputRow -> outputRow.getPortfolioItem().getQuantity())
        .map(String::valueOf)
        .map(String::length)
        .max(Integer::compareTo).orElse("Quantity".length());
  }
}
