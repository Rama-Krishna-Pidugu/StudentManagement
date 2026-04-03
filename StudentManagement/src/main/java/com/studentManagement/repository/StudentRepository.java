package com.studentManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentManagement.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByRollNo(String rollNo);

    Optional<Student> findByUserId(Long userId);
}
