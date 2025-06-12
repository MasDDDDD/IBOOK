package com.huawei.ibookstudy.mapper;

import com.huawei.ibookstudy.model.StudyRoomDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyRoomMapper {
    List<StudyRoomDo> getStudyRooms();

    StudyRoomDo getStudyRoom(@Param("id") int id);

    int addStudyRoom(@Param("room") StudyRoomDo room);

    int updateStudyRoom(@Param("room") StudyRoomDo room);

    int deleteStudyRoom(@Param("id") int id);
}
