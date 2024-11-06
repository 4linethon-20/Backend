package com.example.olympicbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name="interests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interest {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(length=20)
   private String name;

   @ManyToMany
   private Set<User> users;
}
