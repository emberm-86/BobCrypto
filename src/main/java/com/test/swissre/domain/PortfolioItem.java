package com.test.swissre.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PortfolioItem {
  String cryptoCurrency;
  int quantity;
}
