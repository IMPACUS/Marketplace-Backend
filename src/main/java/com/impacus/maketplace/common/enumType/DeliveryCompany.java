package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum DeliveryCompany {
  CJ(1, "CJ"),
  LOTTE(2, "롯데"),
  HANJIN(3, "한진"),
  HYUNDAI(4, "현대"),
  DELIRABBIT(5, "딜리래빗"),

  NONE(100, "알수 없음");

  private final int code;
  private final String value;

  private static final Map<String, DeliveryCompany> nameToEnumMap = new HashMap<>();

  static {
    // enum의 각 요소를 name을 키로 맵에 저장
    for (DeliveryCompany company : values()) {
      nameToEnumMap.put(company.name(), company);
    }
  }

  /**
   * name을 기반으로 DeliveryCompany enum을 가져옵니다.
   *
   * @param name DeliveryCompany의 이름
   * @return 해당 이름에 해당하는 DeliveryCompany enum 값
   */
  public static DeliveryCompany fromName(String name) {
    DeliveryCompany company = nameToEnumMap.get(name);
    if (company == null) {
      company = DeliveryCompany.NONE;
    }
    return company;
  }
}
