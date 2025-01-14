package org.example.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STUDENT_CR")
public class StudentCR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String changedField;
    private String changedValue;
    private String beforeChangeValue;
    private String changedBy;

    @Column(name = "changed_date", nullable = false)
    private LocalDateTime changedDate;

    // Remove or fix the custom setter for changedDate
    public void setChangedDate(LocalDateTime changedDate) {
        this.changedDate = changedDate;
    }

}
