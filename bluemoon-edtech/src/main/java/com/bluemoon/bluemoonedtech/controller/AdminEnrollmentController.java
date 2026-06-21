package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.entity.Enrollment;
import com.bluemoon.bluemoonedtech.enums.EnrollmentStatus;
import com.bluemoon.bluemoonedtech.repository.EnrollmentRepository;
import com.bluemoon.bluemoonedtech.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/enrollments")
@RequiredArgsConstructor
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;

    //  Get all pending requests
    @GetMapping("/pending")
    public ResponseEntity<List<Enrollment>> getPendingRequests() {
        return ResponseEntity.ok(
                enrollmentRepository.findByStatus(EnrollmentStatus.PENDING)
        );
    }

    //  Approve enrollment
    @PutMapping("/{enrollmentId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long enrollmentId) {

        enrollmentService.approveEnrollment(enrollmentId);

        return ResponseEntity.ok("Enrollment approved");
    }
}