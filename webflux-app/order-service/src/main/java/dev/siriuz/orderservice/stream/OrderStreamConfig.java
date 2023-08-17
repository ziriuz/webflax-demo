package dev.siriuz.orderservice.stream;

import dev.siriuz.orderservice.config.Constants;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import dev.siriuz.orderservice.service.OrderManagementService;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.Duration;

//@Configuration
//@EnableKafkaStreams
public class OrderStreamConfig {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStreamConfig.class);
    @Autowired
    OrderManagementService orderManagementService;

    /*
    @Bean
    public KStream<Long, PurchaseOrder> orderConfirmationStream(StreamsBuilder builder) {
        JsonSerde<PurchaseOrder> orderSerde = new JsonSerde<>(PurchaseOrder.class);
        KStream<Long, PurchaseOrder> stream = builder
                .stream(Constants.ORDERS_PAYMENT_TOPIC, Consumed.with(Serdes.Long(), orderSerde));

        stream.join(
                        builder.stream(Constants.ORDERS_STOCK_TOPIC),
                        orderManagementService::confirm,
                        JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(10), Duration.ofSeconds(10)),
                        StreamJoined.with(Serdes.Long(), orderSerde, orderSerde)
                )
                .peek((k, o) -> LOG.info("Output: {}", o))
                .to(Constants.ORDERS_TOPIC);
        return stream;
    }

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(Constants.ORDERS_TOPIC)
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(Constants.ORDERS_PAYMENT_TOPIC)
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic stockTopic() {
        return TopicBuilder.name(Constants.ORDERS_STOCK_TOPIC)
                .partitions(3)
                .compact()
                .build();
    }
    */
}
