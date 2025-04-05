package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.DocumentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {
    List<DocumentRequest> findByStudent_PrnNo(String prnNo); // Find requests by PRN

}
