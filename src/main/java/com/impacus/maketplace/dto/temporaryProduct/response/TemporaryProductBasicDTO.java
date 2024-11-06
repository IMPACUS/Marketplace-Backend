package com.impacus.maketplace.dto.temporaryProduct.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.dto.product.response.WebProductBasicDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemporaryProductBasicDTO extends WebProductBasicDTO {
}
