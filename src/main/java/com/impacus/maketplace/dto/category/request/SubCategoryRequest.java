package com.impacus.maketplace.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequest {
    @NotNull
    private Long superCategoryId;

    @NotBlank
    @Size(max = 10)
    private String name;
}
