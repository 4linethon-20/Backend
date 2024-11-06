package com.example.olympicbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String title;
   private String content;

   // Getter, Setter
}
