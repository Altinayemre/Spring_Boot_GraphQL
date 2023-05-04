package com.example.graphql.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @CreationTimestamp
     private OffsetDateTime created;

     @UpdateTimestamp
     private OffsetDateTime updated;

}
