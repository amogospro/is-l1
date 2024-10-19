package com.lab1.lab1.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Unique, not null

    @Column(name = "password_hash", nullable = false)
    private String passwordHash; // Hashed with SHA-256

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // USER, ADMIN, PENDING_ADMIN

//    @OneToMany(mappedBy = "userOwner", fetch = FetchType.EAGER)
//    @EqualsAndHashCode.Exclude
//    private Set<Product> products;
//
//    @OneToMany(mappedBy = "userOwner", fetch = FetchType.EAGER)
//    @EqualsAndHashCode.Exclude
//    private Set<Person> persons;
//
//    @OneToMany(mappedBy = "userOwner", fetch = FetchType.EAGER)
//    @EqualsAndHashCode.Exclude
//    private Set<Organization> organizations;

    // Additional fields like registration date
    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }
}
