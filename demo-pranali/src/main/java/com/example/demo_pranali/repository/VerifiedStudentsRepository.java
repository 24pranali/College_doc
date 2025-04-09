package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.VerifiedStudents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifiedStudentsRepository extends JpaRepository<VerifiedStudents, String> {
    Optional<VerifiedStudents> findByPrnNoAndOfficialEmail(String prnNo, String officialEmail);

}
