package com.example.mavendemo.service.impl;

import com.example.mavendemo.dao.PromoDOMapper;
import com.example.mavendemo.dataobject.PromoDO;
import com.example.mavendemo.service.ItemService;
import com.example.mavendemo.service.PromoService;
import com.example.mavendemo.service.model.ItemModel;
import com.example.mavendemo.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDOMapper promoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // 数据库中获取对应商品的秒杀活动
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        // dataobject--->model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if (promoModel == null){
            return null;
        }
        // 判断当前时间与活动时间对比
        // 描述活动状态
        // 1表示还未开始
        // 2正在进行
        // 3结束
        if (promoModel.getStartDate().isAfterNow()){ // 注意这里是开始时间
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()){ // 注意这里是结束时间
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2); // 其余所有时间
        }
        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId) {
        // 通过活动id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if (promoDO.getItemId() == null || promoDO.getItemId().intValue() == 0){
            return;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        // 将库存同步到Redis内
        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(), itemModel.getStock());
    }

    private PromoModel convertFromDataObject(PromoDO promoDO){
        if(promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }

}
