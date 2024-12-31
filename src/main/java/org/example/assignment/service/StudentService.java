package org.example.assignment.service;

import org.example.assignment.model.Student;
import org.example.assignment.model.StudentCR;
import org.example.assignment.repository.StudentCRRepository;
import org.example.assignment.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCRRepository studentCRRepository;

    //To filter the request received for specific areas
    public List<Student> fetchStudentsByAddress(String area) {
        List<StudentCR> addressChanges = studentCRRepository.findByChangedField("address");
        return studentRepository.findTop10ByAddressContaining(area);
    }

    // Group change requests by age and fetch up to 10 students.
    public List<Student> fetchStudentsByAge(int age) {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .filter(student -> Period.between(student.getBirthDate(), LocalDate.now()).getYears() == age)
                .limit(10)
                .collect(Collectors.toList());
    }

    public boolean verifyDuplicateContacts() {
        List<String> contacts = studentRepository.findAllContacts();
        Set<String> uniqueContacts = new HashSet<>(contacts);
        return contacts.size() == uniqueContacts.size();
    }


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setBirthDate(updatedStudent.getBirthDate());
            student.setClassName(updatedStudent.getClassName());
            student.setGrade(updatedStudent.getGrade());
            student.setAddress(updatedStudent.getAddress());
            student.setContact(updatedStudent.getContact());
            student.setAlternateContact(updatedStudent.getAlternateContact());
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
