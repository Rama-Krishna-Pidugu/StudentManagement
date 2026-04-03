package com.studentManagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentManagement.dto.Dtos.ApiResponse;
import com.studentManagement.dto.Dtos.AttendanceResponse;
import com.studentManagement.dto.Dtos.MarkResponse;
import com.studentManagement.dto.Dtos.StudentProfileResponse;
import com.studentManagement.model.User;
import com.studentManagement.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully.", studentService.getProfile(user)));
    }

    @GetMapping("/marks")
    public ResponseEntity<ApiResponse<List<MarkResponse>>> getMarks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer semester) {
        return ResponseEntity.ok(ApiResponse.success("Marks fetched successfully.", studentService.getMarks(user, semester)));
    }

    @PostMapping("/attendance")
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Attendance marked successfully.", studentService.markAttendance(user)));
    }

    @GetMapping("/attendance")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Attendance history fetched successfully.", studentService.getAttendanceHistory(user)));
    }
}
