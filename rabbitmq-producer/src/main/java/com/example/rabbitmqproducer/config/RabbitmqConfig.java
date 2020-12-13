package com.example.rabbitmqproducer.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_TEST = "exchange_test";

    public static final String EXCHANGE_FANOUT = "exchange_fanout";

    public static final String QUEUE_1 = "queue_1";

    public static final String QUEUE_2 = "queue_2";

    public static final String RoutingKey_1 = "RoutingKey_1";


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);

        // 消息返回, yml需要配置 publisher-returns: true
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        // 消息确认, yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("发送成功");
                log.debug("消息发送到exchange成功: {}", correlationData);
            } else {
                System.out.println("发送失败");
                log.debug("消息发送到exchange失败,原因: {}", cause);
            }
        });

        return rabbitTemplate;
    }


    /**
     * 定义 exchange_test
     *
     * @return
     */
//    @Bean
//    Exchange exchangeTest() {
//        return ExchangeBuilder.directExchange(EXCHANGE_TEST).durable(true).build();
//    }

    /**
     * 定义 exchange_fanout
     *
     * @return
     */
    @Bean
    Exchange exchangeTest2() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_FANOUT).durable(true).build();
    }


    /**
     * 创建队列 queue_1
     *
     * @return
     */
    @Bean
    Queue queue1() {
        return QueueBuilder.durable(QUEUE_1).build();
    }

    /**
     * 创建队列 queue_2
     *
     * @return
     */
    @Bean
    Queue queue2() {
        return QueueBuilder.durable(QUEUE_2).build();
    }

    /**
     *
     * 将队列和交换机通过routingKey进行绑定
     *
     * @return
     */
//    @Bean
//    Binding binding1() {
//        return BindingBuilder.bind(queue1()).to(exchangeTest()).with(RoutingKey_1).noargs();
//    }
}
