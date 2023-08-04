package com.test.swissre.util;

import com.test.swissre.domain.PortfolioItem;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PortfolioParser {

  public static List<PortfolioItem> parse(String fileLocation) {
    InputStream portfolioContent = PortfolioParser.class.getClassLoader()
        .getResourceAsStream(fileLocation);

    if (portfolioContent == null) {
      throw new RuntimeException("Portfolio file location is invalid!");
    }

    List<PortfolioItem> portfolioItems = new LinkedList<>();

    try (Scanner scanner = new Scanner(portfolioContent)) {
      while (scanner.hasNextLine()) {
          String nextLine = scanner.nextLine();
          addPortfolioItem(portfolioItems, nextLine);
      }
    }

    return portfolioItems;
  }

  private static void addPortfolioItem(List<PortfolioItem> portfolioItems, String nextLine) {
    String[] keyValue = nextLine.split("=");
    String cryptoCurrency = keyValue[0];
    int quantity;

    try {
      quantity = Integer.parseInt(keyValue[1]);
    } catch (NumberFormatException e) {
      throw new RuntimeException("The file is invalid for this cryptocurrency" + cryptoCurrency);
    }

    portfolioItems.add(new PortfolioItem(cryptoCurrency, quantity));
  }
}
