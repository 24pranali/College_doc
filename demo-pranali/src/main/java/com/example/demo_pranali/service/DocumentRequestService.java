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

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    // ✅ Create a new document request (with optional file)
    public DocumentRequest createRequest(String prnNo, String documentType, String reason, int status, MultipartFile file) throws IOException {
        CreateAccount student = createAccountRepository.findByPrnNo(prnNo)
                .orElseThrow(() -> new RuntimeException("Student with PRN " + prnNo + " not found"));

        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setStudent(student);
        documentRequest.setDocumentType(documentType);
        documentRequest.setReason(reason);
        documentRequest.setStatus(status);


        if (file != null && !file.isEmpty()) {
            documentRequest.setDocumentFile(file.getBytes());
            documentRequest.setDocumentName(file.getOriginalFilename());
            documentRequest.setDocumentTypeStored(file.getContentType()); // ✅ store content type separately
           // documentRequest.setDocumentType(file.getContentType());

        }

        return documentRequestRepository.save(documentRequest);
    }

    // ✅ Upload document separately
    public boolean uploadDocument(Long id, MultipartFile file) throws IOException {
        DocumentRequest request = getRequestById(id);
        if (request == null || file.isEmpty()) return false;

        request.setDocumentFile(file.getBytes());
        request.setDocumentName(file.getOriginalFilename());
        //request.setDocumentType(file.getContentType());
        request.setDocumentTypeStored(file.getContentType());


        saveRequest(request);
        return true;
    }

    // ✅ Get all requests
    public List<DocumentRequest> getAllRequests() {
        return documentRequestRepository.findAll();
    }

    // ✅ Get request by ID
    public DocumentRequest getRequestById(Long id) {
        return documentRequestRepository.findById(id).orElse(null);
    }

    // ✅ Save request
    public void saveRequest(DocumentRequest request) {
        documentRequestRepository.save(request);
    }

    // ✅ Get requests by PRN
    public List<DocumentRequest> getRequestsByPrn(String prnNo) {
        return documentRequestRepository.findByStudent_PrnNo(prnNo);
    }

    // ✅ Get document file (raw bytes)
    public byte[] getDocumentFile(Long id) {
        DocumentRequest request = getRequestById(id);
        return (request != null) ? request.getDocumentFile() : null;
    }

    // ✅ Update status (approve/reject)
    public DocumentRequest updateRequestStatusById(Long id, int newStatus) {
        DocumentRequest request = getRequestById(id);
        if (request == null) throw new RuntimeException("Request not found");

        request.setStatus(newStatus);
        return documentRequestRepository.save(request);
    }

    // ✅ Upload verification document
    public boolean uploadVerificationDocument(Long id, MultipartFile file) throws IOException {
        DocumentRequest request = getRequestById(id);
        if (request == null || file.isEmpty()) return false;

        // ✅ ADD CONTENT TYPE VALIDATION HERE
        String contentType = file.getContentType();
        if (!List.of("application/pdf", "image/jpeg", "image/png").contains(contentType)) {
            throw new IllegalArgumentException("Only PDF and image files (JPG, PNG) are allowed");
        }


        request.setVerificationDocument(file.getBytes());
        request.setVerificationDocumentName(file.getOriginalFilename());
        request.setVerificationDocumentType(file.getContentType());

        saveRequest(request);
        return true;
    }
    //  Generate and upload PDF after approval
    //final save after approval

//    public boolean generateAndUploadPDF(Long requestId) {
//        DocumentRequest request = getRequestById(requestId);
//        if (request == null || request.getStatus() != 2) return false;
//
//        byte[] pdfBytes = generatePdfForRequest(request); // ✅ Reuse main logic
//
//        request.setDocumentFile(pdfBytes);
//
//        String fileName = request.getDocumentType().substring(0, 1).toUpperCase() +
//                request.getDocumentType().substring(1).toLowerCase().replace(" ", "_") + ".pdf";
//
//        request.setDocumentName(fileName);
//        request.setDocumentTypeStored("application/pdf");
//
//        saveRequest(request);
//        return true;
//    }

    public boolean generateAndUploadPDF(Long requestId) {
        try {
            DocumentRequest request = getRequestById(requestId);
            if (request == null) {
                System.out.println("Request not found for ID: " + requestId);
                return false;
            }
            if (request.getStatus() != 2) {
                System.out.println("Request not approved. Status: " + request.getStatus());
                return false;
            }

            System.out.println("Generating PDF for type: " + request.getDocumentType());

            byte[] pdfBytes = generatePdfForRequest(request);
            if (pdfBytes == null || pdfBytes.length == 0) {
                System.out.println("Generated PDF is empty or null.");
                return false;
            }

            request.setDocumentFile(pdfBytes);

            String fileName = request.getDocumentType().substring(0, 1).toUpperCase() +
                    request.getDocumentType().substring(1).toLowerCase().replace(" ", "_") + ".pdf";

            request.setDocumentName(fileName);
            request.setDocumentTypeStored("application/pdf");

            saveRequest(request);
            System.out.println("PDF generated and saved successfully for requestId: " + requestId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error during PDF generation: " + e.getMessage());
            e.printStackTrace();  // This will help you see the full stack trace in console
            return false;
        }
    }



    // ✅ Upload generated PDF and mark approved
    public boolean uploadFinal(Long id) {
        DocumentRequest request = getRequestById(id);
        if (request == null || request.getDocumentFile() == null) return false;

        request.setStatus(2); // Approved
        saveRequest(request);
        return true;
    }

    // ✅ Upload generated PDF without changing status
    public boolean uploadFinalWithoutStatusChange(Long id) {
        DocumentRequest request = getRequestById(id);
        if (request == null || request.getGeneratedPdf() == null) return false;

        request.setDocumentFile(request.getGeneratedPdf());
        request.setDocumentName("Document.pdf");
        request.setDocumentTypeStored("application/pdf");

        System.out.println("Request documentType: " + request.getDocumentType());

        request.setPdfUploaded(true);
        saveRequest(request);
        return true;
    }
    //pdf is generated based on the doc type
    //for preview
    public byte[] generatePdfForRequest(DocumentRequest request) {
        CreateAccount student = request.getStudent();
        String type = request.getDocumentType().toLowerCase();

        switch (type) {
            case "bonafide certificate":
                return pdfGeneratorService.generateBonafideCertificate(student.getName(), student.getPrnNo(), request.getReason());

            case "hall ticket":
                String subjects = "Maths,Physics,Chemistry";
                String[] subjectArray = subjects.split(",");
                return pdfGeneratorService.generateHallTicket(
                        student.getName(),
                        student.getPrnNo(),
                        "Final Exam",subjectArray
                         // or any subjects the student has
                );

            case "leaving certificate":
                return pdfGeneratorService.generateLeavingCertificate(student.getName(), student.getPrnNo(), student.getBranch(), request.getReason());

            default:
                throw new IllegalArgumentException("Unsupported document type: " + type);
        }
    }

}
