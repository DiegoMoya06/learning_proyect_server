package com.dm.learning.repositories;

import com.dm.learning.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {

    @Query("SELECT d FROM Deck d JOIN FETCH d.type LEFT JOIN FETCH d.cards")
    List<Deck> findAllWithType();
}
