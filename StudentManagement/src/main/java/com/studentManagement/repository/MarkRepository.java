package com.studentManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentManagement.model.Mark;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    List<Mark> findByStudentIdOrderBySemesterAscSubjectAsc(Long studentId);

    List<Mark> findByStudentIdAndSemesterOrderBySubjectAsc(Long studentId, Integer semester);
}
