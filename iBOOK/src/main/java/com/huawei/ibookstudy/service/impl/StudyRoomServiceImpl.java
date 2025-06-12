package com.huawei.ibooking.service.impl;

import com.huawei.ibooking.dao.StudyRoomDao;
import com.huawei.ibooking.model.StudyRoomDO;
import com.huawei.ibooking.service.StudyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudyRoomServiceImpl implements StudyRoomService {
    @Autowired
    private StudyRoomDao studyRoomDao;

    @Override
    public List<StudyRoomDO> getStudyRooms() {
        return studyRoomDao.getStudyRooms();
    }

    @Override
    public Optional<StudyRoomDO> getStudyRoomById(int id) {
        StudyRoomDO room = studyRoomDao.getStudyRoomById(id);
        return Optional.ofNullable(room);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeTime(StudyRoomDO room) {
        int id = room.getId(), startTime = room.getStartTime(), endTime = room.getEndTime();
        room = studyRoomDao.getStudyRoomById(id);
        Map<String, Object> map = new HashMap<>();
        if(endTime <= startTime) {
            map.put("message", "startTime should be smaller than endTime!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        room.setStartTime(startTime);
        room.setEndTime(endTime);
        boolean result = studyRoomDao.updateStudyRoom(room);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeState(StudyRoomDO room) {
        int id = room.getId(), state = room.getState();
        room = studyRoomDao.getStudyRoomById(id);
        Map<String, Object> map = new HashMap<>();
        if(state < 0 || state > 1) {
            map.put("message", "state is illegal!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        room.setState(state);
        boolean result = studyRoomDao.updateStudyRoom(room);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> addStudyRoom(StudyRoomDO room) {
        Map<String, Object> map = new HashMap<>();
        if(room == null || room.getBuildingNum() == null || room.getClassRoomNum() == null) {
            map.put("message", "incomplete parameters!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(room.getStartTime() == -1) room.setStartTime(7);
        if(room.getEndTime() == -1) room.setEndTime(22);
        boolean result = studyRoomDao.addStudyRoom(room);
        if(result) {
            map.put("id", room.getId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean deleteStudyRoom(int id) {
        return studyRoomDao.deleteStudyRoom(id);
    }
}
