package com.huawei.ibooking.service.impl;

import com.huawei.ibooking.constant.Const;
import com.huawei.ibooking.dao.BookingDao;
import com.huawei.ibooking.dao.SeatDao;
import com.huawei.ibooking.dao.StudentDao;
import com.huawei.ibooking.dao.StudyRoomDao;
import com.huawei.ibooking.model.BookingDo;
import com.huawei.ibooking.model.SeatDo;
import com.huawei.ibooking.model.StudentDO;
import com.huawei.ibooking.model.StudyRoomDO;
import com.huawei.ibooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private StudyRoomDao studyRoomDao;
    @Autowired
    private StudentDao studentDao;

    @Override
    public ResponseEntity<List<BookingDo>> getBookingListByStuNum(String stuNum) {
        StudentDO stu = studentDao.getStudentById(stuNum);
        if(stu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<BookingDo> bookingList = bookingDao.getBookingListByStuNum(stuNum);
        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookingDo>> getBookingListBySeatId(int seatId) {
        List<BookingDo> bookingList = bookingDao.getBookingListBySeatId(seatId);
        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    /**
     * 检查用户是否可以进行签到
     */
    public Map<String, Object> checkSignIn(BookingDo booking, int id, int hour, int minute) {
        Map<String, Object> map = new HashMap<>();
        if(booking.getState() != 0 && booking.getState() != -1) {
            map.put("message", "you cannot sign in!");
        } else if(hour < getHour(booking.getStartTime())) {
            map.put("message", "it's too early to sign in!");
        } else if(booking.getState() == 0 && (hour > getHour(booking.getStartTime()) || minute > 15)) {
            map.put("message", "it's too late to sign in!");
            updateBookingState(id, 3, "not sign in on time!");
        } else if(booking.getState() == -1 && (hour > getHour(booking.getStartTime()) || minute > 5)) {
            map.put("message", "it's too late to sign in!");
            updateBookingState(id, 3, "not sign in on time!");
        }
        return map;
    }

    public void releaseSeat(){
        List<BookingDo> bookingList = bookingDao.getAvailableBookingList();
        List<BookingDo> filtered = bookingList.stream().filter(bookingDo -> bookingDo.getState() == 1).collect(Collectors.toList());
        for(BookingDo book : filtered) {
            bookingDao.updateBookingState(book.getId(), 4, "");
        }
    }

    public Map<String, Object> checkState(BookingDo booking, int id, int hour, int minute, int state){
        Map<String, Object> map = new HashMap<>();
        // 签到
        if(state == 1) map = checkSignIn(booking, id, hour, minute);
        else if(state == 2) {
            // 取消预约
            if(booking.getState() != 0 || hour > getHour(booking.getStartTime())) {
                map.put("message", "you cannot cancel this booking!");
            }
        } else if(state == 4 && booking.getState() != 1) {
            // 签退
            map.put("message", "you cannot sign out!");
        }
        return  map;
    }

    /**
     * 用户签到、签退等操作，更新预约状态
     */
    @Override
    public ResponseEntity<Map<String, Object>> updateBookingStateSelf(int id, int state, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        BookingDo booking = bookingDao.getBookingById(id);
        // 需要本人操作
        if(booking == null || !booking.getStuNum().endsWith(stuNum)) {
            map.put("message", "you don't book this seat!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);

        map = checkState(booking, id, hour, minute, state);

        if(!map.isEmpty()) return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

        // 抢到的座位签到后释放旧座位
        if(state == 1 && booking.getState() == -1) releaseSeat();

        boolean result = bookingDao.updateBookingState(id, state, "");
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateBookingState(int id, int state, String detail) {
        Map<String, Object> map = new HashMap<>();
        if(id <= 0 || state < -1 || state > 4) {
            map.put("message", "incomplete or incorrect parameters!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        boolean result = bookingDao.updateBookingState(id, state, detail);
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    public int getHour(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 检查预约的参数是否有效
     */
    public Map<String, Object> checkValid(BookingDo booking, SeatDo seat) {
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(booking == null || booking.getStartTime() == null) {
            map.put("message", "incomplete parameters!");
        } else if(booking.getSeatId() <= 0 || booking.getBookingPeriod() <= 0 || getHour(booking.getStartTime()) < calendar.get(Calendar.HOUR_OF_DAY)) {
            map.put("message", "invalid parameters!");
        } else if(booking.getBookingPeriod() > 4) {
            map.put("message", "booking period cannot be larger than 4h!");
        } else {
            // 判断要预定的座位是否存在
            if(ObjectUtils.isEmpty(seat)) {
                map.put("message", "this seat is not exist!");
            }
        }
        return map;
    }

    /**
     * 预约座位
     */
    @Override
    public ResponseEntity<Map<String, Object>> bookingSeat(BookingDo booking, HttpServletRequest request, HttpServletResponse response) {
        SeatDo seat = seatDao.getSeatById(booking.getSeatId());

        Map<String, Object> map = checkValid(booking, seat);
        if(!map.isEmpty()) {
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        StudyRoomDO studyRoom = studyRoomDao.getStudyRoomById(seat.getStudyRoomId());
        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        Timestamp startTime = booking.getStartTime();
        int startHour = getHour(startTime), endHour = startHour + booking.getBookingPeriod();
        // 判断预定的时间是否在自习室开放时间内
        if(startHour < studyRoom.getStartTime() || endHour > studyRoom.getEndTime()) {
            map.put("message", "booking period is not in the open time!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        // 检查该时间段是否有其他人预约
        List<BookingDo> bookings = bookingDao.getBookingListBySeatId(booking.getSeatId());
        List<BookingDo> filtered = bookings.stream().filter(bookingDo -> {
            if(bookingDo.getState() >= 2) return false;
            int start = getHour(bookingDo.getStartTime()), end = start + bookingDo.getBookingPeriod();
            return startHour >= start && startHour < end || endHour > start && endHour <= end;
        }).collect(Collectors.toList());
        if(filtered.size() > 0) {
            map.put("message", "booking time conflict exists!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // 判断是否现在是否占有其他座位，根据结果判断正常抢座还是占座
        List<BookingDo> bookingDos = bookingDao.getBookingListByStuNum(stuNum);
        List<BookingDo> filtered2 = bookingDos.stream().filter(bookingDo -> bookingDo.getState() == 0 || bookingDo.getState() == -1).collect(Collectors.toList());
        // 有座位未签到不能再次预约
        if(filtered2.size() > 0) {
            map.put("message", "you've not signed in your current seat!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // 现在有座位，并且已签到，那么可以抢座
        List<BookingDo> filtered3 = bookingDos.stream().filter(bookingDo -> bookingDo.getState() == 1).collect(Collectors.toList());
        boolean result;
        booking.setStuNum(stuNum);
        if(filtered3.size() > 0) {
            result = bookingDao.grabSeat(booking);
        } else {
            result = bookingDao.bookingSeat(booking);
        }

        if(result) {
            map.put("id", booking.getId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else{
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean deleteBooking(int id) {
        return bookingDao.deleteBooking(id);
    }
}
