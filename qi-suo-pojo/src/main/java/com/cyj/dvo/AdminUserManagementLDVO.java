package com.cyj.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserManagementLDVO {
    private Long id;
    private String username;
    private String imageUrl;
    private String email;
    private String phone;
    private String roleName;
    private Integer status;
    private String createTime;


}
