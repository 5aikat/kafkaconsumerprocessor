package com.saikat.samples.kafkaconsumerprocessor.handler;

import com.saikat.samples.kafkaconsumerprocessor.consumer.KafkaStreamingConsumer;
import com.saikat.samples.kafkaconsumerprocessor.model.TweetMessage;
import com.saikat.samples.kafkaconsumerprocessor.model.kafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Saikat
 */
@Slf4j
@Component
public class StreamingHandler implements DefaultHandler {
    
    @Autowired
    KafkaStreamingConsumer streamingConsumer;

    /**
     * This handler function handles the route to get stream of events from a given topic
     * This responds with a flux . The flux is a hot publisher
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest){

        Optional<String> id = Optional.of(serverRequest.pathVariable("topic"));
        String topic = id.get();
        if(topic.equals("tweet-stream")){
            Flux<TweetMessage> tweetFlux = streamingConsumer.getTweetFlux();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(BodyInserters.fromPublisher(tweetFlux,TweetMessage.class));
        }else {
            Flux<kafkaMessage> recordFlux = streamingConsumer.getRecordFlux();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(BodyInserters.fromPublisher(recordFlux, kafkaMessage.class));
        }
    }
}
