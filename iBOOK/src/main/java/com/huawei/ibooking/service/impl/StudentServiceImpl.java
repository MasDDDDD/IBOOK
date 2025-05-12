package com.huawei.ibooking.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.huawei.ibooking.constant.Const;
import com.huawei.ibooking.dao.StudentDao;
import com.huawei.ibooking.mapper.RolesMapper;
import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.service.StudentService;
import com.huawei.ibooking.util.JWTUtils;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public List<StudentDO> getStudents() {
        return studentDao.getStudents();
    }

    @Override
    public Optional<StudentDO> getStudentById(final String stuNum) {
        StudentDO student = studentDao.getStudentById(stuNum);
        return Optional.ofNullable(student);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeName(final String name, HttpServletRequest request, HttpServletResponse response) {
        if(name == null || name.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        StudentDO student = studentDao.getStudentById(stuNum);
        student.setName(name);
        boolean result = studentDao.updateStudent(student);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changePassword(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String oldPass, newPass, rePass;
//        if(params.containsKey("oldPass") && params.containsKey("newPass") && params.containsKey("rePass")) {
        oldPass = params.get("oldPass"); newPass = params.get("newPass"); rePass = params.get("rePass");
//        }

        if(StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass) || StringUtils.isEmpty(rePass)) {
            map.put("message", "incomplete parameters!");
        } else if(!newPass.equals(rePass)) {
            map.put("message", "the two inputs are inconsistent!");
        }
        if(!map.isEmpty()) {
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        StudentDO student = studentDao.getStudentById(stuNum);
        if(!DigestUtils.md5DigestAsHex(oldPass.getBytes()).equals(student.getPassword())) {
            map.put("message", "the old password is incorrect!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        String password = DigestUtils.md5DigestAsHex(newPass.getBytes());
        student.setPassword(password);
        boolean result = studentDao.updateStudent(student);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(StudentDO student, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if(student == null || student.getStuNum() == null || student.getPassword() == null) {
            map.put("message", "incomplete parameters!");
        } else {
            Optional<StudentDO> stu = getStudentById(student.getStuNum());
            if(!stu.isPresent()) {
                status = HttpStatus.NOT_FOUND;
                map.put("message", "user is not exist!");
            } else {
                String password = DigestUtils.md5DigestAsHex(student.getPassword().getBytes());
                System.out.println(password);
                if(stu.get().getPassword().equals(password)) {
                    String token = JWTUtils.createToken(student, rolesMapper.getRoleById(student.getStuNum()));
                    int roleId = rolesMapper.getRoleById(student.getStuNum());
                    map.put("message", "login successfully!");
                    map.put("user", stu);
                    map.put("accessToken", token);
                    map.put("identity", roleId == 1 ? "admin" : "normal");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
                map.put("message", "wrong username or password!");
            }
        }
        return new ResponseEntity<>(map, status);
    }

    @Override
    public ResponseEntity<Map<String, Object>> register(final StudentDO student) {
        Map<String, Object> map = new HashMap<>();
        if(student == null || student.getStuNum() == null || student.getName() == null || student.getPassword() == null) {
            map.put("message", "incomplete parameters!");
        } else {
            Optional<StudentDO> stu = getStudentById(student.getStuNum());
            if(!stu.isPresent()) {
                String password = DigestUtils.md5DigestAsHex(student.getPassword().getBytes());
                student.setPassword(password);
                boolean result = studentDao.addStudent(student);
                boolean result2 = rolesMapper.addRole(student.getStuNum(), 2) > 0;
                if (result && result2) {
                    map.put("message", "register successfully!");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            map.put("message", "username is exist!");
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean deleteStudent(final String stuNum) {
        return studentDao.deleteStudent(stuNum);
    }
}
