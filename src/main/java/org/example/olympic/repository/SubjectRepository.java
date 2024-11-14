package org.example.olympic.repository;

import org.example.olympic.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
   List<Subject> findBySubjectNameContainingIgnoreCase(String subjectName);
}
