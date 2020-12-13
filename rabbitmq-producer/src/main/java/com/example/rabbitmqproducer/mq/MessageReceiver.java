package com.example.rabbitmqproducer.mq;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MessageReceiver implements ReturnListener {

    /**
     * 	组合使用监听
     * 	@RabbitListener @QueueBinding @Queue @Exchange
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue1", durable = "true"),
            exchange = @Exchange(name = "amq.direct",
                    durable = "true",
                    type = "direct",
                    ignoreDeclarationExceptions = "true"),
            key = "RoutingKey1"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        //	1. 收到消息以后进行业务端消费处理
        System.out.println("-----------------------");
        System.out.println("消费消息:" + message.getPayload());


        //  2. 处理成功之后 获取deliveryTag 并进行手工的ACK操作, 因为我们配置文件里配置的是 手工签收
        //	spring.rabbitmq.listener.simple.acknowledge-mode=manual
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue_1", durable = "true"),
            exchange = @Exchange(name = "exchange_test",
                    durable = "true",
                    type = "direct",
                    ignoreDeclarationExceptions = "true"),
            key = "RoutingKey_1"
    ))
    @RabbitHandler
    public void onMessage2(Message message, Channel channel) throws Exception {
        //	1. 收到消息以后进行业务端消费处理
        System.out.println("-----------------------");
        System.out.println("消费消息:" + message.getPayload());


        //  2. 处理成功之后 获取deliveryTag 并进行手工的ACK操作, 因为我们配置文件里配置的是 手工签收
        //	spring.rabbitmq.listener.simple.acknowledge-mode=manual
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }

    @Override
    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.warn("消息发送失败，返回消息 = {} {} {} {} {} {}", replyCode, replyText, exchange, routingKey, properties, new String(body));
    }
}
