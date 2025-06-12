package com.huawei.ibookstudy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolesMapper {
    int addRole(@Param("stuId") String stuId, @Param("rid") int rid);

    int getRoleById(String stuId);
}
