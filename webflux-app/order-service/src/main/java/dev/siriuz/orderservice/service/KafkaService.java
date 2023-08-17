package dev.siriuz.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.siriuz.orderservice.config.Constants;
import dev.siriuz.orderservice.dto.ProductDto;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final static Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    Producer<String, String>  kafkaProducer;

    @Autowired
    String productRequestTopic;

    public void sendProductRequest(ProductDto productDto){
        try {
            String jsonValue = objectMapper.writeValueAsString(productDto);
            logger.debug("Sending {} to Kafka topic {}", jsonValue, productRequestTopic);
            kafkaProducer.send(new ProducerRecord<>(productRequestTopic, productDto.getId(), jsonValue));
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert ProductDto to JSON ", e);
        }
    }

    public void sendProductRequest(String productId){
        logger.debug("Sending {} to Kafka topic {}", productId, productRequestTopic);
        kafkaProducer.send(new ProducerRecord<>(productRequestTopic, productId, productId));
    }

    public void publishNewOrder(PurchaseOrder order){
        try {
            String jsonValue = objectMapper.writeValueAsString(order);
            logger.debug("Sending {} to Kafka topic {}", jsonValue, productRequestTopic);
            kafkaProducer.send(new ProducerRecord<>(Constants.ORDERS_TOPIC, order.getId().toString(), jsonValue));
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert ProductDto to JSON ", e);
        }
    }
}
