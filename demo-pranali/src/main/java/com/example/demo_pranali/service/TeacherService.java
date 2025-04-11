package com.example.demo_pranali.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TeacherService {
    private static final Map<String, String> teacherEmails = new HashMap<>();

    static {
        teacherEmails.put("Prof. A Sharma", "sanika.studiumtech@gmail.com");
        teacherEmails.put("Prof. B Desai", "pictosocialheads@gmail.com");
        teacherEmails.put("Prof. C Patil", "cpatil@gmail.com");
    }

    public String getEmailByTeacherName(String name) {
        return teacherEmails.get(name);
    }
}
