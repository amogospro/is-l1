package com.lab1.lab1.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "import_history")
@Entity
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private LocalDateTime timestamp;
    private String status;
    private int importedObjectsCount;
}
