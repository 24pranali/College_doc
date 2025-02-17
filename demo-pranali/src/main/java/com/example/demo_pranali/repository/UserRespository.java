package com.example.demo_pranali.repository;

import com.example.demo_pranali.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository <User,Long>
{


}
