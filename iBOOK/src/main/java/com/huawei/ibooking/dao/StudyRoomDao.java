package com.huawei.ibooking.dao;

import com.huawei.ibooking.mapper.StudyRoomMapper;
import com.huawei.ibooking.model.StudyRoomDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudyRoomDao {
    @Autowired
    private StudyRoomMapper studyRoomMapper;

    public List<StudyRoomDO> getStudyRooms() {
        return studyRoomMapper.getStudyRooms();
    }

    public StudyRoomDO getStudyRoomById(final int id) {
        return studyRoomMapper.getStudyRoom(id);
    }

    public boolean addStudyRoom(final StudyRoomDO room) {
        return studyRoomMapper.addStudyRoom(room) > 0;
    }

    public boolean updateStudyRoom(final StudyRoomDO room) { return studyRoomMapper.updateStudyRoom(room) > 0; }

    public boolean deleteStudyRoom(final int id) {
        return studyRoomMapper.deleteStudyRoom(id) > 0;
    }
}
