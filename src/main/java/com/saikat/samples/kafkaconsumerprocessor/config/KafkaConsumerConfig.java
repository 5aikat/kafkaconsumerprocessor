package com.saikat.samples.kafkaconsumerprocessor.config;

import com.saikat.samples.kafkaconsumerprocessor.model.kafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Saikat
 */

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    String BOOTSTRAP_SERVERS ="localhost:9092";
    String GROUP_ID_CONFIG = "saikat-consumer-group-id-2";

    private ReceiverOptions<String, kafkaMessage> receiverOptions;

    @Bean
    public ConsumerFactory<String, kafkaMessage> consumerFactory(){
        Map<String,Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVERS);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG,GROUP_ID_CONFIG);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps,new StringDeserializer(),new JsonDeserializer<>(kafkaMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, kafkaMessage> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, kafkaMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
