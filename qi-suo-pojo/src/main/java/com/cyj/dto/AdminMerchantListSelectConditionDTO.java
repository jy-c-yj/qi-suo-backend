package com.cyj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMerchantListSelectConditionDTO {
    private Long id;
    private String merchantName;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
