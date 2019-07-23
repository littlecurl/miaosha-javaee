package com.example.mavendemo.service.impl;

import com.example.mavendemo.dao.ItemDOMapper;
import com.example.mavendemo.dao.ItemStockDOMapper;
import com.example.mavendemo.dataobject.ItemDO;
import com.example.mavendemo.dataobject.ItemStockDO;
import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.error.EmBusinessError;
import com.example.mavendemo.mq.MqProducer;
import com.example.mavendemo.service.ItemService;
import com.example.mavendemo.service.PromoService;
import com.example.mavendemo.service.model.ItemModel;
import com.example.mavendemo.service.model.PromoModel;
import com.example.mavendemo.validator.ValidationResult;
import com.example.mavendemo.validator.ValidatorImpl;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MqProducer mqProducer;

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {
        // 判空
        if (itemModel == null) {
            return null;
        }
        // 复制
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        // 准备数据库操作
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        // 调用上面的convert方法进行转换
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
        // 写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        // 返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelsList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelsList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }
        // 获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        // 将dataobject--->model
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);
        // 获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        // 3代表活动已经结束
        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    public ItemModel getItemByIdInCache(Integer id) {
        ItemModel itemModel = (ItemModel) redisTemplate.opsForValue().get("item_validate_" + id);
        if (itemModel == null) {
            // 如果缓存没有，就去查数据库
            itemModel = this.getItemById(id);
            redisTemplate.opsForValue().set("item_validate_" + id, itemModel);
        }
        redisTemplate.expire("item_validate_" + id, 10, TimeUnit.MINUTES);
        return itemModel;
    }

    @Override
    @Transactional //更新库存，声明事务，原子操作
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
//        int affectedRow = itemStockDOMapper.decreaseStock(itemId,amount);
//        if(affectedRow > 0){
//            // 更新库存成功
//            return true;
//        }else{
//            // 更新库存失败
//            return false;
//        }


        // 需要先在浏览器执行
        //localhost:8090/item/publishpromo?id=18
        //才能执行下面的代码
        // 更新缓存库存
        long result = redisTemplate.opsForValue().increment("promo_item_stock_" + itemId, amount.intValue() * (-1));
        if (result >= 0) {
            boolean mqResult = mqProducer.asyncReduceStock(itemId, amount);

            if (!mqResult) {
                redisTemplate.opsForValue().increment("promo_item_stock_" + itemId, amount.intValue());
                return false;
            }
            return true;
        } else {
            redisTemplate.opsForValue().increment("promo_item_stock_" + itemId, amount.intValue());
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }


}
