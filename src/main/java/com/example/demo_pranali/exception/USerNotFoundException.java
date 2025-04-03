package com.example.demo_pranali.exception;

public class USerNotFoundException extends RuntimeException
{
    public USerNotFoundException(Long id)
    {
        super(" Could not found the user with the id"+id);
    }

}
