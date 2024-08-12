package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.CardRequest;
import com.a2sv.bankdashboard.dto.response.CardResponse;
import com.a2sv.bankdashboard.dto.response.CardResponseDetailed;
import com.a2sv.bankdashboard.exception.ResourceNotFoundException;
import com.a2sv.bankdashboard.model.Card;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.CardRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import com.a2sv.bankdashboard.utils.RandomCardNumberGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public CardResponseDetailed addCard(CardRequest cardRequest) {
        Card card = new Card();
        card.setBalance(cardRequest.getBalance());
        card.setCardHolder(cardRequest.getCardHolder());
        card.setExpiryDate(cardRequest.getExpiryDate());
        card.setCardNumber(RandomCardNumberGenerator.generateMasterCardNumber());
        card.setPasscode(cardRequest.getPasscode());
        card.setCardType(cardRequest.getCardType());

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUserName).orElseThrow(
                () -> new ResourceNotFoundException("User Not found")
        );
        card.setUser(user);
        if (user.getAccountCash() < card.getBalance()) {
            throw new RuntimeException("Insufficient funds to add the card");
        }
        user.setAccountCash(user.getAccountCash() - card.getBalance());
        userRepository.save(user);

        return convertToCardResponseDetailed(cardRepository.save(card));
    }

    public void removeCard(String id) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Card not found")
        );
        User user = card.getUser();

        user.setAccountCash(user.getAccountCash() + card.getBalance());
        userRepository.save(user);

        cardRepository.deleteById(id);
    }

    public CardResponseDetailed getCardById(String id) {
        return convertToCardResponseDetailed( cardRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Card not found")
        ));
    }

    public List<CardResponse> getAllCards() {
        return cardRepository.findAll().stream().map(this::convertToCardResponse).toList();
    }

    public List<CardResponse> getAllCardsByUserId() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Card> cards = cardRepository.findByUserUsername(currentUserName);
        return cards.stream().map(this::convertToCardResponse).collect(Collectors.toList());
    }

    private CardResponse convertToCardResponse(Card card) {
        CardResponse response = new CardResponse();
        response.setId(card.getId());
        response.setCardHolder(card.getCardHolder());
        response.setExpiryDate(card.getExpiryDate());
        response.setSemiCardNumber(card.getCardNumber().substring(0,4));
        response.setCardType(card.getCardType());

        return response;
    }
    private CardResponseDetailed convertToCardResponseDetailed(Card card) {
        CardResponseDetailed response = new CardResponseDetailed();
        response.setId(card.getId());
        response.setBalance(card.getBalance());
        response.setCardHolder(card.getCardHolder());
        response.setExpiryDate(card.getExpiryDate());
        response.setCardNumber(card.getCardNumber());
        response.setPasscode(card.getPasscode());
        response.setCardType(card.getCardType());
        response.setUserId(card.getUser().getId());

        return response;
    }
}