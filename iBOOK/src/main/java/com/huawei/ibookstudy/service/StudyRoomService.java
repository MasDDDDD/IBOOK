package com.huawei.ibookstudy.service;

import com.huawei.ibookstudy.model.StudyRoomDo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudyRoomService {
    List<StudyRoomDo> getStudyRooms();
    Optional<StudyRoomDo> getStudyRoomById(final int id);
    ResponseEntity<Map<String, Object>> changeTime(final StudyRoomDo room);
    ResponseEntity<Map<String, Object>> changeState(final StudyRoomDo room);

    ResponseEntity<Map<String, Object>> addStudyRoom(final StudyRoomDo room);
    boolean deleteStudyRoom(final int id);
}
