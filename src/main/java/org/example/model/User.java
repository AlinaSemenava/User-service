package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Data
public class User {

    @Id
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private Instant created_at = Instant.now();


}
