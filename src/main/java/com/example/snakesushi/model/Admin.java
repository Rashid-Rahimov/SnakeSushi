package com.example.snakesushi.model;

import com.example.snakesushi.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nick;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
