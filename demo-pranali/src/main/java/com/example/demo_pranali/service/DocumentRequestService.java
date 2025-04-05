//package com.example.demo_pranali.service;
//
//import com.example.demo_pranali.Model.CreateAccount;
//import com.example.demo_pranali.Model.DocumentRequest;
//import com.example.demo_pranali.repository.CreateAccountRepository;
//import com.example.demo_pranali.repository.DocumentRequestRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class DocumentRequestService {
//
//    @Autowired
//    private DocumentRequestRepository documentRequestRepository;
//
//    @Autowired
//    private CreateAccountRepository createAccountRepository;
//
//
//
//    // ✅ Create a new document request (with optional file)
//    public DocumentRequest createRequest(String prnNo, String documentType, String reason, int status, MultipartFile file) throws IOException {
//        Optional<CreateAccount> studentOpt = createAccountRepository.findByPrnNo(prnNo);
//        if (studentOpt.isEmpty()) {
//            throw new RuntimeException("Student with PRN " + prnNo + " not found");
//        }
//        CreateAccount student = studentOpt.get();
//
//        DocumentRequest documentRequest = new DocumentRequest();
//        documentRequest.setStudent(student);
//        documentRequest.setDocumentType(documentType);
//        documentRequest.setReason(reason);
//        documentRequest.setStatus(status);
//
//        if (file != null) {
//            documentRequest.setDocumentFile(file);
//        }
//
//        return documentRequestRepository.save(documentRequest);
//    }
//
//    // ✅ Upload a document separately
//    public boolean uploadDocument(Long id, MultipartFile file) throws IOException {
//        Optional<DocumentRequest> documentRequestOpt = documentRequestRepository.findById(id);
//        if (documentRequestOpt.isPresent()) {
//            DocumentRequest documentRequest = documentRequestOpt.get();
//            documentRequest.setDocumentFile(file);
//            documentRequestRepository.save(documentRequest);
//            return true;
//        } else {
//            return false; // Request not found
//        }
//    }
//
//    // ✅ Retrieve document file data by request ID
//    public byte[] getDocumentFile(Long id) {
//        Optional<DocumentRequest> requestOpt = documentRequestRepository.findById(id);
//        return requestOpt.map(DocumentRequest::getDocumentFile).orElse(null);
//    }
//
//
//
//
//    public List<DocumentRequest> getAllRequests() {
//        return documentRequestRepository.findAll();
//    }
//
//
//    // Get all requests for a student by PRN for status
//    public List<DocumentRequest> getRequestsByPrn(String prnNo) {
//        return documentRequestRepository.findByStudent_PrnNo(prnNo);
//    }
//
//    // ✅ Approve/Reject a request by ID
//    public DocumentRequest updateRequestStatusById(Long id, int newStatus) {
//        DocumentRequest request = documentRequestRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Request not found"));
//
//        request.setStatus(newStatus);
//        return documentRequestRepository.save(request);
//    }
//    @Autowired
//    private PdfGeneratorService pdfGeneratorService;
//
//    public boolean generateAndUploadPDF(Long requestId) {
//        Optional<DocumentRequest> requestOpt = documentRequestRepository.findById(requestId);
//        if (requestOpt.isEmpty()) return false;
//
//        DocumentRequest request = requestOpt.get();
//        if (request.getStatus() != 2) return false; // Only generate for approved requests
//
//        String studentName = request.getStudent().getName();
//        String prnNo = request.getStudent().getPrnNo();
//        String docType = request.getDocumentType();
//
//        byte[] pdfBytes = pdfGeneratorService.generateBonafideCertificate(studentName, prnNo, docType);
//
//        request.setDocumentFile(pdfBytes);
//        request.setDocumentName("Bonafide_Certificate.pdf");
//        documentRequestRepository.save(request);
//        return true;
//    }
//
//
//    public boolean uploadVerificationDocument(Long id, MultipartFile file) throws IOException {
//        Optional<DocumentRequest> documentRequestOpt = documentRequestRepository.findById(id);
//        if (documentRequestOpt.isPresent()) {
//            DocumentRequest documentRequest = documentRequestOpt.get();
//            documentRequest.setVerificationDocument(file.getBytes());
//            documentRequest.setVerificationDocumentName(file.getOriginalFilename());
//            documentRequest.setVerificationDocumentType(file.getContentType());
//            documentRequestRepository.save(documentRequest);
//            return true;
//        } else {
//            return false; // Request not found
//        }
//    }
//
//    public DocumentRequest getRequestById(Long id) {
//        return documentRequestRepository.findById(id).orElse(null);
//    }
//    public void saveRequest(DocumentRequest request) {
//        documentRequestRepository.save(request);
//    }
//    //generate the pdf
//    public boolean generateOnly(Long id) {
//        DocumentRequest request = getRequestById(id);
//        if (request == null) return false;
//
//        CreateAccount student = request.getStudent();
//        byte[] pdf;
//
//        switch (request.getDocumentType().toLowerCase()) {
//            case "bonafide":
//                pdf = pdfGeneratorService.generateBonafideCertificate(
//                        student.getName(),
//                        student.getPrnNo(),
//                        request.getReason());
//                break;
//
//            case "hall ticket":
//                pdf = pdfGeneratorService.generateHallTicket(
//                        student.getName(),
//                        student.getPrnNo(),
//                        "Final Exam",
//                        "20-04-2025");
//                break;
//
//            case "leaving certificate":
//                pdf = pdfGeneratorService.generateLeavingCertificate(
//                        student.getName(),
//                        student.getPrnNo(),
//                        student.getBranch(),
//                        request.getReason());
//                break;
//
//            default:
//                return false;
//        }
//
//        request.setDocumentFile(pdf);
//        request.setDocumentName(request.getDocumentType().replace(" ", "_") + "_generated.pdf");
//        request.setDocumentTypeStored("application/pdf");
//        // Do NOT change status here (keep it Pending)
//        saveRequest(request);
//        return true;
//    }
//
//    public boolean uploadFinal(Long id) {
//        DocumentRequest request = getRequestById(id);
//        if (request == null || request.getDocumentFile() == null) return false;
//
//        request.setStatus(2); // Approved
//        saveRequest(request);
//        return true;
//    }
//
//    public boolean uploadFinalWithoutStatusChange(Long id) {
//        DocumentRequest request = getRequestById(id);
//        if (request == null || request.getGeneratedPdf() == null) return false;
//
//        request.setDocumentFile(request.getGeneratedPdf());
//        request.setDocumentName("Document.pdf");
//        request.setDocumentType("application/pdf");
//        request.setPdfUploaded(true);
//        saveRequest(request);
//        return true;
//    }
//
//
//}

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
            documentRequest.setDocumentType(file.getContentType());
        }

        return documentRequestRepository.save(documentRequest);
    }

    // ✅ Upload document separately
    public boolean uploadDocument(Long id, MultipartFile file) throws IOException {
        DocumentRequest request = getRequestById(id);
        if (request == null || file.isEmpty()) return false;

        request.setDocumentFile(file.getBytes());
        request.setDocumentName(file.getOriginalFilename());
        request.setDocumentType(file.getContentType());

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

        request.setVerificationDocument(file.getBytes());
        request.setVerificationDocumentName(file.getOriginalFilename());
        request.setVerificationDocumentType(file.getContentType());

        saveRequest(request);
        return true;
    }

    // ✅ Generate and upload PDF after approval
    public boolean generateAndUploadPDF(Long requestId) {
        DocumentRequest request = getRequestById(requestId);
        if (request == null || request.getStatus() != 2) return false;

        byte[] pdfBytes = pdfGeneratorService.generateBonafideCertificate(
                request.getStudent().getName(),
                request.getStudent().getPrnNo(),
                request.getDocumentType()
        );

        request.setDocumentFile(pdfBytes);
        request.setDocumentName("Bonafide_Certificate.pdf");
        request.setDocumentTypeStored("application/pdf");

        saveRequest(request);
        return true;
    }

