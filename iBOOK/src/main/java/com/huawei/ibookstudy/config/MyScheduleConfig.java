package com.huawei.ibookstudy.config;

import com.huawei.ibookstudy.service.AutoUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class MyScheduleConfig {
//    private AutoUpdateService autoUpdateService;
//    @Autowired
//    public MyScheduleConfig(AutoUpdateService autoUpdateService) {
//        this.autoUpdateService = autoUpdateService;
//    }
//
//    @Scheduled(cron = "0/5 * * * * ?")
//    private void myTasks() {
//        autoUpdateService.scanBookingList();
//    }
}
