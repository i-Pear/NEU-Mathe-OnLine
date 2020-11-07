package com.ipear.web.training.mapper;

import com.ipear.web.training.entity.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("select * from table")
    @Results({
            @Result(property = "uid",column = "uid"),
            @Result(property = "alias",column = "alias"),
            @Result(property = "password",column = "password"),
            @Result(property = "sex",column = "sex",javaType = boolean.class)
    })
    List<User> getUser();

    @Select("")


}
