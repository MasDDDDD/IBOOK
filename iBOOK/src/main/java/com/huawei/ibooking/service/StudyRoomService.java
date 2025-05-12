package com.huawei.ibooking.service;

import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.model.StudyRoomDO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudyRoomService {
    List<StudyRoomDO> getStudyRooms();
    Optional<StudyRoomDO> getStudyRoomById(final int id);
    ResponseEntity<Map<String, Object>> changeTime(final StudyRoomDO room);
    ResponseEntity<Map<String, Object>> changeState(final StudyRoomDO room);

    ResponseEntity<Map<String, Object>> addStudyRoom(final StudyRoomDO room);
    boolean deleteStudyRoom(final int id);
}
