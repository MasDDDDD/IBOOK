package com.huawei.ibooking.service;

import com.huawei.ibooking.model.StudentDO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentService {
    List<StudentDO> getStudents();
    Optional<StudentDO> getStudentById(final String stuNum);
    ResponseEntity<Map<String, Object>> changeName(final String name, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Map<String, Object>> changePassword(Map<String, String> params, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<Map<String, Object>> register(final StudentDO stu);
    ResponseEntity<Map<String, Object>> login(final StudentDO stu, HttpServletRequest request, HttpServletResponse response);

    boolean deleteStudent(final String stuNum);
}
