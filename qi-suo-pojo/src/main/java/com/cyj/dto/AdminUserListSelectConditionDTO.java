package com.cyj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserListSelectConditionDTO {
    private Long id;
    private String username;
    private String email;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
