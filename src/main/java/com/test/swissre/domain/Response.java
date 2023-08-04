package com.test.swissre.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Response {
  String currency;
  BigDecimal price;
}
