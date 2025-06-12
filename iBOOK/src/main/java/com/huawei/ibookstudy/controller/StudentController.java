package com.huawei.ibooking.controller;

import com.alibaba.druid.util.StringUtils;
import com.huawei.ibooking.constant.Const;
import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/v1")
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/profile/me")
    public ResponseEntity<Optional<StudentDO>> myProfile(HttpServletRequest request, HttpServletResponse response) {
        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        return new ResponseEntity<>(studentService.getStudentById(stuNum), HttpStatus.OK);
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
