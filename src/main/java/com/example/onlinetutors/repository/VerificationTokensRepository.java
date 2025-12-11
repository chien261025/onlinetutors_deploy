package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.User;
import com.example.onlinetutors.model.VerificationTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokensRepository extends JpaRepository<VerificationTokens, Long> {
    Optional<VerificationTokens> findByToken(String token);
}
