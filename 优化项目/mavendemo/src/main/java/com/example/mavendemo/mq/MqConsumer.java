package com.example.mavendemo.mq;

import com.alibaba.fastjson.JSON;
import com.example.mavendemo.dao.ItemStockDOMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class MqConsumer {

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    private DefaultMQPushConsumer consumer;

    // 从配置文件中注入
    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value("${mq.topicname}")
    private String topicName;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("stock_consumer_group");
        consumer.setNamesrvAddr(nameAddr);

        // 订阅
        consumer.subscribe(topicName, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 实现库存真正到库存扣减的操作
                Message message = list.get(0);
                String josnStr = new String(message.getBody());
                Map<String,Object> map = JSON.parseObject(josnStr, Map.class);
                Integer itemId = (Integer) map.get("itemId");
                Integer amount = (Integer) map.get("amount");

                itemStockDOMapper.decreaseStock(itemId, amount);
                System.out.println("接收一条同步消息，扣减库存成功！");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 一定记得启动
        consumer.start();
    }
}
