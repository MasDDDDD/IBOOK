package com.huawei.ibookstudy.service.impl;

import com.huawei.ibookstudy.dao.StudyRoomDao;
import com.huawei.ibookstudy.model.StudyRoomDo;
import com.huawei.ibookstudy.service.StudyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudyRoomServiceImpl implements StudyRoomService {
    private StudyRoomDao studyRoomDao;
    @Autowired
    public StudyRoomServiceImpl(StudyRoomDao studyRoomDao) {
        this.studyRoomDao = studyRoomDao;
    }

    @Override
    public List<StudyRoomDo> getStudyRooms() { return studyRoomDao.getStudyRooms(); }

    @Override
    public Optional<StudyRoomDo> getStudyRoomById(int id) {
        StudyRoomDo room = studyRoomDao.getStudyRoomById(id);
        return Optional.ofNullable(room);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeTime(StudyRoomDo room) {
        int id = room.getId(), startTime = room.getStartTime(), endTime = room.getEndTime();
        room = studyRoomDao.getStudyRoomById(id);
        Map<String, Object> map = new HashMap<>();
        if (endTime <= startTime) {
            map.put("message", "startTime should be smaller than endTime!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        room.setStartTime(startTime);
        room.setEndTime(endTime);
        boolean result = studyRoomDao.updateStudyRoom(room);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeState(StudyRoomDo room) {
        int id = room.getId(), state = room.getState();
        room = studyRoomDao.getStudyRoomById(id);
        Map<String, Object> map = new HashMap<>();
        if (state < 0 || state > 1) {
            map.put("message", "state is illegal!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        room.setState(state);
        boolean result = studyRoomDao.updateStudyRoom(room);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> addStudyRoom(StudyRoomDo room) {
        Map<String, Object> map = new HashMap<>();
        if (room == null || room.getBuildingNum() == null || room.getClassRoomNum() == null) {
            map.put("message", "incomplete parameters!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if (room.getStartTime() == -1) room.setStartTime(7);
        if (room.getEndTime() == -1) room.setEndTime(22);
        boolean result = studyRoomDao.addStudyRoom(room);
        if (result) {
            map.put("id", room.getId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean deleteStudyRoom(int id) {
        return studyRoomDao.deleteStudyRoom(id);
    }
}
