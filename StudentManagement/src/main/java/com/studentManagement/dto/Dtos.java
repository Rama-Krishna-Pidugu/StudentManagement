package com.studentManagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.studentManagement.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

/**
 * Single-file DTO container to avoid many separate DTO files.
 * <p>
 * Usage example: {@code import com.studentManagement.dto.Dtos.MarkResponse;}
 */
public final class Dtos {
    private Dtos() {
        // utility class
    }

    @Builder
    public static record ApiResponse<T>(
            boolean success,
            String message,
            T data,
            LocalDateTime timestamp) {

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    public static record AuthResponse(
            Long userId,
            String name,
            String email,
            Role role) {
    }

    public static record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password) {
    }

    public static record MarkResponse(
            Long id,
            String subject,
            Integer marks,
            Integer semester) {
    }

    public static record AttendanceResponse(
            Long id,
            LocalDate attendanceDate) {
    }

    public static record StudentProfileResponse(
            Long id,
            String name,
            String email,
            String rollNo,
            String department,
            Integer semester) {
    }

    public static record DepartmentResponse(
            Long id,
            String name) {
    }

    public static record StudentSummaryResponse(
            Long id,
            String name,
            String email,
            String rollNo,
            String department,
            Integer semester) {
    }

    public static record UploadMarksRequest(
            @NotNull Long studentId,
            @NotBlank String subject,
            @NotNull @Min(0) @Max(100) Integer marks,
            @NotNull @Min(1) Integer semester) {
    }

    public static record AssignStudentRequest(
            @NotNull Long studentId,
            @NotNull Long departmentId,
            @NotNull @Min(1) Integer semester) {
    }

    public static record CreateDepartmentRequest(
            @NotBlank String name) {
    }

    public static record CreateStudentRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank String password,
            @NotBlank String rollNo) {
    }
}

