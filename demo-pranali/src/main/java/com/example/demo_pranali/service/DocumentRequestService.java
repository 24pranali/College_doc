package com.example.demo_pranali.service;

import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.Model.DocumentRequest;
import com.example.demo_pranali.repository.CreateAccountRepository;
import com.example.demo_pranali.repository.DocumentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.util.List;
import java.util.Optional;

@Service
public class DocumentRequestService {

    @Autowired
    private DocumentRequestRepository documentRequestRepository;

    @Autowired
    private CreateAccountRepository createAccountRepository;

//    // Create a new document request
//    public DocumentRequest createRequest(String prnNo, String documentType, String reason,int status, String document) {
//        Optional<CreateAccount> studentOpt = createAccountRepository.findByPrnNo(prnNo);
//
//        if (studentOpt.isPresent()) {
//            DocumentRequest request = new DocumentRequest(studentOpt.get(), documentType, reason, status, document);
//            return documentRequestRepository.save(request);
//        } else {
//            throw new RuntimeException("Student with PRN " + prnNo + " not found!");
//        }
//    }

    // ✅ Create a new document request (with optional file)
    public DocumentRequest createRequest(String prnNo, String documentType, String reason, int status, MultipartFile file) throws IOException {
        Optional<CreateAccount> studentOpt = createAccountRepository.findByPrnNo(prnNo);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student with PRN " + prnNo + " not found");
        }
        CreateAccount student = studentOpt.get();

        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setStudent(student);
        documentRequest.setDocumentType(documentType);
        documentRequest.setReason(reason);
        documentRequest.setStatus(status);

        if (file != null) {
            documentRequest.setDocumentFile(file);
        }

        return documentRequestRepository.save(documentRequest);
    }

    // ✅ Upload a document separately
    public boolean uploadDocument(Long id, MultipartFile file) throws IOException {
        Optional<DocumentRequest> documentRequestOpt = documentRequestRepository.findById(id);
        if (documentRequestOpt.isPresent()) {
            DocumentRequest documentRequest = documentRequestOpt.get();
            documentRequest.setDocumentFile(file);
            documentRequestRepository.save(documentRequest);
            return true;
        } else {
            return false; // Request not found
        }
    }

    // ✅ Retrieve document file data by request ID
    public byte[] getDocumentFile(Long id) {
        Optional<DocumentRequest> requestOpt = documentRequestRepository.findById(id);
        return requestOpt.map(DocumentRequest::getDocumentData).orElse(null);
    }



    public List<DocumentRequest> getAllRequests() {
        return documentRequestRepository.findAll();
    }


    // Get all requests for a student by PRN for status
    public List<DocumentRequest> getRequestsByPrn(String prnNo) {
        return documentRequestRepository.findByStudent_PrnNo(prnNo);
    }

    // ✅ Approve/Reject a request by ID
    public DocumentRequest updateRequestStatusById(Long id, int newStatus) {
        DocumentRequest request = documentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(newStatus);
        return documentRequestRepository.save(request);
    }




}
















