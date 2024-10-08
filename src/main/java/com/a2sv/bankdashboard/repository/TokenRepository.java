package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    List<Token> findByUserIdAndLoggedOutFalse(String user_id);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String token);
}