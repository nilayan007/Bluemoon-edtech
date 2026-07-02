package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.EnrollmentResponseDTO;
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

    //  Get all pending requests
    @GetMapping("/pending")
    public ResponseEntity<List<EnrollmentResponseDTO>> getPendingRequests() {
        return ResponseEntity.ok(enrollmentService.getPendingEnrollments());
    }

    //  Approve enrollment
    @PutMapping("/{enrollmentId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long enrollmentId) {

        enrollmentService.approveEnrollment(enrollmentId);

        return ResponseEntity.ok("Enrollment approved");
    }

    //  Reject enrollment
    @PutMapping("/{enrollmentId}/reject")
    public ResponseEntity<?> reject(@PathVariable Long enrollmentId) {

        enrollmentService.rejectEnrollment(enrollmentId);

        return ResponseEntity.ok("Enrollment rejected");
    }
}
