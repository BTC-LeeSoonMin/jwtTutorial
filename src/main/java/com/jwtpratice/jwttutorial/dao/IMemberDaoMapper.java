package com.jwtpratice.jwttutorial.dao;

import com.jwtpratice.jwttutorial.entity.RefTokenEntity;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberDaoMapper {


    int insertMember(UserEntity userEntity);

    UserEntity isMember(UserEntity userEntity);

    int insertRefToken(RefTokenEntity refTokenEntity);

    RefTokenEntity selectRefToken(RefTokenEntity refTokenEntity);

    int deleteDupRefToken(RefTokenEntity checkedRefToken);

    int deleteMember(String userEmail);
}
