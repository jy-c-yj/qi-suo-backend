package com.cyj.common;

import lombok.Data;

import java.util.List;


@Data
public class JwtClaimType {
    private Long id;
    private List<String> roles;
}
