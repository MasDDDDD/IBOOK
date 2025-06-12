package com.huawei.ibookstudy.service.impl;

import com.huawei.ibookstudy.dao.BookingDao;
import com.huawei.ibookstudy.model.BookingDo;
import com.huawei.ibookstudy.service.AutoUpdateService;
import com.huawei.ibookstudy.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AutoUpdateServiceImpl implements AutoUpdateService {
    @Autowired
    BookingDao bookingDao;
    @Autowired
    BookingService bookingService;

    public int getHour(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public void scanBookingList() {
        // 扫描预约表，更新
        List<BookingDo> bookingList = bookingDao.getAvailableBookingList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);

        for (BookingDo booking : bookingList) {
            int startTime = getHour(booking.getStartTime());
            boolean isIllegal_1 = hour > startTime || hour == startTime && minute > 15;
            boolean isIllegal_2 = hour > startTime || hour == startTime && minute > 5;
            // 待签到
            if (booking.getState() == 0 && isIllegal_1) {
                // 自动记录违约
                bookingService.updateBookingState(booking.getId(), 3, "not sign in on time");
            }
            else if (booking.getState() == 1 && hour >= startTime + booking.getBookingPeriod()) {
                // 自动释放座位
                bookingService.updateBookingState(booking.getId(), 4, "");
            }
            else if (booking.getState() == -1 && isIllegal_2) {
                // 抢座待签到
                bookingService.updateBookingState(booking.getId(), 3, "not sign in on time");
            }
        }
    }
}
