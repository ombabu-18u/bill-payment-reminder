package com.billreminder.system.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    // We will store the contact number as a String
    @Column(name = "contact_no", length = 20)
    private String contactNo;

    // Password field will be added in a later security phase
    // private String password; 

    @Column(name = "join_date", updatable = false)
    @CreationTimestamp // Automatically sets the time on creation
    private LocalDateTime joinDate;

    // Relationships (e.g., OneToMany Bills) will be added later
}