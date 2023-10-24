package com.jwtpratice.jwttutorial.dao;

import com.jwtpratice.jwttutorial.entity.RefTokenEntity;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberDaoMapper {


    int insertMember(UserEntity userEntity);

    UserEntity isMember(UserEntity userEntity);

    String checkPw(UserEntity userEntity);

    int insertRefToken(RefTokenEntity refTokenEntity);
}
