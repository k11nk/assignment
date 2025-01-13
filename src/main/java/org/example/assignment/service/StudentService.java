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

    // To filter the request received for specific areas
    public List<Student> fetchStudentsByAddress(String area) {
        return studentRepository.findTop10ByAddressContaining(area);
    }

    // Group change requests by age and fetch up to 10 students
    public List<Student> fetchStudentsByAge(int age) {
        LocalDate now = LocalDate.now();
        return studentRepository.findAll().stream()
                .filter(student -> student.getBirthDate() != null &&
                        Period.between(student.getBirthDate(), now).getYears() == age)
                .limit(10)
                .collect(Collectors.toList());
    }

    // Verify if contact details are not duplicated across students
    public boolean verifyDuplicateContacts() {
        List<String> contacts = studentRepository.findAllContacts();
        Set<String> uniqueContacts = new HashSet<>(contacts);
        return contacts.size() == uniqueContacts.size();
    }

    // Fetch all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add a new student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

//    // Update an existing student
//    public Student updateStudent(Long id, Student updatedStudent) {
//        return studentRepository.findById(id).map(student -> {
//            student.setName(updatedStudent.getName());
//            student.setBirthDate(updatedStudent.getBirthDate());
//            student.setClassName(updatedStudent.getClassName());
//            student.setGrade(updatedStudent.getGrade());
//            student.setAddress(updatedStudent.getAddress());
//            student.setContact(updatedStudent.getContact());
//            student.setAlternateContact(updatedStudent.getAlternateContact());
//            return studentRepository.save(student);
//        }).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
//    }

    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(existingStudent -> {
            // Log changes in the STUDENT_CR table
            if (!existingStudent.getName().equals(updatedStudent.getName())) {
                logChange(id, "name", existingStudent.getName(), updatedStudent.getName());
                existingStudent.setName(updatedStudent.getName());
            }
            if (!existingStudent.getBirthDate().equals(updatedStudent.getBirthDate())) {
                logChange(id, "birthDate", existingStudent.getBirthDate().toString(), updatedStudent.getBirthDate().toString());
                existingStudent.setBirthDate(updatedStudent.getBirthDate());
            }
            if (!existingStudent.getClassName().equals(updatedStudent.getClassName())) {
                logChange(id, "className", existingStudent.getClassName(), updatedStudent.getClassName());
                existingStudent.setClassName(updatedStudent.getClassName());
            }
            if (!existingStudent.getGrade().equals(updatedStudent.getGrade())) {
                logChange(id, "grade", existingStudent.getGrade(), updatedStudent.getGrade());
                existingStudent.setGrade(updatedStudent.getGrade());
            }
            if (!existingStudent.getAddress().equals(updatedStudent.getAddress())) {
                logChange(id, "address", existingStudent.getAddress(), updatedStudent.getAddress());
                existingStudent.setAddress(updatedStudent.getAddress());
            }
            if (!existingStudent.getContact().equals(updatedStudent.getContact())) {
                logChange(id, "contact", existingStudent.getContact(), updatedStudent.getContact());
                existingStudent.setContact(updatedStudent.getContact());
            }
            if (!existingStudent.getAlternateContact().equals(updatedStudent.getAlternateContact())) {
                logChange(id, "alternateContact", existingStudent.getAlternateContact(), updatedStudent.getAlternateContact());
                existingStudent.setAlternateContact(updatedStudent.getAlternateContact());
            }

            // Save the updated student
            return studentRepository.save(existingStudent);
        }).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    // Helper method to log changes in STUDENT_CR table
//    private void logChange(Long studentId, String fieldName, String beforeValue, String afterValue) {
//        StudentCR studentCR = new StudentCR();
//        studentCR.setId(studentId);
//        studentCR.setChangedField(fieldName);
//        studentCR.setBeforeChangeValue(beforeValue);
//        studentCR.setChangedValue(afterValue);
//        studentCR.setChangedBy("Admin");
//        studentCR.setChangedDate(LocalDate.now().atStartOfDay());
//        studentCRRepository.save(studentCR);
//    }
    private void logChange(Long studentId, String fieldName, String beforeValue, String afterValue) {
        try {
            StudentCR studentCR = new StudentCR();
            studentCR.setId(studentId); // Ensure you're setting the correct student ID
            studentCR.setChangedField(fieldName);
            studentCR.setBeforeChangeValue(beforeValue);
            studentCR.setChangedValue(afterValue);
            studentCR.setChangedBy("Admin"); // Replace with actual user info if needed
            studentCR.setChangedDate(LocalDate.now().atStartOfDay()); // Make sure this is correct

            // Debugging logs to help trace values
            System.out.println("Logging change for student ID: " + studentId);
            System.out.println("Field: " + fieldName);
            System.out.println("Before value: " + beforeValue);
            System.out.println("After value: " + afterValue);
            System.out.println("Changed by: Admin");
            System.out.println("Changed date: " + LocalDate.now().atStartOfDay());

            // Save the log in the database
            studentCRRepository.save(studentCR);
            System.out.println("Change logged successfully");
        } catch (Exception e) {
            // Log the error if something goes wrong
            System.err.println("Error logging change for student ID: " + studentId);
            e.printStackTrace();
        }
    }


    // Delete a student
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
