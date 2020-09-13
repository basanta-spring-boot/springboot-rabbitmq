package com.javatechie.rabbitmq.publisher;

import com.javatechie.rabbitmq.config.MessagingQueueConfig;
import com.javatechie.rabbitmq.dto.Order;
import com.javatechie.rabbitmq.dto.OrderStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class PublisherController {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @PostMapping("/{restaurant}")
    public String orderNow(@RequestBody Order order, @PathVariable String restaurant) throws InterruptedException {
        order.setOrderId(UUID.randomUUID().toString());
        OrderStatus orderStatus = new OrderStatus(order, "PROCESSING", "Your Order placed successfully in " + restaurant);
        rabbitTemplate.convertAndSend(MessagingQueueConfig.TOPIC_EXCHANGE_NAME, MessagingQueueConfig.ROUTING_KEY, orderStatus);
         return "Order Placed !!";
    }
}
