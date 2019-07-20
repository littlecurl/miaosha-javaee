package com.example.mavendemo.service;

import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
