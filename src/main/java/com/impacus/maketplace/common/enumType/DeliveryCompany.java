package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryCompany {
  NONE(100, "알수 없음");

  private final int code;
  private final String value;
}
