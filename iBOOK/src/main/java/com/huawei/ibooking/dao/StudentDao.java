package com.huawei.ibooking.dao;

import com.huawei.ibooking.mapper.StudentMapper;
import com.huawei.ibooking.model.StudentDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDao {
    @Autowired
    private StudentMapper studentMapper;

    public List<StudentDO> getStudents() {
        return studentMapper.getStudents();
    }

    public StudentDO getStudentById(final String stuNum) {
        return studentMapper.getStudent(stuNum);
    }

    public boolean addStudent(final StudentDO stu) {
        return studentMapper.addStudent(stu) > 0;
    }

    public boolean updateStudent(final StudentDO stu) { return studentMapper.updateStudent(stu) > 0; }

    public boolean deleteStudent(final String stuNum) {
        return studentMapper.deleteStudent(stuNum) > 0;
    }
}
