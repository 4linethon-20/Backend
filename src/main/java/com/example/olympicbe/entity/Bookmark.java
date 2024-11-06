package com.example.olympicbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id")
   private User user;


   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "post_id")
   private Post post;

   private LocalDateTime createdAt= LocalDateTime.now();
}
