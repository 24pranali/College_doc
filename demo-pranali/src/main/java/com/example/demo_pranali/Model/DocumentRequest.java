//package com.example.demo_pranali.Model;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "document_requests")
//public class DocumentRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "prnNo", referencedColumnName = "prnNo", nullable = false)
//    private CreateAccount student;  // Many-to-One Relationship with StudentInfo
//
//    @Column(nullable = false)
//    private String documentType;
//
//    @Column(nullable = false)
//    private String reason;
//
////    @Column(columnDefinition = "TINYINT(1)")
////    private boolean status = false;
//@Column(nullable = true)
//private Integer status;  // 1 = Pending, 2 = Approved, 3 = Rejected
//
//
//    @Column
//    private String document; // Path or URL of the document sent by admin
//
//    // ✅ Add this default (no-arg) constructor
//    public DocumentRequest() {
//    }
//
//    // Parameterized Constructor
//    public DocumentRequest(CreateAccount student, String documentType, String reason, int status, String document) {
//        this.student = student;
//        this.documentType = documentType;
//        this.reason = reason;
//        this.status = status;
//        this.document = document;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public CreateAccount getStudent() {
//        return student;
//    }
//
//    public void setStudent(CreateAccount student) {
//        this.student = student;
//    }
//
//    public String getDocumentType() {
//        return documentType;
//    }
//
//    public void setDocumentType(String documentType) {
//        this.documentType = documentType;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public String getDocument() {
//        return document;
//    }
//
//    public void setDocument(String document) {
//        this.document = document;
//    }
//
//    //  Add this method to return status as a String ("Approved" / "Pending")
//    public String getStatusString() {
//        if (status == null || status == 1) return "Pending";
//        if (status == 2) return "Approved";
//        if (status == 3) return "Rejected";
//        return "Unknown";
//    }
////    public void setStatus(Boolean status) {
////        this.status = (status != null) ? status : false;  // Default to false if null
////    }
//
//
//
//}

package com.example.demo_pranali.Model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Table(name = "document_requests")
public class DocumentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prnNo", referencedColumnName = "prnNo", nullable = false)
    private CreateAccount student;  // Many-to-One Relationship with StudentInfo

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = true)
    private Integer status;  // 1 = Pending, 2 = Approved, 3 = Rejected

    @Column
    private String documentName; // Store file name

    @Column
    private String documentTypeStored; // Store file type (e.g., PDF, DOCX)

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentData; // Store file as BLOB

    // ✅ Default Constructor
    public DocumentRequest() {
    }

    public DocumentRequest(CreateAccount student, String documentType, String reason, int status, String documentName, String documentTypeStored, byte[] documentData) {
        this.student = student;
        this.documentType = documentType;
        this.reason = reason;
        this.status = status;
        this.documentName = documentName;
        this.documentTypeStored = documentTypeStored;
        this.documentData = documentData;
    }


    // ✅ Set Document from MultipartFile
    public void setDocumentFile(MultipartFile file) throws IOException {
        this.documentName = file.getOriginalFilename();
        this.documentTypeStored = file.getContentType();
        this.documentData = file.getBytes();
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreateAccount getStudent() {
        return student;
    }

    public void setStudent(CreateAccount student) {
        this.student = student;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentTypeStored() {
        return documentTypeStored;
    }

    public void setDocumentTypeStored(String documentTypeStored) {
        this.documentTypeStored = documentTypeStored;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    // ✅ Convert status to a string representation
    public String getStatusString() {
        if (status == null || status == 1) return "Pending";
        if (status == 2) return "Approved";
        if (status == 3) return "Rejected";
        return "Unknown";
    }
}
