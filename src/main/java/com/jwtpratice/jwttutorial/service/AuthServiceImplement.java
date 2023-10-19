package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.dao.IMemberDaoMapper;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import com.jwtpratice.jwttutorial.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImplement implements AuthService {

    @Value("${secret-key}")
    private String secretKey;

    private Long accessTokenExpireTimeMs = 1000 * 60 * 5l;
    private Long RefreshTokenExpireTimeMs = 1000 * 60 * 10l;

    @Autowired
    IMemberDaoMapper iMemberDaoMapper;

    @Autowired
    JwtProvider jwtProvider;


    private PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();

    //
    @Override
    public String signUp(Map<String, Object> msgMap, UserEntity userEntity) {
        System.out.println("[AuthServiceImplement] signUp");

            userEntity.setEmail(msgMap.get("email").toString());
            userEntity.setM_password(passwordEncoder.encode(msgMap.get("m_password").toString()));
            userEntity.setNickname(msgMap.get("nickname").toString());


            int result = -1;
            result = iMemberDaoMapper.insertMember(userEntity);
            if(result > 0){
                System.out.println("SIGN IN SUCCESS");
                return "SIGN IN SUCCESS";
            } else {
                System.out.println("SIGN IN FAIL");
                return "SIGN IN FAIL";
            }

    }

    @Override
    public Map<String,Object> signIn(Map<String, Object> msgMap, UserEntity userEntity) {
        String password = msgMap.get("m_password").toString();

        Map<String, Object> map = new HashMap<>();

        // 동일한 username 없음
        userEntity = iMemberDaoMapper.isMember(userEntity);
        System.out.println("000 : "+userEntity);
        if(userEntity == null){
            System.out.println("user null");
//            return HttpStatus.NOT_FOUND+" user가 없습니다";
            map.put("result", HttpStatus.NOT_FOUND);
            return map;
        }
        // password check
        String DBpassword = userEntity.getM_password();

        if(!passwordEncoder.matches(password, DBpassword)){
//            return HttpStatus.BAD_REQUEST+"비밀번호가 틀렸습니다.";
            map.put("result", HttpStatus.BAD_REQUEST);
            return map;
        }

        System.out.println("tp1");
        String accessToken = jwtProvider.createToken(userEntity.getEmail(), secretKey, accessTokenExpireTimeMs);
        String refreshToken = jwtProvider.createToken(userEntity.getEmail(), secretKey, RefreshTokenExpireTimeMs);
        System.out.println(accessToken);
        System.out.println(refreshToken);
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }
}
