package com.cyj.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDTO {

    private String username;

    private String password;

    private String imageUrl;

    private String phone;

    private String email;

}
