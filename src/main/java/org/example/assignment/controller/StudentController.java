package org.example.assignment.controller;

import org.example.assignment.model.Student;
import org.example.assignment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get students by address area
    @GetMapping("/by-address")
    public List<Student> getStudentsByAddress(@RequestParam String area) {
        return studentService.fetchStudentsByAddress(area);
    }

    // Get students by age
    @GetMapping("/by-age")
    public List<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.fetchStudentsByAge(age);
    }

    // Verify duplicate contacts
    @GetMapping("/verify-contacts")
    public boolean verifyDuplicateContacts() {
        return studentService.verifyDuplicateContacts();
    }

    // Add a new student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // Update student details
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentService.updateStudent(id, updatedStudent);
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
