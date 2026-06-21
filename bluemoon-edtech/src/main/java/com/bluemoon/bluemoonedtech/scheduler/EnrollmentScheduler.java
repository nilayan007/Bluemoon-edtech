package com.bluemoon.bluemoonedtech.scheduler;

import com.bluemoon.bluemoonedtech.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentScheduler {

    private final EnrollmentService enrollmentService;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireEnrollmentsJob() {
        enrollmentService.expireEnrollments();
    }
}