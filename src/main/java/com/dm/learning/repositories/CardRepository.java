package com.dm.learning.repositories;

import com.dm.learning.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findCardById(UUID cardId);

    @Query("SELECT c FROM Card c JOIN FETCH c.deck WHERE c.title = :title")
    Optional<Card> findCardByTitle(String title);
}
