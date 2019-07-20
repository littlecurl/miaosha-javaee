package com.example.mavendemo.service;

import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    // 浏览商品列表
    List<ItemModel> listItem();
    // 浏览商品详情
    ItemModel getItemById(Integer id);
    // 库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;
    // 商品销量增加
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
