package com.huawei.ibooking.controller.admin;

import com.huawei.ibooking.model.StudyRoomDO;
import com.huawei.ibooking.service.StudyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin")
public class StudyRoomController {
    @Autowired
    private StudyRoomService studyRoomService;

    @PostMapping(value = "/studyroom")
    public ResponseEntity<Map<String, Object>> addStudyRoom(@RequestBody StudyRoomDO room) {
        return studyRoomService.addStudyRoom(room);
    }

    @PostMapping(value = "/studyroom/time")
    public ResponseEntity<Map<String, Object>> changeTime(@RequestBody StudyRoomDO room) {
        return studyRoomService.changeTime(room);
    }

    @PostMapping(value = "/studyroom/state")
    public ResponseEntity<Map<String, Object>> changeState(@RequestBody StudyRoomDO room) {
        return studyRoomService.changeState(room);
    }

    @DeleteMapping(value = "/studyroom")
    public ResponseEntity<Void> delete(@RequestBody StudyRoomDO room) {
        boolean result = studyRoomService.deleteStudyRoom(room.getId());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
