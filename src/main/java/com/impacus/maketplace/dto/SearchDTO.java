package com.impacus.maketplace.dto;

import com.impacus.maketplace.common.enumType.SearchType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private String searchName;
    private SearchType type;
    private Long searchId;
}
