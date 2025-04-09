package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.DocumentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {
    List<DocumentRequest> findByStudent_PrnNo(String prnNo); // Find requests by PRN

    @Query("SELECT d FROM DocumentRequest d WHERE d.student.prnNo = :prnNo ORDER BY d.id DESC")
    List<DocumentRequest> findHistoryForStudent(@Param("prnNo") String prnNo);

}
