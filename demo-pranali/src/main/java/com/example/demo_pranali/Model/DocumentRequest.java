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

    // Default Constructor
    public DocumentRequest() {}

    // Parameterized Constructor
    public DocumentRequest(CreateAccount student, String documentType, String reason) {
        this.student = student;
        this.documentType = documentType;
        this.reason = reason;
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
}
