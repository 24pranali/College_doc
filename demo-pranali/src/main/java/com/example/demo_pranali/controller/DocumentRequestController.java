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

    // Endpoint to create a new document request
    @PostMapping("/create")
    public DocumentRequest createRequest(
            @RequestParam String prnNo,
            @RequestParam String documentType,
            @RequestParam String reason) {
        return documentRequestService.createRequest(prnNo, documentType, reason);
    }

    // Endpoint to get all requests for a student by PRN
    @GetMapping("/{prnNo}")
    public List<DocumentRequest> getRequestsByPrn(@PathVariable String prnNo) {
        return documentRequestService.getRequestsByPrn(prnNo);
    }
}
