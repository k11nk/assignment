package org.example.assignment.repository;

import org.example.assignment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findTop10ByAddressContaining(String area);

    List<String> findAllContacts();
}
