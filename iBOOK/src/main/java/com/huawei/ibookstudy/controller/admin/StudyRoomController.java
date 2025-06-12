package com.huawei.ibookstudy.controller.admin;

import com.huawei.ibookstudy.model.StudyRoomDo;
import com.huawei.ibookstudy.service.StudyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin")
public class StudyRoomController {
    private StudyRoomService studyRoomService;
    @Autowired
    public StudyRoomController(StudyRoomService studyRoomService) {
        this.studyRoomService = studyRoomService;
    }

    @PostMapping(value = "/studyroom")
    public ResponseEntity<Map<String, Object>> addStudyRoom(@RequestBody StudyRoomDo room) {
        return studyRoomService.addStudyRoom(room);
    }

    @PostMapping(value = "/studyroom/time")
    public ResponseEntity<Map<String, Object>> changeTime(@RequestBody StudyRoomDo room) {
        return studyRoomService.changeTime(room);
    }

    @PostMapping(value = "/studyroom/state")
    public ResponseEntity<Map<String, Object>> changeState(@RequestBody StudyRoomDo room) {
        return studyRoomService.changeState(room);
    }

    @DeleteMapping(value = "studyroom")
    public ResponseEntity<Void> delete(@RequestBody StudyRoomDo room) {
        boolean result = studyRoomService.deleteStudyRoom(room.getId());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
