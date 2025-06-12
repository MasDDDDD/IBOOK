package com.huawei.ibooking.controller.admin;

import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/admin")
public class UserManager {
    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/student")
    public ResponseEntity<List<StudentDO>> list() {
        final List<StudentDO> students = studentService.getStudents();

        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping(value = "/student/{stuNum}")
    public ResponseEntity<StudentDO> query(@PathVariable("stuNum") String stuNum) {
        Optional<StudentDO> stu = studentService.getStudentById(stuNum);

        return stu.map(studentDO -> new ResponseEntity<>(studentDO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/student")
    public ResponseEntity<Void> delete(@RequestBody StudentDO student) {
        boolean result = studentService.deleteStudent(student.getStuNum());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
