package com.cyj.dto;


import com.cyj.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationUpdateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String imageUrl;
    private String phone;
    private String email;

    public static User toUser(UserInformationUpdateDTO userInformationUpdateDTO, Long userId) {
            return User.builder()
                    .id(userId)
                    .username(userInformationUpdateDTO.getUsername())
                    .password(userInformationUpdateDTO.getPassword())
                    .imageUrl(userInformationUpdateDTO.getImageUrl())
                    .phone(userInformationUpdateDTO.getPhone())
                    .email(userInformationUpdateDTO.getEmail())
                    .build();
    }
}
