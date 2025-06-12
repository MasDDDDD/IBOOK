package com.huawei.ibookstudy.mapper;

import com.huawei.ibookstudy.model.StudentDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<StudentDo> getStudents();

    StudentDo getStudent(@Param("stuNum") String stuNum);

    int addStudent(@Param("stu") StudentDo stu);

    int updateStudent(@Param("stu") StudentDo stu);

    int deleteStudent(@Param("stuNum") String stuNum);
}

