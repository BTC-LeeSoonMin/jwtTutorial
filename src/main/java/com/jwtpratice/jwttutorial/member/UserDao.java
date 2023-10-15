package com.jwtpratice.jwttutorial.member;

import com.jwtpratice.jwttutorial.entity.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final SqlSessionTemplate sqlSession;

    @Autowired
    public UserDao(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

//    public User getUserById(Integer id) {
//        return sqlSession.getMapper(UserMapper.class).getUserById(id);
//    }
}