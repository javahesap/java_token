package com.example.javainterviewques.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.javainterviewques.model.RefreshToken;
import com.example.javainterviewques.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);
}
