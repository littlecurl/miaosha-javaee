package com.example.mavendemo.service;

import com.example.mavendemo.service.model.PromoModel;

public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);

    // 活动发布
    void publishPromo(Integer promoId);
}
