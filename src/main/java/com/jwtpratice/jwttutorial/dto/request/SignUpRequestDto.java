package com.jwtpratice.jwttutorial.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    // 문자열에서 null,공백,빈문자열 불허
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min=8, max=20)
    private String password;

    private String nickname;

    @NotBlank @Pattern(regexp="^[0-9]{11,13}$")
    private String telNumber;

    private String address;

    private String addressDetail;

    private String agreedPersonal;

}
