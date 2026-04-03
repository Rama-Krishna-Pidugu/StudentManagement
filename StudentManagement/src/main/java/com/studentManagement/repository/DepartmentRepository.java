package com.studentManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentManagement.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByNameIgnoreCase(String name);
}
