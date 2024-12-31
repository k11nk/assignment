package org.example.assignment.repository;

import org.example.assignment.model.StudentCR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCRRepository extends JpaRepository<StudentCR,Long> {

    List<StudentCR> findByChangedField(String address);
}
