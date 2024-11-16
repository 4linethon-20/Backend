package org.example.olympic.repository;


import org.example.olympic.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByUserId(String userId);
   @Query("SELECT u FROM User u LEFT JOIN FETCH u.studyList WHERE u.userId = :userId")
   Optional<User> findByUserIdWithStudies(@Param("userId") String userId);
   boolean existsByUserId(String userId);
}
