package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class IdempotencyKey {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String key;
    private String response;


    private LocalDateTime createdAt;
}
