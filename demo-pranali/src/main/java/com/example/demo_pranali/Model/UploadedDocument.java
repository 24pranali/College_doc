package com.example.demo_pranali.Model;

import jakarta.persistence.*;

@Entity
public class UploadedDocument {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    @Lob
    private byte[] data;

    @OneToOne
    private DocumentRequest request;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public DocumentRequest getRequest() {
        return request;
    }

    public void setRequest(DocumentRequest request) {
        this.request = request;
    }
}
