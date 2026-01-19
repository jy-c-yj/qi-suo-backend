package com.cyj.dvo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenDVO {
    private String access_token;
    private String refresh_token_key;
}
