package com.huawei.ibooking.mapper;

import com.huawei.ibooking.model.StudyRoomDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudyRoomMapper {
    List<StudyRoomDO> getStudyRooms();

    StudyRoomDO getStudyRoom(@Param("id") int id);

    int addStudyRoom(@Param("room") StudyRoomDO room);

    int updateStudyRoom(@Param("room") StudyRoomDO room);

    int deleteStudyRoom(@Param("id") int id);
}
