package com.test.swissre.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OutputRow {
  PortfolioItem portfolioItem;
  BigDecimal price;
}
