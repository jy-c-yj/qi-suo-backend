package com.cyj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewProductCategoryDTO {
    private Integer productId;
    private Integer categoryId;
}
