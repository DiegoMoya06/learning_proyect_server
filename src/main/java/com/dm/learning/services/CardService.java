package com.dm.learning.services;

import com.dm.learning.dto.card.CardDto;
import com.dm.learning.services.base.BaseService;

import java.util.List;

public interface CardService extends BaseService {
    List<CardDto> updateCardsWeight(String cardTitle, String weightType);
}
