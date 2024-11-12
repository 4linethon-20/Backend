package com.example.olympicbe.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "user_id", length= 20, nullable = false, unique= true)
   private String userId;

   @Column(nullable = false)
   private String password;

   @Column(name = "profile_image", length =1000)
   private String profileImage;

   @Column(length = 20)
   private String nickname;

   @ManyToMany
   @JoinTable(
         name = "user_subject",
         joinColumns = @JoinColumn(name="user_id"),
         inverseJoinColumns = @JoinColumn(name="hobby_id")
   )
   private Set<Subject> subjects;

   @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch=FetchType.LAZY)
   private Set<Bookmark> bookmarks= new HashSet<>();
}