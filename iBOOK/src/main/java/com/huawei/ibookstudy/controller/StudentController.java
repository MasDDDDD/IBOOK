package com.huawei.ibookstudy.controller;

import com.huawei.ibookstudy.model.StudentDo;
import com.huawei.ibookstudy.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/v1")
@RestController
public class StudentController {
    private StudentService studentService;
    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(value = "/profile/me")
    public ResponseEntity<Optional<StudentDo>> myProfile(HttpServletRequest request, HttpServletResponse response) {
        String stuNum = request.getAttribute("SESSION_USERNAME").toString();
        return ResponseEntity.ok(studentService.getStudentById(stuNum));
    }

    @PostMapping(value = "/profile/name")
    public ResponseEntity<Map<String, Object>> changeName(@RequestParam String name, HttpServletRequest request, HttpServletResponse response) {
        return studentService.changeName(name, request, response);
    }

    @PostMapping(value = "/profile/password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        return studentService.changePassword(params, request, response);
    }
}
