package com.example.mavendemo.service;

import com.example.mavendemo.service.model.PromoModel;

public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);
}
