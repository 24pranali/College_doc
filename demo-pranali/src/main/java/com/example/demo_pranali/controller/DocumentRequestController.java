package com.example.demo_pranali.controller;

import com.example.demo_pranali.Model.CreateAccount;
import com.example.demo_pranali.Model.DocumentRequest;
import com.example.demo_pranali.repository.DocumentRequestRepository;
import com.example.demo_pranali.service.DocumentRequestService;
import com.example.demo_pranali.service.EmailService;
import com.example.demo_pranali.service.PdfGeneratorService;
import com.example.demo_pranali.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/document-requests")
@CrossOrigin(origins = "http://localhost:5173") // Adjust for frontend
public class DocumentRequestController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private DocumentRequestService documentRequestService;

    @Autowired
    private DocumentRequestRepository documentRequestRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private TeacherService teacherService;
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


//    //  2. Upload Document (Admin uploads file separately)
//    @PostMapping("/upload/{id}")
//    public ResponseEntity<String> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
//        try {
//            boolean uploaded = documentRequestService.uploadDocument(id, file);
//            if (uploaded) {
//                return ResponseEntity.ok("File uploaded successfully!!!");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document request not found.");
//            }
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
//        }
//    }


    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        DocumentRequest request = documentRequestService.getRequestById(id);  // ✅ Get full request object
        if (request == null || request.getDocumentFile() == null) {
            return ResponseEntity.notFound().build();
        }

        // Mark as downloaded and save the update
        request.setDownloaded(true);
        documentRequestService.saveRequest(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.getDocumentName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(request.getDocumentFile());
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

//    //  5. Approve Request
//    @PutMapping("/{id}/approve")
//    public DocumentRequest approveRequest(@PathVariable Long id)
//    {
//        return documentRequestService.updateRequestStatusById(id, 2); // 2 = Approved
//    }
@PutMapping("/{id}/approve")
public ResponseEntity<String> approveRequest(@PathVariable Long id) {
    try {
        documentRequestService.updateRequestStatusById(id, 2);  // ✅ This triggers email sending
        return ResponseEntity.ok("✅ Request approved and email sent.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error approving request: " + e.getMessage());
    }
}


    //  6. Reject Request
//    @PutMapping("/{id}/reject")
//    public ResponseEntity<String> rejectRequest(@PathVariable Long id) {
//        try {
//            documentRequestService.updateRequestStatusById(id, 3);// Will send rejection email
//
//            return ResponseEntity.ok("❌ Request rejected and email sent.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error rejecting request: " + e.getMessage());
//        }
//    }
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            String reason = payload.get("reason");
            documentRequestService.updateRequestStatusById(id, 3); // Will send rejection email
            documentRequestService.updateRejectionReason(id, reason);
            return ResponseEntity.ok("❌ Request rejected and email sent.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error rejecting request: " + e.getMessage());
        }
    }


    // 7. Generate & Upload PDF (admin)
