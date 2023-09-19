package com.prgrms.nabmart.domain.user.scheduler;

import com.prgrms.nabmart.domain.user.service.GradeService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S")
public class GradeScheduler {

    private final GradeService gradeService;

    @Async
    @Scheduled(cron = "0 0 5 1 * *")
    @SchedulerLock(name = "Update_User_Grade", lockAtLeastFor = "PT10S")
    public void updateUserGradeScheduler() {
        gradeService.updateUserGrade();
    }
}
