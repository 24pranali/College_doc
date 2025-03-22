package com.example.demo_pranali.service;

import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.Model.DocumentRequest;
import com.example.demo_pranali.repository.CreateAccountRepository;
import com.example.demo_pranali.repository.DocumentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentRequestService {

    @Autowired
    private DocumentRequestRepository documentRequestRepository;

    @Autowired
    private CreateAccountRepository createAccountRepository;

    // Create a new document request
    public DocumentRequest createRequest(String prnNo, String documentType, String reason,int status, String document) {
        Optional<CreateAccount> studentOpt = createAccountRepository.findByPrnNo(prnNo);

        if (studentOpt.isPresent()) {
            DocumentRequest request = new DocumentRequest(studentOpt.get(), documentType, reason, status, document);
            return documentRequestRepository.save(request);
        } else {
            throw new RuntimeException("Student with PRN " + prnNo + " not found!");
        }
    }

    public List<DocumentRequest> getAllRequests() {
        return documentRequestRepository.findAll();
    }


    // Get all requests for a student by PRN for status
    public List<DocumentRequest> getRequestsByPrn(String prnNo) {
        return documentRequestRepository.findByStudent_PrnNo(prnNo);
    }

    public DocumentRequest updateRequestStatusById(Long id, int newStatus) {
        DocumentRequest request = documentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(newStatus);
        return documentRequestRepository.save(request);
    }
}
















