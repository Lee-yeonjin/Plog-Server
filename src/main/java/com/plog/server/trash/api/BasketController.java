package com.plog.server.trash.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.trash.dto.BasketRequest;
import com.plog.server.trash.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping("/basket")
    public ApiResponse<String>checkBasket(@RequestBody BasketRequest basketRequest){
        basketService.saveBasket(basketRequest);
        return new ApiResponse<>("신고가 접수되었습니다.");
    }
}
