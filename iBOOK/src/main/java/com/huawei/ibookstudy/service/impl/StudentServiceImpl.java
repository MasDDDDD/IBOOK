package com.huawei.ibookstudy.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.huawei.ibookstudy.constant.Const;
import com.huawei.ibookstudy.dao.StudentDao;
import com.huawei.ibookstudy.mapper.RolesMapper;
import com.huawei.ibookstudy.model.StudentDo;
import com.huawei.ibookstudy.service.StudentService;
import com.huawei.ibookstudy.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private StudentDao studentDao;
    private RolesMapper rolesMapper;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, RolesMapper rolesMapper) {
        this.studentDao = studentDao;
        this.rolesMapper = rolesMapper;
    }

    @Override
    public List<StudentDo> getStudents() { return studentDao.getStudents(); }

    @Override
    public Optional<StudentDo> getStudentById(final String stuNum) {
        StudentDo student = studentDao.getStudentById(stuNum);
        return Optional.ofNullable(student);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeName(final String name, HttpServletRequest request, HttpServletResponse response) {
        if (name == null || name.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        StudentDo student = studentDao.getStudentById(stuNum);
        student.setName(name);
        boolean result = studentDao.updateStudent(student);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changePassword(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String oldPass, newPass, rePass;
        oldPass = params.get("oldPass");
        newPass = params.get("newPass");
        rePass = params.get("rePass");

        if (StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass) || StringUtils.isEmpty(rePass)) {
            map.put("message", "incomplete parameters");
        }
        else if (!newPass.equals(rePass)) {
            map.put("message", "the new password and the repeated password do not match");
        }
        if (!map.isEmpty()) {
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        StudentDo student = studentDao.getStudentById(stuNum);
        if (!DigestUtils.md5DigestAsHex(oldPass.getBytes()).equals(student.getPassword())) {
            map.put("message", "the old password is incorrect");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        String password = DigestUtils.md5DigestAsHex(newPass.getBytes());
        student.setPassword(password);
        boolean result = studentDao.updateStudent(student);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(StudentDo student, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (student == null || student.getStuNum() == null || student.getPassword() == null) {
            map.put("message", "incomplete parameters");
        }
        else {
            Optional<StudentDo> stu = getStudentById(student.getStuNum());
            if (stu.isEmpty()) {
                status = HttpStatus.NOT_FOUND;
                map.put("message", "user does not exist");
            }
            else {
                String password = DigestUtils.md5DigestAsHex(student.getPassword().getBytes());
                if (stu.get().getPassword().equals(password)) {
                    String token = JWTUtils.createToken(student, rolesMapper.getRoleById(student.getStuNum()));
                    int roleId = rolesMapper.getRoleById(student.getStuNum());
                    map.put("message", "login successful");
                    map.put("user", stu);
                    map.put("accessToken", token);
                    map.put("identity", roleId == 1 ? "admin" : "normal");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
                map.put("message", "wrong username or password");
            }
        }
        return new ResponseEntity<>(map, status);
    }

    @Override
    public ResponseEntity<Map<String, Object>> register(final StudentDo student) {
        Map<String, Object> map = new HashMap<>();
        if (student == null || student.getStuNum() == null || student.getName() == null || student.getPassword() == null) {
            map.put("message", "incomplete parameters");
        }
        else {
            Optional<StudentDo> stu = getStudentById(student.getStuNum());
            if (stu.isEmpty()) {
                String password = DigestUtils.md5DigestAsHex(student.getPassword().getBytes());
                student.setPassword(password);
                boolean result = studentDao.addStudent(student);
                boolean result2 = rolesMapper.addRole(student.getStuNum(), 2) > 0;
                if (result && result2) {
                    map.put("message", "register successfully!");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            map.put("message", "username already exists");
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean deleteStudent(final String stuNum) { return studentDao.deleteStudent(stuNum); }
}
