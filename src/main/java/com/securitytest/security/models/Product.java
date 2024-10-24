package com.securitytest.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "description cant be empty")
    private String description;

    private int stock;

    private LocalDateTime income;

    private LocalDateTime lastMove;

    private double price;

    @Enumerated(EnumType.STRING)
    private TypeMove typeMove;

}
