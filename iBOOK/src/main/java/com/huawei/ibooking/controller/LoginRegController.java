package com.huawei.ibooking.controller;

import com.huawei.ibooking.constant.Const;
import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.service.StudentService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/v1")
@RestController
public class LoginRegController {
    @Autowired
    private StudentService studentService;

    private final Log log = LogFactory.getLog(getClass());

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody StudentDO student, HttpServletRequest request, HttpServletResponse response) {
        return studentService.login(student, request, response);
    }

    @PostMapping(value = "/logout")
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody StudentDO student) {
        return studentService.register(student);
    }
}
