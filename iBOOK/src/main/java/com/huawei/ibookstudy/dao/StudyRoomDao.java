package com.huawei.ibookstudy.dao;

import com.huawei.ibookstudy.mapper.StudyRoomMapper;
import com.huawei.ibookstudy.model.StudyRoomDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudyRoomDao {
    @Autowired
    private StudyRoomMapper studyRoomMapper;

    public List<StudyRoomDo> getStudyRooms(){
        return studyRoomMapper.getStudyRooms();
    }

    public StudyRoomDo getStudyRoomById(final int id) {
        return studyRoomMapper.getStudyRoom(id);
    }

    public boolean addStudyRoom(final StudyRoomDo room) {
        return studyRoomMapper.addStudyRoom(room) > 0;
    }

    public boolean updateStudyRoom(final StudyRoomDo room) {
        return studyRoomMapper.updateStudyRoom(room) > 0;
    }

    public boolean deleteStudyRoom(final int id) {
        return studyRoomMapper.deleteStudyRoom(id) > 0;
    }
}
