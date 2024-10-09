package com.plog.server.trash.service;

import com.plog.server.trash.domain.Basket;
import com.plog.server.trash.dto.BasketRequest;
import com.plog.server.trash.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BasketService {
    private final BasketRepository basketRepository;

    @Transactional
    public Basket saveBasket(BasketRequest basketRequest) {

        Basket basket = Basket.builder()
                .basketLatitude(basketRequest.getBasketLatitude())
                .basketLongitude(basketRequest.getBasketLongitude())
                .basketIsPresent(basketRequest.isBasketIsPresent())
                .build();

        return basketRepository.save(basket);
    }
}
