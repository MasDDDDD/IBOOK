package com.huawei.ibookstudy.dao;

import com.huawei.ibookstudy.mapper.StudentMapper;
import com.huawei.ibookstudy.model.StudentDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDao {
    @Autowired
    private StudentMapper studentMapper;

    public List<StudentDo> getStudents() {
        return studentMapper.getStudents();
    }

    public StudentDo getStudentById(final String stuNum) {
        return studentMapper.getStudent(stuNum);
    }

    public boolean addStudent(final StudentDo stu) {
        return studentMapper.addStudent(stu) > 0;
    }

    public boolean updateStudent(final StudentDo stu) {
        return studentMapper.updateStudent(stu) > 0;
    }

    public boolean deleteStudent(final String stuNum) {
        return studentMapper.deleteStudent(stuNum) > 0;
    }
}