//    // ✅ Only generate (without approval)
//    public boolean generateOnly(Long id) {
//        DocumentRequest request = getRequestById(id);
//        if (request == null) return false;
//
//        CreateAccount student = request.getStudent();
//        byte[] pdf;
//
//        switch (request.getDocumentType().toLowerCase()) {
//            case "bonafide":
//                pdf = pdfGeneratorService.generateBonafideCertificate(
//                        student.getName(), student.getPrnNo(), request.getReason());
//                break;
//
//            case "hall ticket":
//                pdf = pdfGeneratorService.generateHallTicket(
//                        student.getName(), student.getPrnNo(), "Final Exam", );
//                break;
//
//            case "leaving certificate":
//                pdf = pdfGeneratorService.generateLeavingCertificate(
//                        student.getName(), student.getPrnNo(), student.getBranch(), request.getReason());
//                break;
//
//            default:
//                return false;
//        }
//
//        request.setGeneratedPdf(pdf);
//        request.setDocumentName(request.getDocumentType().replace(" ", "_") + "_generated.pdf");
//        request.setDocumentTypeStored("application/pdf");
//        saveRequest(request);
//        return true;
//    }

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
        request.setDocumentType("application/pdf");
        request.setPdfUploaded(true);
        saveRequest(request);
        return true;
    }
    public byte[] generatePdfForRequest(DocumentRequest request) {
        CreateAccount student = request.getStudent();
        String type = request.getDocumentType().toLowerCase();

        switch (type) {
            case "bonafide":
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
