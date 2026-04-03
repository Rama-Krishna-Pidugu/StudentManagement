package com.studentManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentManagement.dto.Dtos.AttendanceResponse;
import com.studentManagement.dto.Dtos.MarkResponse;
import com.studentManagement.dto.Dtos.StudentProfileResponse;
import com.studentManagement.model.Attendance;
import com.studentManagement.model.Student;
import com.studentManagement.model.User;
import com.studentManagement.repository.AttendanceRepository;
import com.studentManagement.repository.MarkRepository;
import com.studentManagement.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public StudentProfileResponse getProfile(User user) {
        Student student = getStudentByUser(user);
        return new StudentProfileResponse(
                student.getId(),
                student.getUser().getName(),
                student.getUser().getEmail(),
                student.getRollNo(),
                student.getDepartment() != null ? student.getDepartment().getName() : null,
                student.getSemester());
    }

    @Transactional(readOnly = true)
    public List<MarkResponse> getMarks(User user, Integer semester) {
        Student student = getStudentByUser(user);
        if (semester == null) {
            return markRepository.findByStudentIdOrderBySemesterAscSubjectAsc(student.getId()).stream()
                    .map(mark -> new MarkResponse(mark.getId(), mark.getSubject(), mark.getMarks(), mark.getSemester()))
                    .toList();
        }
        return markRepository.findByStudentIdAndSemesterOrderBySubjectAsc(student.getId(), semester).stream()
                .map(mark -> new MarkResponse(mark.getId(), mark.getSubject(), mark.getMarks(), mark.getSemester()))
                .toList();
    }

    @Transactional
    public AttendanceResponse markAttendance(User user) {
        Student student = getStudentByUser(user);
        LocalDate today = LocalDate.now();

        if (attendanceRepository.existsByStudentIdAndAttendanceDate(student.getId(), today)) {
            throw new IllegalStateException("Attendance already marked for today.");
        }

        Attendance attendance = attendanceRepository.save(Attendance.builder()
                .student(student)
                .attendanceDate(today)
                .build());

        return new AttendanceResponse(attendance.getId(), attendance.getAttendanceDate());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceHistory(User user) {
        Student student = getStudentByUser(user);
        return attendanceRepository.findByStudentIdOrderByAttendanceDateDesc(student.getId()).stream()
                .map(item -> new AttendanceResponse(item.getId(), item.getAttendanceDate()))
                .toList();
    }

    private Student getStudentByUser(User user) {
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found."));
    }
}
