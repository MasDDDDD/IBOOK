package com.huawei.ibookstudy.service.impl;

import com.huawei.ibookstudy.dao.SeatDao;
import com.huawei.ibookstudy.dao.StudyRoomDao;
import com.huawei.ibookstudy.model.SeatDo;
import com.huawei.ibookstudy.model.StudyRoomDo;
import com.huawei.ibookstudy.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatServiceImpl implements SeatService {
    final SeatDao seatDao;
    final StudyRoomDao studyRoomDao;

    @Autowired
    public SeatServiceImpl(SeatDao seatDao, StudyRoomDao studyRoomDao) {
        this.seatDao = seatDao;
        this.studyRoomDao = studyRoomDao;
    }

    @Override
    public List<SeatDo> getSeatsByRoomId(int roomId) {
        return seatDao.getSeatsByRoomId(roomId);
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeState(SeatDo seat) {
        int state = seat.getState();

        Map<String, Object> map = new HashMap<>();
        if(state < 0 || state > 2) {
            map.put("message", "state is illegal!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        boolean result = seatDao.updateSeat(seat);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> addSeat(SeatDo seat) {
        Map<String, Object> map = new HashMap<>();
        if(seat == null || seat.getStudyRoomId() == -1) {
            map.put("message", "incomplete parameters!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        // 判断自习室是否存在
        StudyRoomDo room = studyRoomDao.getStudyRoomById(seat.getStudyRoomId());
        if(ObjectUtils.isEmpty(room)) {
            map.put("message", "this studyRoom is not exist!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        boolean result = seatDao.addSeat(seat);
        if(result) {
            map.put("id", seat.getId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean deleteSeat(int id) {
        return seatDao.deleteSeat(id);
    }

}
