package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.dto.request.SignUpRequestDto;
import com.jwtpratice.jwttutorial.dto.response.ResponseDto;
import com.jwtpratice.jwttutorial.dto.response.auth.SignUpResponseDto;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import com.jwtpratice.jwttutorial.repository.UserRespository;
import com.jwtpratice.jwttutorial.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRespository userRespository;

    private PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();

    //
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {

            String email = dto.getEmail();
            boolean existedEmail = userRespository.existsByEmail(email);
            if (existedEmail) return SignUpResponseDto.duplicateEmail();
            String nickname = dto.getNickname();
            boolean existedNickname = userRespository.existsByNickname(nickname);
            if (existedNickname) return SignUpResponseDto.duplicateNickname();

            String telNumber = dto.getTelNumber();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRespository.save(userEntity);


        }  catch(Exception  e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }
}