//    @PostMapping("/{id}/generate-and-upload")
//    public ResponseEntity<String> generateAndUploadPDF(@PathVariable Long id) {
//        boolean success = documentRequestService.generateAndUploadPDF(id);
//        if (success) {
//            return ResponseEntity.ok("PDF generated and uploaded.");
//        } else {
//            return ResponseEntity.badRequest().body("PDF generation failed.");
//        }
//    }

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

    // 8. Get statistics summary for admin dashboard
    @GetMapping("/stats")
    public Map<String, Integer> getRequestStats() {
        List<DocumentRequest> all = documentRequestService.getAllRequests();

        int total = all.size();
        int pending = 0, approved = 0, rejected = 0;

        Set<String> uniquePrns = new HashSet<>();

        for (DocumentRequest dr : all) {
            uniquePrns.add(dr.getStudent().getPrnNo());

            if (dr.getStatus() == 1) pending++;
            else if (dr.getStatus() == 2) approved++;
            else if (dr.getStatus() == 3) rejected++;
        }

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalRequests", total);
        stats.put("pending", pending);
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("totalStudents", uniquePrns.size());

        return stats;
    }


    @GetMapping("/{id}/generate")
    public ResponseEntity<String> generateDocument(@PathVariable Long id) {
        try {
            DocumentRequest request = documentRequestService.getRequestById(id);
            if (request == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document request not found.");
            }

            byte[] pdfBytes = documentRequestService.generatePdfForRequest(request);
            request.setGeneratedPdf(pdfBytes);
            request.setDocumentName(request.getDocumentType().replace(" ", "_") + "_generated.pdf");
            request.setDocumentTypeStored("application/pdf");
          // s request.setDocumentType(request.getDocumentType());
            request.setPdfGenerated(true);
            // Save it to DB
            documentRequestService.saveRequest(request);

            return ResponseEntity.ok("✅ PDF generated successfully for request ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF: " + e.getMessage());
        }
    }


    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> viewUploadedPdf(@PathVariable Long id) {
        DocumentRequest request = documentRequestService.getRequestById(id);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileToDisplay = (request.getGeneratedPdf() != null)
                ? request.getGeneratedPdf()
                : request.getDocumentFile();

        if (fileToDisplay == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename(request.getDocumentName() != null ? request.getDocumentName() : "Document.pdf")
                .build());

        return new ResponseEntity<>(fileToDisplay, headers, HttpStatus.OK);
    }



    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadPDF(@PathVariable Long id) {
        boolean uploaded = documentRequestService.uploadFinalWithoutStatusChange(id); // new method that doesn't change status
        if (uploaded) {
            return ResponseEntity.ok("📤 PDF uploaded successfully.");
        } else {
            return ResponseEntity.badRequest().body("❌ Upload failed or document not generated.");
        }
    }


    @GetMapping("/{id}/status-flags")
    public ResponseEntity<Map<String, Boolean>> getFlags(@PathVariable Long id) {
        DocumentRequest request = documentRequestService.getRequestById(id);
        if (request == null) return ResponseEntity.notFound().build();

        Map<String, Boolean> flags = new HashMap<>();
        flags.put("isPdfGenerated", request.getPdfGenerated());
        flags.put("isPdfUploaded", request.getPdfUploaded());
        flags.put("isFinalized", request.getFinalized());
        flags.put("hasVerificationDoc", request.getVerificationDocument() != null);

        return ResponseEntity.ok(flags);
    }

    @PutMapping("/{id}/finalize")
    public ResponseEntity<String> finalizeDocument(@PathVariable Long id) {
        DocumentRequest request = documentRequestService.getRequestById(id);

        if (request == null || request.getDocumentFile() == null || request.getStatus() != 2) {
            return ResponseEntity.badRequest().body("Request must be approved and document must be generated.");
        }

        request.setFinalized(true);  // Only set internal flag
        documentRequestService.saveRequest(request);
        return ResponseEntity.ok("📤 Document finalized internally (admin only).");
    }
    @PostMapping("/send-lor")
    public ResponseEntity<String> sendLorRequest(@RequestParam String studentName,
                                                 @RequestParam String teacherName,
                                                 @RequestParam MultipartFile resume) throws IOException {

        String toEmail = teacherService.getEmailByTeacherName(teacherName);
        if (toEmail == null) return ResponseEntity.badRequest().body("Invalid Teacher Selected");

        // Send mail with attachment
        emailService.sendLorRequestEmail(toEmail, studentName);


        return ResponseEntity.ok("LOR Request sent successfully.");
    }

    @GetMapping("/history/{prnNo}")
    public ResponseEntity<List<DocumentRequest>> getHistory(@PathVariable String prnNo) {
        List<DocumentRequest> history = documentRequestService.getHistoryByPrnNo(prnNo);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{id}/reapply")
    public ResponseEntity<String> reapplyDocumentRequest(@PathVariable Long id) {
        try {
            documentRequestService.reapplyRequest(id); // Call your service method
            return ResponseEntity.ok("Reapplied successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Reapply failed: " + e.getMessage());
        }
    }

    private String rejectionReason;
    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }















}

