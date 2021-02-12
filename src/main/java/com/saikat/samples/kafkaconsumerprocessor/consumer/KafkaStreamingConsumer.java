package com.saikat.samples.kafkaconsumerprocessor.consumer;

import com.saikat.samples.kafkaconsumerprocessor.model.TweetMessage;
import com.saikat.samples.kafkaconsumerprocessor.model.kafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Saikat
 */

@Slf4j
@Component
public class KafkaStreamingConsumer {

    String BOOTSTRAP_SERVERS ="localhost:9092";
    String GROUP_ID_CONFIG = "saikat-consumer-group-id-2";

    Flux<kafkaMessage> recordFlux ;

    Flux<TweetMessage> tweetFlux;

    public Flux<TweetMessage> getTweetFlux() {
        return tweetFlux;
    }

    public Flux<kafkaMessage> getRecordFlux() {
        return recordFlux;
    }


    /**
     * This is responsible to create a hot publisher
     * We are creating kafka streaming consumer
     * This is using project reactor kafka implementations
     */

    //@PostConstruct
    public void streamingConsumer(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        ReceiverOptions<String, kafkaMessage> receiverOptions = ReceiverOptions.create(props);

        DefaultKafkaConsumerFactory<String, kafkaMessage> consumerFactory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(kafkaMessage.class));

        ReceiverOptions<String,kafkaMessage> options = receiverOptions.subscription(Collections.singleton("saikatstream"))
                .withKeyDeserializer(consumerFactory.getKeyDeserializer())
                .withValueDeserializer(consumerFactory.getValueDeserializer())
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        recordFlux = KafkaReceiver.create(options).receive()
                        .map(ConsumerRecord::value)
                        .share(); // converting the flux to a hot publisher

    }

    @PostConstruct
    public void streamingTweetConsumer(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        ReceiverOptions<String, TweetMessage> receiverOptions = ReceiverOptions.create(props);

        DefaultKafkaConsumerFactory<String, TweetMessage> consumerFactory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(TweetMessage.class));

        ReceiverOptions<String,TweetMessage> options = receiverOptions.subscription(Collections.singleton("tweet-stream"))
                .withKeyDeserializer(consumerFactory.getKeyDeserializer())
                .withValueDeserializer(consumerFactory.getValueDeserializer())
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        tweetFlux = KafkaReceiver.create(options).receive()
                .map(ConsumerRecord::value)
                .share(); // converting the flux to a hot publisher
    }
}
