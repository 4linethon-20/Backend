package com.example.olympicbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name="subjects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(length=20)
   private String name;

   @ManyToMany
   private Set<User> users;
}
