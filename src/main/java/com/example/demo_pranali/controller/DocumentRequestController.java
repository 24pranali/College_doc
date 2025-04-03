package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.DocumentRequest;
import com.example.demo_pranali.repository.DocumentRequestRepository;
import com.example.demo_pranali.service.DocumentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/document-requests")
@CrossOrigin(origins = "http://localhost:5173") // Adjust for frontend
public class DocumentRequestController {

    @Autowired
    private DocumentRequestService documentRequestService;

    @Autowired
    private DocumentRequestRepository documentRequestRepository;


    //  1. Create Document Request (with optional file upload)
    @PostMapping("/create")
    public ResponseEntity<DocumentRequest> createRequest(
            @RequestParam String prnNo,
            @RequestParam String documentType,
            @RequestParam String reason,
            @RequestParam(required = false) Integer status,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        if (status == null) {
            status = 1; // Default to Pending
        }

        try {
            DocumentRequest documentRequest = documentRequestService.createRequest(prnNo, documentType, reason, status, file);
            return ResponseEntity.ok(documentRequest);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //  2. Upload Document (Admin uploads file separately)
    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            boolean uploaded = documentRequestService.uploadDocument(id, file);
            if (uploaded) {
                return ResponseEntity.ok("File uploaded successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document request not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }

    // 3. Download Document (PDF) student
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] file = documentRequestService.getDocumentFile(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=Bonafide_Certificate.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(file);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getDocumentName())
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(file);


    }


    //  3. Get all document requests
    @GetMapping
    public List<DocumentRequest> getAllRequests()
    {
        return documentRequestService.getAllRequests();
    }


    //  4. Get all requests for a specific PRN
    @GetMapping("/{prnNo}")
    public List<DocumentRequest> getRequestsByPrn(@PathVariable String prnNo)
    {
        return documentRequestService.getRequestsByPrn(prnNo);
    }

    //  5. Approve Request
    @PutMapping("/{id}/approve")
    public DocumentRequest approveRequest(@PathVariable Long id)
    {
        return documentRequestService.updateRequestStatusById(id, 2); // 2 = Approved
    }

    //  6. Reject Request
    @PutMapping("/{id}/reject")
    public DocumentRequest rejectRequest(@PathVariable Long id)
    {
        return documentRequestService.updateRequestStatusById(id, 3); // 3 = Rejected
    }

    // 7. Generate & Upload PDF (admin)
    @PostMapping("/{id}/generate-and-upload")
    public ResponseEntity<String> generateAndUploadPDF(@PathVariable Long id) {
        boolean success = documentRequestService.generateAndUploadPDF(id);
        if (success) {
            return ResponseEntity.ok("PDF generated and uploaded.");
        } else {
            return ResponseEntity.badRequest().body("PDF generation failed.");
        }
    }

    // ✅ Upload verification document (Student)
    @PostMapping("/{id}/upload-verification")
    public ResponseEntity<String> uploadVerificationDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            boolean uploaded = documentRequestService.uploadVerificationDocument(id, file);
            if (uploaded) {
                return ResponseEntity.ok("Verification document uploaded successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document request not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading verification document.");
        }
    }

    @GetMapping("/{id}/download-verification")
    public ResponseEntity<byte[]> downloadVerificationDocument(@PathVariable Long id) {
        Optional<DocumentRequest> requestOpt = documentRequestRepository.findById(id);
        if (requestOpt.isEmpty() || requestOpt.get().getVerificationDocument() == null) {
            return ResponseEntity.notFound().build();
        }

        DocumentRequest request = requestOpt.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + request.getVerificationDocumentName())
                .contentType(MediaType.parseMediaType(request.getVerificationDocumentType()))
                .body(request.getVerificationDocument());
    }








}

