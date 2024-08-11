package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card,Long> {
    List<Card> findByUserUsername(String user_username);
}
