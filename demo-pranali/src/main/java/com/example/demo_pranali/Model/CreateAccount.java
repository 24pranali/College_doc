package com.example.demo_pranali.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "StudentInfo")
public class CreateAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String branch;

    @Column(unique = true, nullable = false)
    private String rollNo;

    @Column(unique = true, nullable = false)
    private String prnNo;

    // Default constructor (required for JPA)
    public CreateAccount() {}


    public CreateAccount(String name, String year, String branch, String rollNo, String prnNo)
    {
        this.name = name;
        this.year = year;
        this.branch = branch;
        this.rollNo = rollNo;
        this.prnNo = prnNo;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPrnNo() {
        return prnNo;
    }

    public void setPrnNo(String prnNo) {
        this.prnNo = prnNo;
    }
}
