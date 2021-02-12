package com.saikat.samples.kafkaconsumerprocessor.router;

import com.saikat.samples.kafkaconsumerprocessor.handler.StreamingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Saikat
 */
@Configuration
public class StreamingRouter {

    @Autowired
    StreamingHandler streamingHandler;

    /**
     * This bean initializes the routes
     * Currently a server-sent-route is configured to read streaming events from a topic
     * @return
     */

    @Bean
    public RouterFunction<ServerResponse> route(){

        return RouterFunctions
                .route(RequestPredicates.GET("/proxy/stream/{topic}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),streamingHandler::handle);

    }
}
