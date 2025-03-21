package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.DocumentRequest;
import com.example.demo_pranali.service.DocumentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-requests")
@CrossOrigin(origins = "http://localhost:5173") // Adjust for frontend
public class DocumentRequestController {

    @Autowired
    private DocumentRequestService documentRequestService;

    // Create a new document request
    @PostMapping("/create")
    public DocumentRequest createRequest(
            @RequestParam String prnNo,
            @RequestParam String documentType,
            @RequestParam String reason,
            @RequestParam(required = false) Boolean status, // Can be null
            @RequestParam String document) {

        if (status == null) {
            status = false; // Default to false (Pending)
        }

        return documentRequestService.createRequest(prnNo, documentType, reason, status, document);
    }

    // Get all document requests
    @GetMapping
    public List<DocumentRequest> getAllRequests() {
        return documentRequestService.getAllRequests();
    }

    // Get all requests for a specific PRN
    @GetMapping("/{prnNo}")
    public List<DocumentRequest> getRequestsByPrn(@PathVariable String prnNo) {
        return documentRequestService.getRequestsByPrn(prnNo);
    }

    @PutMapping("/{id}/approve")
    public DocumentRequest approveRequest(@PathVariable Long id) {
        return documentRequestService.updateRequestStatusById(id, true);  // true = Approved
    }

    // Reject a specific document request by ID
    @PutMapping("/{id}/reject")
    public DocumentRequest rejectRequest(@PathVariable Long id) {
        return documentRequestService.updateRequestStatusById(id, false);  // false = Rejected
    }

}

