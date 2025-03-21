package com.example.demo_pranali.Model;

import jakarta.persistence.*;

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

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean status = false;  // false = Pending/Rejected, true = Approved

    @Column
    private String document; // Path or URL of the document sent by admin

    // ✅ Add this default (no-arg) constructor
    public DocumentRequest() {
    }

    // Parameterized Constructor
    public DocumentRequest(CreateAccount student, String documentType, String reason, boolean status, String document) {
        this.student = student;
        this.documentType = documentType;
        this.reason = reason;
        this.status = status;
        this.document = document;
    }

    // Getters and Setters
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    //  Add this method to return status as a String ("Approved" / "Pending")
    public String getStatusString()
    {
        return status ? "Approved" : "Pending";  // false = Pending, true = Approved
    }
    public void setStatus(Boolean status) {
        this.status = (status != null) ? status : false;  // Default to false if null
    }



}
