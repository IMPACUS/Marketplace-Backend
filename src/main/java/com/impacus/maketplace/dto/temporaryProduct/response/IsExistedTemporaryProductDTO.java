package com.impacus.maketplace.dto.temporaryProduct.response;

import lombok.Data;

@Data
public class IsExistedTemporaryProductDTO {
    private boolean isExisted;

    public IsExistedTemporaryProductDTO(boolean isExisted) {
        this.isExisted = isExisted;
    }
}
