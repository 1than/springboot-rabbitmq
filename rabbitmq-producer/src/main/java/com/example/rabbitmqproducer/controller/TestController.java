package com.example.rabbitmqproducer.controller;


import com.example.rabbitmqproducer.mq.MessageSend;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "rabbitmq")
public class TestController {

    @Autowired
    private MessageSend messageSend;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "send1")
    public void send() {
        messageSend.send("asga", null);
    }

    @GetMapping(value = "send2")
    public void send2() {
        Map<String, Object> map = new HashMap<>();
        map.put("test1", "test1");
        rabbitTemplate.convertAndSend("exchange_test","RoutingKey_1", map);
    }
}
