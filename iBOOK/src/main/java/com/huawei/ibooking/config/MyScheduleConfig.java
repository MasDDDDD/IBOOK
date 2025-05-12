package com.huawei.ibooking.config;

import com.huawei.ibooking.service.AutoUpdateService;
import com.huawei.ibooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class MyScheduleConfig {

    @Autowired
    private AutoUpdateService autoUpdateService;

    @Scheduled(cron = "0/5 * * * * ?")
    private void myTasks() {
//        System.out.println("执行定时任务 " + LocalDateTime.now());
        autoUpdateService.scanBookingList();
    }
}
