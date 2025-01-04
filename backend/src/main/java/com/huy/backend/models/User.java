package com.huy.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true , nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String profilePicture;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
