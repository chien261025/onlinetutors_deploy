package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.PasswordResetToken;
import com.example.onlinetutors.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    User findUserByToken(String token);
}
