package com.studentManagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentManagement.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    List<Attendance> findByStudentIdOrderByAttendanceDateDesc(Long studentId);
}
