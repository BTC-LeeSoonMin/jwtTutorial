package com.jwtpratice.jwttutorial.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class SignUpRequestDto {


    private String email;
    private String m_password;
    private String nickname;
    private String telNumber;
    private String address;
    private String addressDetail;

}
