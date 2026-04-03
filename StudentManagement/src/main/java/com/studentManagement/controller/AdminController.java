package com.studentManagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentManagement.dto.Dtos.ApiResponse;
import com.studentManagement.dto.Dtos.AssignStudentRequest;
import com.studentManagement.dto.Dtos.CreateDepartmentRequest;
import com.studentManagement.dto.Dtos.CreateStudentRequest;
import com.studentManagement.dto.Dtos.DepartmentResponse;
import com.studentManagement.dto.Dtos.StudentSummaryResponse;
import com.studentManagement.dto.Dtos.UploadMarksRequest;
import com.studentManagement.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@Validated
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/students")
    public ResponseEntity<ApiResponse<StudentSummaryResponse>> createStudent(
            @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Student created successfully.", adminService.createStudent(request)));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentSummaryResponse>>> getStudents() {
        return ResponseEntity.ok(ApiResponse.success("Students fetched successfully.", adminService.getStudents()));
    }

    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Department created successfully.", adminService.createDepartment(request)));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartments() {
        return ResponseEntity.ok(ApiResponse.success("Departments fetched successfully.", adminService.getDepartments()));
    }

    @PutMapping("/students/assign")
    public ResponseEntity<ApiResponse<StudentSummaryResponse>> assignStudent(
            @Valid @RequestBody AssignStudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Student assigned successfully.",
                adminService.assignDepartmentAndSemester(request)));
    }

    @PostMapping("/marks")
    public ResponseEntity<ApiResponse<Void>> uploadMarks(@Valid @RequestBody UploadMarksRequest request) {
        adminService.uploadMarks(request);
        return ResponseEntity.ok(ApiResponse.success("Marks uploaded successfully.", null));
    }
}
