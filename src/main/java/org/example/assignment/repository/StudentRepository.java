package org.example.assignment.repository;

import org.example.assignment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findTop10ByAddressContaining(String area);

    @Query("SELECT s.contact FROM Student s")
    List<String> findAllContacts();
}
