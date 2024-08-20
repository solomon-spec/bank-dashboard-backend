package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card,String> {
    Page<Card> findByUserId(String user_id, Pageable pageable);
}
