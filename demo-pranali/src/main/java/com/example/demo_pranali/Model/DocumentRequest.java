package com.example.demo_pranali.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private CreateAccount student;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String reason;

    @Column
    private Integer status;  // 1 = Pending, 2 = Approved, 3 = Rejected

    @Lob
    @Column(name = "document_file", columnDefinition = "LONGBLOB")
    private byte[] documentFile;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_type_stored")
    private String documentTypeStored;

    @Lob
    @Column(name = "verification_document", columnDefinition = "LONGBLOB")
    private byte[] verificationDocument;

    @Column(name = "verification_document_name")
    private String verificationDocumentName;

    @Column(name = "verification_document_type")
    private String verificationDocumentType;



    private Boolean isPdfGenerated = false;
    private Boolean isPdfUploaded = false;

    public Boolean getPdfGenerated() {
        return isPdfGenerated;
    }

    public void setPdfGenerated(Boolean pdfGenerated) {
        isPdfGenerated = pdfGenerated;
    }


    public Boolean getPdfUploaded() {
        return isPdfUploaded;
    }

    public void setPdfUploaded(Boolean pdfUploaded) {
        isPdfUploaded = pdfUploaded;
    }

    private Boolean isFinalized = false;
    public Boolean getFinalized() {
        return isFinalized;
    }

    public void setFinalized(Boolean finalized) {
        isFinalized = finalized;
    }



    //  Constructors
    public DocumentRequest() {}

    public DocumentRequest(CreateAccount student, String documentType, String reason, int status, String documentName, String documentTypeStored, byte[] documentFile) {
        this.student = student;
        this.documentType = documentType;
        this.reason = reason;
        this.status = status;
        this.documentName = documentName;
        this.documentTypeStored = documentTypeStored;
        this.documentFile = documentFile;
    }

    //  Set file from MultipartFile
    public void setDocumentFile(MultipartFile file) throws IOException
    {
        this.documentName = file.getOriginalFilename();
        this.documentTypeStored = file.getContentType();
        this.documentFile = file.getBytes();
    }

    //  Getters & Setters
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public CreateAccount getStudent()
    {
        return student;
    }

    public void setStudent(CreateAccount student)
    {
        this.student = student;
    }

    public String getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(String documentType)
    {
         this.documentType = documentType;
    }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public byte[] getDocumentFile() { return documentFile; }

    public void setDocumentFile(byte[] documentFile) { this.documentFile = documentFile; }

    public String getDocumentName() { return documentName; }

    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public String getDocumentTypeStored() { return documentTypeStored; }

    public void setDocumentTypeStored(String documentTypeStored) { this.documentTypeStored = documentTypeStored; }

    public String getStatusString()
    {
        if (status == null || status == 1)
        {
            return "Pending";
        }
        if (status == 2)
        {
            return "Approved";
        }
        if (status == 3)
        {
            return "Rejected";
        }

        return "Unknown";
    }


    public byte[] getVerificationDocument() {
        return verificationDocument;
    }

    public void setVerificationDocument(byte[] verificationDocument) {
        this.verificationDocument = verificationDocument;
    }

    public String getVerificationDocumentType() {
        return verificationDocumentType;
    }

    public void setVerificationDocumentType(String verificationDocumentType) {
        this.verificationDocumentType = verificationDocumentType;
    }

    public String getVerificationDocumentName() {
        return verificationDocumentName;
    }

    public void setVerificationDocumentName(String verificationDocumentName) {
        this.verificationDocumentName = verificationDocumentName;
    }

    //useful for temporarily storing generated files before they're finalized and saved into documentFile.
    @JsonProperty("verificationDocumentAvailable")
    public boolean isVerificationDocumentAvailable() {
        return this.verificationDocument != null;
    }

//    @Transient
//    private byte[] generatedPdf;
@Lob
@Column(name = "generated_pdf", columnDefinition = "LONGBLOB")
private byte[] generatedPdf;

    public byte[] getGeneratedPdf() {
        return generatedPdf;
    }

    public void setGeneratedPdf(byte[] generatedPdf) {
        this.generatedPdf = generatedPdf;
    }


    //for history
    private Boolean isDownloaded = false;
    public Boolean getDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        isDownloaded = downloaded;
    }

    @Column(length = 1000)
    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }






}
