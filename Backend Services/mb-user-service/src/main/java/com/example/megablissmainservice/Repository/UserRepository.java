package com.example.megablissmainservice.Repository;

import com.example.megablissmainservice.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);

    User findByTokenToForgotPassword(Long token);

    @Query("select u from User u where u.tokenToValidate=:v1")
    User findByTokenToValidate(@Param("v1") Long token);

    boolean existsByEmail(String email);

    List<User> findAllByDateCreationBefore(Instant dateCreation);

    List<User> findAllByDateCreationBetween(Instant startDate, Instant endDate);

}
