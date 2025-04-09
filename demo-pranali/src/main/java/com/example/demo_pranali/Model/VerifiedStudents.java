package com.example.demo_pranali.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "verified_students")
public class VerifiedStudents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prn_no", nullable = false, unique = true)
    private String prnNo;

    @Column(name = "official_email", nullable = false, unique = true)
    private String officialEmail;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrnNo() {
        return prnNo;
    }

    public void setPrnNo(String prnNo) {
        this.prnNo = prnNo;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }
}
