package com.studentManagement.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentManagement.dto.Dtos.AssignStudentRequest;
import com.studentManagement.dto.Dtos.CreateDepartmentRequest;
import com.studentManagement.dto.Dtos.CreateStudentRequest;
import com.studentManagement.dto.Dtos.DepartmentResponse;
import com.studentManagement.dto.Dtos.StudentSummaryResponse;
import com.studentManagement.dto.Dtos.UploadMarksRequest;
import com.studentManagement.model.Department;
import com.studentManagement.model.Mark;
import com.studentManagement.model.Role;
import com.studentManagement.model.Student;
import com.studentManagement.model.User;
import com.studentManagement.repository.DepartmentRepository;
import com.studentManagement.repository.MarkRepository;
import com.studentManagement.repository.StudentRepository;
import com.studentManagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final MarkRepository markRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StudentSummaryResponse createStudent(CreateStudentRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (studentRepository.existsByRollNo(request.rollNo())) {
            throw new IllegalArgumentException("Roll number already exists.");
        }

        User user = userRepository.save(User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STUDENT)
                .build());

        Student student = studentRepository.save(Student.builder()
                .rollNo(request.rollNo())
                .semester(1)
                .user(user)
                .build());

        return mapStudent(student);
    }

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Department already exists.");
        }

        Department department = departmentRepository.save(Department.builder()
                .name(request.name())
                .build());

        return new DepartmentResponse(department.getId(), department.getName());
    }

    @Transactional
    public StudentSummaryResponse assignDepartmentAndSemester(AssignStudentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));

        student.setDepartment(department);
        student.setSemester(request.semester());

        return mapStudent(studentRepository.save(student));
    }

    @Transactional
    public void uploadMarks(UploadMarksRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));

        markRepository.save(Mark.builder()
                .student(student)
                .subject(request.subject())
                .marks(request.marks())
                .semester(request.semester())
                .build());
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryResponse> getStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapStudent)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> new DepartmentResponse(department.getId(), department.getName()))
                .toList();
    }

    private StudentSummaryResponse mapStudent(Student student) {
        return new StudentSummaryResponse(
                student.getId(),
                student.getUser().getName(),
                student.getUser().getEmail(),
                student.getRollNo(),
                student.getDepartment() != null ? student.getDepartment().getName() : null,
                student.getSemester());
    }
}
