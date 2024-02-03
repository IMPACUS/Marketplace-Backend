package com.impacus.maketplace.dto.temporaryProduct.response;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import lombok.Data;

@Data
public class SimpleTemporaryProductDTO {
    private Long id;
    private String name;

    public SimpleTemporaryProductDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SimpleTemporaryProductDTO toDTO(TemporaryProduct temporaryProduct) {
        return new SimpleTemporaryProductDTO(
                temporaryProduct.getId(),
                temporaryProduct.getName()
        );
    }
}
