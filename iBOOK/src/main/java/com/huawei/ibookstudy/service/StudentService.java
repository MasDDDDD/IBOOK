package com.huawei.ibookstudy.service;


import com.huawei.ibookstudy.model.StudentDo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentService {
    List<StudentDo> getStudents();
    Optional<StudentDo> getStudentById(final String stuNum);
    ResponseEntity<Map<String, Object>> changeName(final String name, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Map<String, Object>> changePassword(Map<String, String> params, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Map<String, Object>> register(final StudentDo stu);
    ResponseEntity<Map<String, Object>> login(final StudentDo stu, HttpServletRequest request, HttpServletResponse response);
    boolean deleteStudent(final String stuNum);
}
