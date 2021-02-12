package com.saikat.samples.kafkaconsumerprocessor.consumer;

import com.saikat.samples.kafkaconsumerprocessor.model.kafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Saikat
 */
@Slf4j
@Component
public class KafkaTopicListener {

    @Autowired
    ConsumerFactory<String, kafkaMessage> consumerFactory;

    MessageListener<String,kafkaMessage> messageListener;

    @KafkaListener(topics = "saikat-streaming-topic", groupId = "saikat-consumer-group-id-2", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload kafkaMessage message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId) {

        log.info("Recieved message on topic : saikat-test on Partiion ID : [ {} ]", partitionId);
        log.info("Message : {}", message.toString());
    }

}