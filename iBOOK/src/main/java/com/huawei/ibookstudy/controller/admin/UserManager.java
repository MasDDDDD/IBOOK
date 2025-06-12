package com.huawei.ibookstudy.controller.admin;

import com.huawei.ibookstudy.model.StudentDo;
import com.huawei.ibookstudy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/admin")
public class UserManager {
    private StudentService studentService;
    @Autowired
    public UserManager(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(value = "/student")
    public ResponseEntity<List<StudentDo>> list() {
        final List<StudentDo> students = studentService.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping(value = "/student/{stuNum}")
    public ResponseEntity<StudentDo> query(@PathVariable("stuNum") String stuNum) {
        Optional<StudentDo> stu = studentService.getStudentById(stuNum);
        return stu.map(studentDo -> new ResponseEntity<>(studentDo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping(value = "/student")
    public ResponseEntity<Void> delete(@RequestBody StudentDo student) {
        boolean result = studentService.deleteStudent(student.getStuNum());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
