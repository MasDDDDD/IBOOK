package com.huawei.ibookstudy.controller;

import com.huawei.ibookstudy.model.StudentDo;
import com.huawei.ibookstudy.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.juli.logging.LogFactory;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/v1")
@RestController
public class LoginRegController {
    private StudentService studentService;
    @Autowired
    public LoginRegController(StudentService studentService) {
        this.studentService = studentService;
    }

    private final Log log = LogFactory.getLog(getClass());

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody StudentDo student, HttpServletRequest request, HttpServletResponse response) {
        return studentService.login(student, request, response);
    }

    @PostMapping(value = "logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("message", "logout successfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e);
            map.put("message", "logout failed!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody StudentDo student) {
        return studentService.register(student);
    }
}
